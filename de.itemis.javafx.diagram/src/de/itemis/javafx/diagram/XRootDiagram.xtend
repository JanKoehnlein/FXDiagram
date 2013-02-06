package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.tools.DiagramGestureTool
import de.itemis.javafx.diagram.tools.SelectionTool
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Group

class XRootDiagram extends XAbstractDiagram {
	
	Group nodeLayer = new Group
	Group connectionLayer = new Group
	Group buttonLayer = new Group
	
	DoubleProperty scaleProperty = new SimpleDoubleProperty(1.0)
	
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
	
	def getScaleProperty() {
		scaleProperty
	}
}