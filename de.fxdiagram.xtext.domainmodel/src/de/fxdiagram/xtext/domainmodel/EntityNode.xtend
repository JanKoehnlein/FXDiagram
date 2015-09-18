package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.shapes.BaseNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity

import static extension de.fxdiagram.mapping.reconcile.MappingLabelListener.*

@ModelNode
class EntityNode extends BaseNode<Entity> {
	
	public static val ENTITY_NAME = 'entityName'
	public static val ATTRIBUTE = 'attribute'
	public static val OPERATION = 'operation'
	
	VBox nameCompartment
	VBox attributeCompartment
	VBox operationsCompartment
	
	new(IMappedElementDescriptor<Entity> descriptor) {
		super(descriptor)
	}
	
	override protected createNode() {
		val pane = new RectangleBorderPane => [
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				#[
					new Stop(0, Color.rgb(158, 188, 227)), 
					new Stop(1, Color.rgb(220, 230, 255))
				])
			children += new VBox => [
				padding = new Insets(10, 20, 10, 20)
				spacing = 10
				alignment = Pos.CENTER
				children += nameCompartment = new VBox => [
					alignment = Pos.CENTER
				]
				children += attributeCompartment = new VBox
				children += operationsCompartment = new VBox
			]
		]
		labelsProperty.addMappingLabelListener(
			ENTITY_NAME -> nameCompartment,
			ATTRIBUTE -> attributeCompartment,
			OPERATION -> operationsCompartment)
		pane
	}
}