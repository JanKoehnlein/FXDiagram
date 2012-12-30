package de.itemis.javafx.diagram

import javafx.beans.value.ChangeListener
import javafx.scene.Group

class XNestedDiagram extends XAbstractDiagram {

	@Property XAbstractDiagram parentDiagram

	(XNestedDiagram)=>void contentsInitializer 

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
	
	def setContentsInitializer((XNestedDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}
	
	override doActivate() {
		if(contentsInitializer != null)
			this => contentsInitializer
		super.doActivate()
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
		super.internalAddNode(node)
		parentDiagram.internalAddNode(node)
	}
	
	override internalAddButton(XRapidButton button) {
		super.internalAddButton(button)
		parentDiagram.internalAddButton(button)
	}
}