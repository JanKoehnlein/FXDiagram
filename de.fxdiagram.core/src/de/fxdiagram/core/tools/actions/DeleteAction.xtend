package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.AddRemoveCommand

class DeleteAction implements DiagramAction {
	
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
