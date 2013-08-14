package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot

class ExitAction implements DiagramAction {
	
	override perform(XRoot root) {
		System.exit(0)
	}
	
}