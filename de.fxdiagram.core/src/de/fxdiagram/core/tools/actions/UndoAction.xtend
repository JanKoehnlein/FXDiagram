package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.Symbol
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class UndoAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && !shiftDown && code == KeyCode.Y
	}
	
	override getSymbol() {
		Symbol.Type.REWIND
	}

	override getTooltip() {
		'Undo'
	}
	
	override perform(XRoot root) {
		root.commandStack.undo
	}
	
}

class RedoAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && shiftDown && code == KeyCode.Y
	}
	
	override getSymbol() {
		Symbol.Type.FORWARD
	}

	override getTooltip() {
		'Redo'
	}
	
	override perform(XRoot root) {
		root.commandStack.redo
	}
	
}