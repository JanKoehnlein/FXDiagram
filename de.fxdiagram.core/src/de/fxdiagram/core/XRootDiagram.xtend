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

@Logging
class XRootDiagram extends XAbstractDiagram {
	
	Group nodeLayer = new Group
	Group connectionLayer = new Group
	Group buttonLayer = new Group
	
	@FxProperty double scale = 1.0
	
	List<XDiagramTool> tools = newArrayList
	
	CompositeTool defaultTool
	
	XDiagramTool _currentTool
	
	XRoot root
	
	new(XRoot root) {
		this.root = root
		children += nodeLayer
		children += connectionLayer
		children += buttonLayer
		defaultTool = new CompositeTool
		defaultTool += new SelectionTool(this)
		defaultTool += new DiagramGestureTool(this)
		defaultTool += new MenuTool(this)
		tools += defaultTool
		stylesheets += "de/fxdiagram/core/XRootDiagram.css"
	}
	
	override doActivate() {
		super.doActivate
		currentTool = defaultTool		
	}

	def getRoot() {
		root
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
}