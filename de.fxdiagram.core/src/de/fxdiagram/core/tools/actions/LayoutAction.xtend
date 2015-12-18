package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.core.layout.Layouter
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import static extension javafx.util.Duration.*

class LayoutAction implements DiagramAction {
	
	LayoutType layoutType
	
	new(LayoutType layoutType) {
		this.layoutType = layoutType 
	}
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.L
	}
	
	override getSymbol() {
		SymbolType.GRAPH
	}

	override getTooltip() {
		'Layout diagram'
	}

	override perform(XRoot root) {
		val layoutCommand = new Layouter().createLayoutCommand(root.diagram, 1000.millis)
		root.commandStack.execute(layoutCommand)
	}
	
}