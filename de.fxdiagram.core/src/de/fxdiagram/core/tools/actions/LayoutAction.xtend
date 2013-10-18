package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter

import static extension javafx.util.Duration.*
import de.fxdiagram.core.layout.LayoutType

class LayoutAction implements DiagramAction {
	
	LayoutType layoutType
	
	new(LayoutType layoutType) {
		this.layoutType = layoutType 
	}
	
	override perform(XRoot root) {
		new Layouter().layout(layoutType, root.diagram, 1000.millis)
	}
	
}