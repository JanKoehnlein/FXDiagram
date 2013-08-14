package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.tools.CompositeTool
import de.fxdiagram.core.tools.DiagramGestureTool
import de.fxdiagram.core.tools.MenuTool
import de.fxdiagram.core.tools.SelectionTool
import de.fxdiagram.core.tools.XDiagramTool
import java.util.List
import javafx.scene.Group

@Logging
class XRoot extends Group implements XActivatable {
	
	XRootDiagram diagram
	
	List<XDiagramTool> tools = newArrayList
	
	CompositeTool defaultTool
	
	XDiagramTool _currentTool
	
	new() {
		diagram = new XRootDiagram(this)
		children += diagram
		defaultTool = new CompositeTool
		defaultTool += new SelectionTool(this)
		defaultTool += new DiagramGestureTool(this)
		defaultTool += new MenuTool(this)
		tools += defaultTool
		stylesheets += "de/fxdiagram/core/XRootDiagram.css"
	}
	
	def getDiagram() {
		diagram
	}
	
	override activate() {
		diagram.activate
		currentTool = defaultTool		
	}
	
	def setCurrentTool(XDiagramTool tool) {
		var previousTool = _currentTool
		if(previousTool != null) {
			if(!previousTool.deactivate)
				LOG.severe("Could not deactivate active tool")
		}
		_currentTool = tool
		if(tool != null) {
			if(!tool.activate) {
				_currentTool = previousTool
				if(!previousTool?.activate)
					LOG.severe("Could not reactivate tool")
			}
		}
	}

	def restoreDefaultTool() {
		currentTool = defaultTool
	}
	
	def getCurrentSelection() {
		diagram.allShapes.filter[isSelectable && selected]
	}
	
}