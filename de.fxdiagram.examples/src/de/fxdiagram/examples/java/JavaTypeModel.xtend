package de.fxdiagram.examples.java

import com.google.common.collect.HashMultimap
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.List
import org.eclipse.xtend.lib.annotations.Data

import static java.lang.reflect.Modifier.*

class JavaTypeModel {
	
	List<Constructor<?>> constructors
	
	List<Method> operations

	List<JavaProperty> properties = newArrayList

	List<JavaProperty> references = newArrayList
	
	List<Class<?>> superTypes = newArrayList
	
	new(Class<?> javaType) {
		constructors = javaType.declaredConstructors.filter[isPublic(it.modifiers)].toList
		val propertyMethods = HashMultimap.<String, Pair<Class<?>, Method>>create()
		operations = javaType.declaredMethods.filter[isPublic(it.modifiers) && !name.startsWith('impl_')].toList
		javaType.declaredMethods
			.filter[isPublic(it.modifiers)]
			.forEach[ method |
				val nameAndType = method.propertyNameAndType
				if(nameAndType != null)
					propertyMethods.put(nameAndType.key, nameAndType.value -> method)
			]
		for(propertyName: propertyMethods.keySet) {
			val type2Method = propertyMethods.get(propertyName)
			val types = type2Method.map[key].filterNull.toSet
			if(types.size == 1) {
				operations.removeAll(type2Method.map[value])
				if(types.head.primitiveOrString)
					properties += new JavaProperty(propertyName, types.head)
				else
					references += new JavaProperty(propertyName, types.head)
			} else {
				println(types.join(" "))
			}
		}
		operations.sortInplaceBy[name]
		properties.sortInplaceBy[name]
		if(javaType.superclass != null)
			superTypes.add(javaType.superclass)
		superTypes.addAll(javaType.interfaces)
	}
	
	protected def getPropertyNameAndType(Method method) {
		val methodName = method.name
		if(methodName.startsWith('get')) {
			if(method.parameterTypes.size == 0)			
				methodName.substring(3).toFirstLower -> method.returnType			
		} else if(methodName.startsWith('set')) {
			if(method.parameterTypes.size == 1)		
				methodName.substring(3).toFirstLower -> method.parameterTypes.head
		} else if(methodName.startsWith('is')) {			
			if(method.parameterTypes.size == 0)	
				methodName.substring(2).toFirstLower -> method.returnType
		} else if(methodName.endsWith('Property')) {
			// rough shortcut as it is hard to extract the property's type
			methodName.substring(0, methodName.length - 8) -> null 
		} else null
	}
	
	def getSuperTypes() {
		superTypes
	}
	
	def getConstructors() {
		constructors
	}
	
	def getOperations() {
		operations
	}
	
	def getProperties() {
		properties
	}
	
	def getReferences() {
		references
	}
	
	def boolean isPrimitiveOrString(Class<?> type) {
		type.isPrimitive || type == String
	}
}

@Data class JavaProperty {
	String name
	Class<?> type
}

@Data
class JavaSuperTypeHandle {
	Class<?> subType
	Class<?> superType
}
