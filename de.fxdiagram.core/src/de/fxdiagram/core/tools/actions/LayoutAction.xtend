package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter

import static extension javafx.util.Duration.*

class LayoutAction implements DiagramAction {
	
	override perform(XRoot root) {
		new Layouter().layout(root.diagram, 2.seconds)
	}
	
}