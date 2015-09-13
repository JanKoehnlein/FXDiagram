package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class UndoAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && !shiftDown && code == KeyCode.Y
	}
	
	override getSymbol() {
		SymbolType.REWIND
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
		SymbolType.FORWARD
	}

	override getTooltip() {
		'Redo'
	}
	
	override perform(XRoot root) {
		root.commandStack.redo
	}
	
}