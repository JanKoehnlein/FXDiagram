package de.fxdiagram.mapping.execution

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.model.DomainObjectDescriptor
import java.util.Set
import org.eclipse.xtend.lib.annotations.Accessors

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.command.CommandStack
import de.fxdiagram.core.command.ChangeDiagramCommand
import java.util.List
import de.fxdiagram.core.XDomainObjectShape

class InterpreterContext {

	XDiagram diagram
	
	@Accessors boolean isReplaceRootDiagram
	@Accessors boolean isCreateNodes = true
	@Accessors boolean isCreateConnections = true
	@Accessors boolean isCreateDuplicateNodes = false

	List<InterpreterContext> subContexts = newArrayList

	Set<XNode> addedNodes = newHashSet
	Set<XConnection> addedConnections = newHashSet
	
	new(XDiagram diagram) {
		this.diagram = diagram	
	}

	def getDiagram() {
		diagram
	}
	
	def addSubContext(InterpreterContext subContext) {
		subContexts += subContext 	
	}
	
	def addNode(XNode node) {
		if(diagram.root != null)
			addedNodes += node
		else if(!diagram.nodes.contains(node))
			diagram.nodes += node
	}

	def addConnection(XConnection connection) {
		if(diagram.root != null)
			addedConnections += connection
		else if(!diagram.connections.contains(connection))
			diagram.connections += connection
	}

	def <T> getConnection(DomainObjectDescriptor descriptor) {
		(addedConnections + diagram.connections).findFirst[domainObjectDescriptor == descriptor]
	}

	def <T> getNode(DomainObjectDescriptor descriptor) {
		(addedNodes + diagram.nodes).findFirst[domainObjectDescriptor == descriptor]
	}
	
	def boolean needsLayoutCommand() {
		!replaceRootDiagram && addedNodes.size + addedConnections.size  > 1		
	}
	
	def void applyChanges() {
		diagram.nodes += addedNodes
		diagram.connections += addedConnections
		subContexts.forEach[
			applyChanges
		]
	}
	
	def Iterable<XDomainObjectShape> getAddedShapes() {
		addedNodes + addedConnections + subContexts.map[addedShapes].flatten
	}
	
	def void executeCommands(CommandStack commandStack) {
		if(replaceRootDiagram && !subContexts.empty) 
			commandStack.execute(new ChangeDiagramCommand(subContexts.head.diagram))
		commandStack.execute(AddRemoveCommand.newAddCommand(diagram, addedNodes + addedConnections))
		subContexts.forEach [
			executeCommands(commandStack)
		]
	}
}
