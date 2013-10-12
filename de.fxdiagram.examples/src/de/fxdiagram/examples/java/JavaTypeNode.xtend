package de.fxdiagram.examples.java

import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.Separator
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

class JavaTypeNode extends XNode {
	
	Class<?> javaType
	
	val name = new Text
	val propertyCompartment = new VBox
	val operationCompartment = new VBox
	
	JavaTypeModel model
	
	new(Class<?> javaType) {
		super(javaType.simpleName)
		this.javaType = javaType
		name.text = javaType.simpleName
		model = new JavaTypeModel(javaType)
		node = new RectangleBorderPane => [
			children += new VBox => [
				children += name => [
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
					VBox.setMargin(it, new Insets(12, 12, 12, 12))
				]
				children += new Separator 
				children += propertyCompartment => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				children += new Separator
				children += operationCompartment => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				alignment = Pos.CENTER
			]
		]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	def populateComprtments() {
		model.properties.limit.forEach [
			property |
			propertyCompartment.children += new Text => [
				text = '''«property.name»: «property.type.simpleName»''' 
			]
		]
		model.constructors.forEach [
			constructor |
			operationCompartment.children += new Text => [
				text = '''«javaType.simpleName»(«constructor.parameterTypes.map[simpleName].join(', ')»)''' 
			]
		]
		model.operations.limit.forEach [
			method |
			operationCompartment.children += new Text => [
				text = '''«method.name»(«method.parameterTypes.map[simpleName].join(', ')»): «method.returnType.simpleName»''' 
			]
		]
	}
	
	protected def <T> limit(List<T> list) {
		if(list.empty)
			list
		else if(isActive)
			list
		else 
			list.subList(0, Math.min(list.size, 4))
	}
	
	def getJavaType() {
		javaType
	}
	
	def getJavaTypeModel() {
		model
	}
	
	override activate() {
		super.activate
		populateComprtments
		new AddSuperTypeRapidButtonBehavior(this).activate
		new AddReferenceRapidButtonBehavior(this).activate
	}
}

