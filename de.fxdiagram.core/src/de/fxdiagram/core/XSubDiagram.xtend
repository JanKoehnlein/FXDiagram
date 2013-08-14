package de.fxdiagram.core

import javafx.scene.Group

import static extension de.fxdiagram.core.Extensions.*

class XSubDiagram extends XAbstractDiagram {
	
	(XSubDiagram)=>void contentsInitializer

	Group nodeLayer = new Group
	Group buttonLayer = new Group

	new() {
		children += nodeLayer
		children += buttonLayer
		visibleProperty.addListener [ property, oldVal, newVal |
			connections.forEach[visible = newVal]
		]
	}

	def setContentsInitializer((XSubDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}

	override doActivate() {
		super.doActivate()
		contentsInitializer?.apply(this)
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

	def protected getParentDiagram() {
		parent?.diagram
	}
	
}