package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.AddRemoveCommand
import eu.hansolo.enzo.radialmenu.Symbol
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class DeleteAction implements DiagramAction {
	
	override matches(KeyEvent event) {
		event.code == KeyCode.DELETE || event.code == KeyCode.BACK_SPACE
	}
	
	override getSymbol() {
		Symbol.Type.TRASH
	}
	
	override perform(XRoot root) {
  		val elements = root.currentSelection
		val nodes = elements.filter(XNode)
  		val deleteThem = 
			nodes.map[incomingConnections].flatten + 
			nodes.map[outgoingConnections].flatten +
			elements
		root.commandStack.execute(AddRemoveCommand.newRemoveCommand(root.diagram, deleteThem))
	}
	
}
