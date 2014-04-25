package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor

class InterpreterContext {

	XDiagram diagram

	int numAddedShapes 

	new(XDiagram diagram) {
		this.diagram = diagram
	}

	def addNode(XNode node) {
		diagram.nodes += node
		node.layout
		numAddedShapes = numAddedShapes + 1
	}

	def addConnection(XConnection connection) {
		diagram.connections += connection
		numAddedShapes = numAddedShapes + 1
	}

	def <T> getConnection(DomainObjectDescriptor descriptor) {
		diagram.connections.findFirst[domainObject == descriptor]
	}

	def <T> getNode(DomainObjectDescriptor descriptor) {
		diagram.nodes.findFirst[domainObject == descriptor]
	}
	
	def boolean needsLayout() {
		numAddedShapes > 1		
	}
}