package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import javafx.scene.Group

@Logging
class XRootDiagram extends XAbstractDiagram {
	
	Group nodeLayer = new Group
	Group buttonLayer = new Group
	
	XRoot root
	
	new(XRoot root) {
		this.root = root
		children += nodeLayer
		children += buttonLayer
	}
	
	override doActivate() {
		super.doActivate
	}

	override getNodeLayer() {
		nodeLayer
	}
	
	override getConnectionLayer() {
		nodeLayer		
	}
	
	override getButtonLayer() {
		buttonLayer
	}
}