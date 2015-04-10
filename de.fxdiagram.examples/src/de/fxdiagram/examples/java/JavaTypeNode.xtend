package de.fxdiagram.examples.java

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.geometry.VPos
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@ModelNode
class JavaTypeNode extends XNode {
	
	val propertyCompartment = new VBox
	val operationCompartment = new VBox
	
	val contentArea= new VBox
	
	JavaTypeModel model
	
	new(JavaTypeDescriptor domainObject) {
		super(domainObject)
		placementHint = Side.BOTTOM
	}
	
	protected override createNode() {
		new RectangleBorderPane => [
			children += contentArea => [
				padding = new Insets(15, 20, 15, 20)
				children += new Text => [
					text = javaType.simpleName
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
				]
				alignment = Pos.CENTER
			]
		]
	}
	
	def getJavaType() {
		(domainObject as JavaTypeDescriptor).domainObject
	}
	
	def getJavaTypeModel() {
		if(model == null) 
			model = new JavaTypeModel(javaType)
		model
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	def populateCompartments() {
		javaTypeModel.properties.limit.forEach [
			property |
			propertyCompartment.children += new Text => [
				text = '''«property.name»: «property.type.simpleName»''' 
			]
		]
		javaTypeModel.constructors.forEach [
			constructor |
			operationCompartment.children += new Text => [
				text = '''«javaType.simpleName»(«constructor.parameterTypes.map[simpleName].join(', ')»)''' 
			]
		]
		javaTypeModel.operations.limit.forEach [
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
	
	override doActivate() {
		super.doActivate
		propertyCompartment.padding = new Insets(15,0,0,0)
		operationCompartment.padding = new Insets(10,0,0,0)
		populateCompartments
		val inflator = new Inflator(this, contentArea)
		inflator.addInflatable(propertyCompartment, 1)
		inflator.addInflatable(operationCompartment, 2)
		addBehavior(new AddSuperTypeRapidButtonBehavior(this))
		addBehavior(new AddReferenceRapidButtonBehavior(this))
		root.commandStack.execute(inflator.inflateCommand)
	}
	
	override toString() {
		javaType.simpleName
	}
	
}

