package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.tools.CompositeTool
import de.fxdiagram.core.tools.DiagramGestureTool
import de.fxdiagram.core.tools.MenuTool
import de.fxdiagram.core.tools.SelectionTool
import de.fxdiagram.core.tools.XDiagramTool
import java.util.List
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.transform.Affine

import static de.fxdiagram.core.binding.NumberExpressionExtensions.*

@Logging
class XRoot extends Parent implements XActivatable {
	
	Group headsUpDisplay = new Group
	
	Group diagramCanvas  = new Group
	
	public static val MIN_SCALE = EPSILON
	
	@FxProperty double diagramScale = 1.0
	
	Affine diagramTransform = new Affine
	
	XDiagram diagram
	
	List<XDiagramTool> tools = newArrayList
	
	CompositeTool defaultTool
	
	XDiagramTool _currentTool
	
	new() {
		children += diagramCanvas
		children += headsUpDisplay
		
		diagram = new XDiagram
		diagramCanvas.children += diagram
		diagramCanvas.transforms.setAll(diagramTransform)

		defaultTool = new CompositeTool
		defaultTool += new SelectionTool(this)
		defaultTool += new DiagramGestureTool(this)
		defaultTool += new MenuTool(this)
		tools += defaultTool
		stylesheets += "de/fxdiagram/core/XRoot.css"
	}
	
	def getDiagram() {
		diagram
	}
	
	def getHeadsUpDisplay() {
		headsUpDisplay
	}
	
	def getDiagramTransform() {
		diagramTransform
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