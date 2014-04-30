package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.model.DomainObjectDescriptor
import java.util.List

class InterpreterContext {

	XDiagram diagram

	List<XNode> addedNodes = newArrayList
	List<XConnection> addedConnections = newArrayList
	
	def setDiagram(XDiagram diagram) {
		this.diagram = diagram
	}

	def addNode(XNode node) {
		addedNodes += node
	}

	def addConnection(XConnection connection) {
		addedConnections += connection
	}

	def <T> getConnection(DomainObjectDescriptor descriptor) {
		(addedConnections + diagram.connections).findFirst[domainObject == descriptor]
	}

	def <T> getNode(DomainObjectDescriptor descriptor) {
		(addedNodes + diagram.nodes).findFirst[domainObject == descriptor]
	}
	
	def boolean needsLayout() {
		addedNodes.size + addedConnections.size  > 1		
	}
	
	def getCommand() {
		AddRemoveCommand.newAddCommand(diagram, addedNodes + addedConnections)
	}
}