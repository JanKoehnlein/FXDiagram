package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import javafx.scene.Group
import de.fxdiagram.core.XControlPoint

class AuxiliaryLinesSupport {
	
	AuxiliaryLinesCache cache
	Group group = new Group
	
	
	new(XDiagram diagram) {
		this.cache = new AuxiliaryLinesCache(diagram)
		diagram.buttonLayer.children += group
	}
	
	def show(Iterable<? extends XShape> selection) {
		group.children.clear()
		val selectedNodes = selection.filter(XNode)
		if(selectedNodes.size == 1) {
			val lines = cache.getAuxiliaryLines(selectedNodes.head)
			lines.forEach[ group.children += createNode ]
		} else {
			val selectedControlPoints = selection.filter(XControlPoint)
			if(selectedControlPoints.size == 1) {
				val lines = cache.getAuxiliaryLines(selectedControlPoints.head)
				lines.forEach[ group.children += createNode ]
			}
		}
	}
	
	def hide() {
		group.children.clear()		
	}
}