package de.fxdiagram.core.model

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot

@Logging
class ModelRepairer {
	
	def dispatch boolean repair(Object node) {
		false
	}
	
	def dispatch boolean repair(XRoot root) {
		root.diagram.repair
	}
	
	def dispatch boolean repair(XDiagram diagram) {
		val deleteThem = newHashSet
		diagram.connections.forEach[
			if(repair) deleteThem += it
		]
		diagram.connections -= deleteThem
		diagram.nodes.forEach[repair]
		false
	}

	def dispatch boolean repair(XNode node) {
		node.outgoingConnections.forEach [
			if (source != node) {
				LOG.severe('Node ' + it + ' is not source of outgoning connection ' + it)
				source = node
			}
		]
		node.incomingConnections.forEach [
			if (target != node) {
				LOG.severe('Node ' + it + ' is not target of incoming connection ' + it)
				target = node
			}
		]
		if (node instanceof XDiagramContainer)
			node.innerDiagram.repair
		false
	}

	def dispatch boolean repair(XConnection it) {
		var deleteIt = false
		if (source == null) {
			LOG.severe('Connection ' + it + ' lacks source node')
			deleteIt = true
		} else {
			if (!source.outgoingConnections.contains(it)) {
				LOG.severe('Connection ' + it + ' not contained in outgoing connections of source node')
				source.outgoingConnections.add(it)
			}
		}
		if (target == null) {
			LOG.severe('Connection ' + it + ' lacks target node')
			deleteIt = true
		} else {
			if (!target.incomingConnections.contains(it)) {
				LOG.severe('Connection ' + it + ' not contained in incoming connections of target node')
				target.incomingConnections.add(it)
			}
		}
		return deleteIt
	}

}