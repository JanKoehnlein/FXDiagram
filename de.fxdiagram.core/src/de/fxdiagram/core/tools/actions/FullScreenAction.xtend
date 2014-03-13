package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Stage

class FullScreenAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && shiftDown && code == KeyCode.F
	}

	override getSymbol() {
		null
	}

	override perform(XRoot root) {
		val window = root.scene.window
		if (window instanceof Stage) {
			window.fullScreen = true
			return
		}
	}

}
