package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XAbstractDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import javafx.scene.Group

class AuxiliaryLinesSupport {
	
	AuxiliaryLinesCache cache
	Group group = new Group
	
	
	new(XAbstractDiagram diagram) {
		this.cache = new AuxiliaryLinesCache(diagram)
		diagram.buttonLayer.children += group
	}
	
	def show(Iterable<XShape> selection) {
		group.children.clear()
		val selectedNodes = selection.filter(XNode)
		if(selectedNodes.size == 1) {
			val lines = cache.getAuxiliaryLines(selectedNodes.head)
			lines.forEach[ group.children += createNode ]
		}	
	}
	
	def hide() {
		group.children.clear()		
	}
}