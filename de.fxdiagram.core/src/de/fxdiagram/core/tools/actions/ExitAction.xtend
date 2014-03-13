package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.Symbol
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class ExitAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.Q
	}
	
	override getSymbol() {
		Symbol.Type.EJECT
	}

	override perform(XRoot root) {
		System.exit(0)
	}
}