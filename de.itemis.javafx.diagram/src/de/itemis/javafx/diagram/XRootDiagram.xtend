package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.tools.SelectionTool
import javafx.scene.Group
import de.itemis.javafx.diagram.tools.DiagramGestureTool

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
		new DiagramGestureTool(this)   
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