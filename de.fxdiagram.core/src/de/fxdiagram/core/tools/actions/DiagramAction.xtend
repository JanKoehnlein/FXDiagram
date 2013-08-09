package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRootDiagram

interface DiagramAction {
	
	def void perform(XRootDiagram rootDiagram)
}