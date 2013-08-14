package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot

interface DiagramAction {
	
	def void perform(XRoot root)
}