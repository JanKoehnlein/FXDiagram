package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot

class SelectAllAction implements DiagramAction {
	
	override perform(XRoot root) {
		root.diagram.allShapes.forEach[if(selectable) selected = true]
	}
}