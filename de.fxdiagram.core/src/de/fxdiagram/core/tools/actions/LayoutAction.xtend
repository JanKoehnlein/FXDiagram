package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRootDiagram

import static extension javafx.util.Duration.*
import de.fxdiagram.core.layout.Layouter

class LayoutAction implements DiagramAction {
	
	override perform(XRootDiagram diagram) {
		new Layouter().layout(diagram, 2.seconds)
	}
	
}