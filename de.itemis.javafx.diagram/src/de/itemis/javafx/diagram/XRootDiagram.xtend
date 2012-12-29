package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.tools.SelectionTool
import de.itemis.javafx.diagram.tools.ZoomTool
import javafx.scene.Group

class XRootDiagram extends XAbstractDiagram {
	
	Group nodeLayer = new Group
	Group connectionLayer = new Group
	Group buttonLayer = new Group
	
	new() {
		children += nodeLayer
		children += connectionLayer
		children += buttonLayer
	}
	
	override doActivate() {
		super.doActivate
		new ZoomTool(this)   
		new SelectionTool(this)     
	}

	override getNodeLayer() {
		nodeLayer
	}
	
	override getConnectionLayer() {
		connectionLayer
	}
	
	override getButtonLayer() {
		buttonLayer
	}
}