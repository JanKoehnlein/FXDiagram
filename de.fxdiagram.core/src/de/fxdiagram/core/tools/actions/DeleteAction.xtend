package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot

class DeleteAction implements DiagramAction {
	
	override perform(XRoot root) {
  		val elements = root.currentSelection
		val nodes = elements.filter(XNode)
		nodes.map[incomingConnections].forEach[
			root.diagram.connections -= it
		]
		nodes.map[outgoingConnections].forEach[
			root.diagram.connections -= it
		]
		elements.filter(XConnection).forEach[
			root.diagram.connections -= it
		]
		nodes.forEach[
			root.diagram.nodes -= it
		]
	}
}
