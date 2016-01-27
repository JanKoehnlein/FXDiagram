package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class SelectAllAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.A
	}

	override getSymbol() {
		SymbolType.SELECTION1
	}

	override getTooltip() {
		'Select all'
	}

	override perform(XRoot root) {
		root.diagram.allShapes.filter[
			!(it instanceof XConnectionLabel)
		].forEach[
			if(selectable) selected = true
		]
	}
}
