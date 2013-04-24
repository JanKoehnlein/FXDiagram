package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.tools.CompositeTool
import de.itemis.javafx.diagram.tools.DiagramGestureTool
import de.itemis.javafx.diagram.tools.SelectionTool
import de.itemis.javafx.diagram.tools.XDiagramTool
import java.util.List
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Group

import static extension de.itemis.javafx.diagram.Extensions.*

class XRootDiagram extends XAbstractDiagram {
	
	Group nodeLayer = new Group
	Group connectionLayer = new Group
	Group buttonLayer = new Group
	
	DoubleProperty scaleProperty = new SimpleDoubleProperty(1.0)
	
	List<XDiagramTool> tools = newArrayList
	
	CompositeTool defaultTool
	
	XDiagramTool _currentTool
	
	new() {
		children += nodeLayer
		children += connectionLayer
		children += buttonLayer
		defaultTool = new CompositeTool
		defaultTool += new SelectionTool(this)
		defaultTool += new DiagramGestureTool(this)
		tools += defaultTool
	}
	
	override doActivate() {
		super.doActivate
		currentTool = defaultTool		
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
	
	def setCurrentTool(XDiagramTool tool) {
		var previousTool = _currentTool
		if(previousTool != null) {
			if(!previousTool.deactivate)
				logger.severe("Could not deactivate active tool")
		}
		_currentTool = tool
		if(tool != null) {
			if(!tool.activate) {
				_currentTool = previousTool
				if(!previousTool?.activate)
					logger.severe("Could not reactivate tool")
			}
		}
	}

	def restoreDefaultTool() {
		currentTool = defaultTool
	}
}