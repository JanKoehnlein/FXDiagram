package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRootDiagram

class SelectAllAction implements DiagramAction {
	
	override perform(XRootDiagram diagram) {
		diagram.allShapes.forEach[if(selectable) selected = true]
	}
}