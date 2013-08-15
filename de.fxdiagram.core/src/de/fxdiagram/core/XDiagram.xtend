package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import javafx.scene.Group

import static extension de.fxdiagram.core.Extensions.*

class XDiagram extends XAbstractDiagram {
	
	@FxProperty@ReadOnly boolean isRootDiagram
	
	Group nodeLayer = new Group
	Group buttonLayer = new Group
	
	XAbstractDiagram parentDiagram
	
	(XDiagram)=>void contentsInitializer
	
	new() {
		children += nodeLayer
		children += buttonLayer
		parentProperty.addListener [
			property, oldValue, newValue |
			parentDiagram = newValue.diagram
			isRootDiagramProperty.set(parentDiagram == null)
		]
		isRootDiagramProperty.addListener [
			property, oldValue, newValue |
			if(newValue) 
				nodeLayer.children += connections
			else 
				nodeLayer.children -= connections
		]
	}
	
	override doActivate() {
		super.doActivate
		contentsInitializer?.apply(this)
	}

	def setContentsInitializer((XDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}
	
	def getParentDiagram() {
		parentDiagram
	}

	override getNodeLayer() {
		nodeLayer
	}
	
	override getConnectionLayer() {
		if(isRootDiagram) 
			nodeLayer
		else 
			parentDiagram.nodeLayer		
	}
	
	override getButtonLayer() {
		buttonLayer
	}
}