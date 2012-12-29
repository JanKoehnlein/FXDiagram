package de.itemis.javafx.diagram

import javafx.scene.Group
import javafx.beans.value.ChangeListener

class XNestedDiagram extends XAbstractDiagram {

	@Property XAbstractDiagram parentDiagram

	Group nodeLayer = new Group
	Group buttonLayer = new Group

	new() {
		children += nodeLayer
		children += buttonLayer
		scaleX = 0.3
		scaleY = 0.3
		managed = false
		val ChangeListener<Boolean> visibilityListener = [ 
			property, oldVal, newVal |
			connections.forEach[ visible = newVal ]
		]
		visibleProperty.addListener(visibilityListener)
	}
	
	override getNodeLayer() {
		nodeLayer
	}

	override getConnectionLayer() {
		parentDiagram.connectionLayer
	}
	
	override getButtonLayer() {
		buttonLayer
	}
	
	override internalAddNode(XNode node) {
		parentDiagram.internalAddNode(node)
	}
	
	override internalAddButton(XRapidButton button) {
		parentDiagram.internalAddButton(button)
	}
	
}