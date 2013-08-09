package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRootDiagram

class ExitAction implements DiagramAction {
	
	override perform(XRootDiagram rootDiagram) {
		System.exit(0)
	}
	
}