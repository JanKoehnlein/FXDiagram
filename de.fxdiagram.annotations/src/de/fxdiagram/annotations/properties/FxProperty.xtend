package de.fxdiagram.annotations.properties

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import java.util.List
import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ListProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.ReadOnlyFloatProperty
import javafx.beans.property.ReadOnlyFloatWrapper
import javafx.beans.property.ReadOnlyIntegerProperty
import javafx.beans.property.ReadOnlyIntegerWrapper
import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.beans.property.ReadOnlyLongProperty
import javafx.beans.property.ReadOnlyLongWrapper
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.TransformationParticipant
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeReference
import org.eclipse.xtend.lib.macro.declaration.Visibility

/** 
 * An active annotation which turns a simple field into a lazy JavaFX property as described  
 * <a href="http://blog.netopyr.com/2011/05/19/creating-javafx-properties/">here</a>.
 * 
 * That is it 
 * <ul>
 *  <li> adds a field with the corresponding JavaFX property type,
 *  <li> a getter method
 *  <li> a setter method (not for {@link #readOnly} properties)
 *  <li> and an accessor to the JavaFX property (a <code>ReadOnlyXWrapper</code> for {@link #readOnly} properties).
 * </ul>
 */
@Active(FxPropertyCompilationParticipant)
@Target(ElementType.FIELD)
annotation FxProperty {
	val readOnly = false
}

class FxPropertyCompilationParticipant implements TransformationParticipant<MutableFieldDeclaration> {

	override doTransform(List<? extends MutableFieldDeclaration> fields, extension TransformationContext context) {
		val annotationType = findTypeGlobally(FxProperty)
		for (f : fields) {
			val annotation = f.findAnnotation(annotationType)
			val isReadOnly = annotation.getValue('readOnly') != Boolean.FALSE
            val isList = f.type.type.qualifiedName == 'javafx.collections.ObservableList' 
			val fieldName = f.simpleName
			val fieldType = f.type
			val propName = f.simpleName + 'Property'
			val propType = f.type.toPropertyType(isReadOnly, context)
			val propTypeAPI = f.type.toPropertyType_API(isReadOnly, context)

			val clazz = f.declaringType as MutableClassDeclaration
				createField(f, clazz, propName, propType, fieldName, fieldType, isReadOnly, isList,
					propTypeAPI)
		}
	}

	def createField(MutableFieldDeclaration f, MutableClassDeclaration clazz,
		String propName, TypeReference propType, String fieldName, TypeReference fieldType, boolean isReadOnly,
		boolean isList, TypeReference propTypeAPI) {
		if (f.initializer === null) {
			clazz.addField(propName) [
				type = propType
				initializer = ['''new «toJavaCode(propType)»(this, "«fieldName»")''']
			]
		} else {
			clazz.addField(propName) [
				type = propType
				initializer = ['''new «toJavaCode(propType)»(this, "«fieldName»",_init«fieldName.toFirstUpper»())''']
			]

			clazz.addMethod("_init" + fieldName.toFirstUpper) [
				returnType = fieldType
				visibility = Visibility.PRIVATE
				static = true
				final = true
				body = f.initializer
			]
		}

		clazz.addMethod('get' + fieldName.toFirstUpper) [
			returnType = fieldType
			body = [
				'''
					return this.«propName».get();
				''']
		]

		if (!isReadOnly && !isList) {
			clazz.addMethod('set' + fieldName.toFirstUpper) [
				addParameter(fieldName, fieldType)
				body = [
					'''
						this.«propName».set(«fieldName»);
					''']
			]
		}

		clazz.addMethod(fieldName + 'Property') [
			returnType = propTypeAPI
			body = [
				'''
					return «IF isReadOnly»this.«propName».getReadOnlyProperty()«ELSE»this.«propName»«ENDIF»;
				''']
		]

		f.remove
	}

	def String defaultValue(TypeReference ref) {
		switch ref.toString {
			case 'boolean': "false"
			case 'double': "0d"
			case 'float': "0f"
			case 'long': "0"
			case 'int': "0"
			default: "null"
		}
	}
	
	def TypeReference toPropertyType_API(TypeReference ref, boolean isReadOnly, extension TransformationContext context) {
		if (isReadOnly) {
			switch ref.type.qualifiedName {
				case 'boolean': ReadOnlyBooleanProperty.newTypeReference
				case 'double': ReadOnlyDoubleProperty.newTypeReference
				case 'float': ReadOnlyFloatProperty.newTypeReference
				case 'long': ReadOnlyLongProperty.newTypeReference
				case 'java.lang.String': ReadOnlyStringProperty.newTypeReference
				case 'int': ReadOnlyIntegerProperty.newTypeReference
				case 'javafx.collections.ObservableList': ReadOnlyListProperty.newTypeReference(
					ref.actualTypeArguments.head)
				default: ReadOnlyObjectProperty.newTypeReference(ref)
			}
		} else {
			switch ref.type.qualifiedName {
				case 'boolean': BooleanProperty.newTypeReference
				case 'double': DoubleProperty.newTypeReference
				case 'float': FloatProperty.newTypeReference
				case 'long': LongProperty.newTypeReference
				case 'java.lang.String': StringProperty.newTypeReference
				case 'int': IntegerProperty.newTypeReference
				case 'javafx.collections.ObservableList': ListProperty.newTypeReference(ref.actualTypeArguments.head)
				default: ObjectProperty.newTypeReference(ref)
			}
		}
	}

	def TypeReference toPropertyType(TypeReference ref, boolean isReadOnly, extension TransformationContext context) {
		if (isReadOnly) {
			switch ref.type.qualifiedName {
				case 'boolean': ReadOnlyBooleanWrapper.newTypeReference
				case 'double': ReadOnlyDoubleWrapper.newTypeReference
				case 'float': ReadOnlyFloatWrapper.newTypeReference
				case 'long': ReadOnlyLongWrapper.newTypeReference
				case 'java.lang.String': ReadOnlyStringWrapper.newTypeReference
				case 'int': ReadOnlyIntegerWrapper.newTypeReference
				case 'javafx.collections.ObservableList': ReadOnlyListWrapper.newTypeReference(
					ref.actualTypeArguments.head)
				default: ReadOnlyObjectWrapper.newTypeReference(ref)
			}
		} else {
			switch ref.type.qualifiedName {
				case 'boolean': SimpleBooleanProperty.newTypeReference
				case 'double': SimpleDoubleProperty.newTypeReference
				case 'float': SimpleFloatProperty.newTypeReference
				case 'long': SimpleLongProperty.newTypeReference
				case 'java.lang.String': SimpleStringProperty.newTypeReference
				case 'int': SimpleIntegerProperty.newTypeReference
				case 'javafx.collections.ObservableList': SimpleListProperty.newTypeReference(
					ref.actualTypeArguments.head)
				default: SimpleObjectProperty.newTypeReference(ref)
			}
		}
	}
}
