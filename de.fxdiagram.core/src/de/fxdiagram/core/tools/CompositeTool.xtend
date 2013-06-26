package de.fxdiagram.core.tools

import java.util.List

class CompositeTool implements XDiagramTool {
	
	List<XDiagramTool> children = newArrayList

	def boolean operator_add(XDiagramTool child) {
		children.add(child)
	}	
	
	override activate() {
		children.map[activate].reduce[a,b | a||b]
	}
	
	override deactivate() {
		children.map[deactivate].reduce[a,b | a||b]
	}
}