package de.fxdiagram.examples.ecore

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import javafx.geometry.Side

@ModelNode
class EClassNode extends XNode {
	
	val attributeCompartment = new VBox
	val operationCompartment = new VBox

	val contentArea = new VBox
	
	new(EClassDescriptor domainObject) {
		super(domainObject)
		placementHint = Side.BOTTOM
	}			

	def getEClass() {
		(domainObjectDescriptor as EClassDescriptor).domainObject
	}

	protected override createNode() {
		new RectangleBorderPane => [
			children += contentArea => [
				padding = new Insets(15, 20, 15, 20)
				children += new Text => [
					text = EClass.name 
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
//					VBox.setMargin(it, new Insets(12, 12, 12, 12))
				]
				alignment = Pos.CENTER
			]
		]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	def populateCompartments() {
		EClass.EAttributes.limit.forEach [
			attribute |
			attributeCompartment.children += new Text => [
				text = '''«attribute.name»: «attribute.EType.name»''' 
			]
		]
		EClass.EOperations.limit.forEach [
			operation |
			operationCompartment.children += new Text => [
				text = '''«operation.name»(«operation.EParameters.map[EType.name].join(', ')»): «operation.EType.name»''' 
			]
		]
		null
	}
	
	protected def <T> limit(List<T> list) {
		if(list.empty)
			list
		else if(getIsActive)
			list
		else 
			list.subList(0, Math.min(list.size, 4))
	}
	
	override doActivate() {
		super.doActivate()
		attributeCompartment.padding = new Insets(15,0,0,0)
		operationCompartment.padding = new Insets(10,0,0,0)
		populateCompartments
		val inflator = new Inflator(this, contentArea)
		inflator.addInflatable(attributeCompartment, 1)
		inflator.addInflatable(operationCompartment, 2)
		addBehavior(new AddESuperTypeRapidButtonBehavior(this))
		addBehavior(new AddEReferenceRapidButtonBehavior(this))
		root.commandStack.execute(inflator.inflateCommand)
	}
}



