package de.itemis.javafx.diagram

import javafx.scene.Group

import static extension de.itemis.javafx.diagram.Extensions.*

class XNestedDiagram extends XAbstractDiagram {

	(XNestedDiagram)=>void contentsInitializer 

	Group nodeLayer = new Group
	Group buttonLayer = new Group

	new() {
		children += nodeLayer
		children += buttonLayer
		scale = 0.2
		managed = false
		visibleProperty.addListener [ 
			property, oldVal, newVal |
			connections.forEach[ visible = newVal ]
		]
	}
	
	def setScale(double scale) {
		scaleX = scale 
		scaleY = scale
	}
	
	def setContentsInitializer((XNestedDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}
	
	override doActivate() {
		contentsInitializer?.apply(this)
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
	
	def protected getParentDiagram() {
		parent?.diagram
	}
}