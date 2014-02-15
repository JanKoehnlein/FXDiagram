package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot

class UndoAction implements DiagramAction {
	
	override perform(XRoot root) {
		root.commandStack.undo
	}
	
}

class RedoAction implements DiagramAction {
	
	override perform(XRoot root) {
		root.commandStack.redo
	}
	
}