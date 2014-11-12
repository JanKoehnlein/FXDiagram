package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.model.DomainObjectDescriptor
import java.util.List
import org.eclipse.xtend.lib.annotations.Accessors

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class InterpreterContext {

	protected XDiagram diagram
	
	@Accessors boolean isNewDiagram

	List<XNode> addedNodes = newArrayList
	List<XConnection> addedConnections = newArrayList
	
	def setDiagram(XDiagram diagram) {
		this.diagram = diagram
	}

	def addNode(XNode node) {
		if(diagram.root != null)
			addedNodes += node
		else 
			diagram.nodes += node
	}

	def addConnection(XConnection connection) {
		if(diagram.root != null)
			addedConnections += connection
		else 
			diagram.connections += connection
	}

	def <T> getConnection(DomainObjectDescriptor descriptor) {
		(addedConnections + diagram.connections).findFirst[domainObject == descriptor]
	}

	def <T> getNode(DomainObjectDescriptor descriptor) {
		(addedNodes + diagram.nodes).findFirst[domainObject == descriptor]
	}
	
	def boolean needsLayout() {
		isNewDiagram || addedNodes.size + addedConnections.size  > 1		
	}
	
	def getCommand() {
		AddRemoveCommand.newAddCommand(diagram, addedNodes + addedConnections)
	}
}
