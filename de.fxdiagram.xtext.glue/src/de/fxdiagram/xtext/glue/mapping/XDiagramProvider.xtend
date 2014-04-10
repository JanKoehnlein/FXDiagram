package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider
import java.util.List

class XDiagramProvider {

	XtextDomainObjectProvider domainObjectProvider

	new(XtextDomainObjectProvider domainObjectProvider) {
		this.domainObjectProvider = domainObjectProvider
	}

	def <T> createDiagram(T diagramObject, DiagramMapping<T> diagramMapping) {
		if (!diagramMapping.isApplicable(diagramObject))
			return null
		val diagram = diagramMapping.createDiagram.apply(diagramObject.getDescriptor(diagramMapping))
		val context = new TransformationContext(diagram)
		diagramMapping.nodes.forEach[execute(diagramObject, context)]
		diagramMapping.connections.forEach[execute(diagramObject, context)]
		diagram
	}

	def <T> createNode(T nodeObject, NodeMapping<T> nodeMapping, TransformationContext context) {
		if (nodeMapping.isApplicable(nodeObject)) {
			val descriptor = nodeObject.getDescriptor(nodeMapping)
			val existingNode = context.getNode(descriptor)
			if (existingNode != null)
				return existingNode
			val node = nodeMapping.createNode.apply(descriptor)
			node.addBehavior(new OpenElementInEditorBehavior(node))
			context.addNode(node)
			nodeMapping.incoming.forEach[
				if(lazy) 
					node.addBehavior(new LazyConnectionMappingBehavior(node, it, this, false))
				else
					execute(nodeObject, context).forEach[target = node]
			]
			nodeMapping.outgoing.forEach[
				if(lazy) 
					node.addBehavior(new LazyConnectionMappingBehavior(node, it, this, true))
				else
					execute(nodeObject, context).forEach[source = node]
			]
			return node
		} else {
			return null
		}
	}

	def <T> createConnection(T connectionObject, ConnectionMapping<T> connectionMapping, TransformationContext context) {
		if (connectionMapping.isApplicable(connectionObject)) {
			val connectionMappingCasted = connectionMapping as ConnectionMapping<T>
			val descriptor = connectionObject.getDescriptor(connectionMappingCasted)
			val existingConnection = context.getConnection(descriptor)
			if (existingConnection != null)
				return existingConnection
			val connection = connectionMappingCasted.createConnection.apply(descriptor)
			connection => [
				onMouseClicked = [
					if (clickCount == 2)
						descriptor.revealInEditor
				]
				createEndpoints(connectionMapping, connectionObject, connection, context)
				context.addConnection(connection)
			]
			return connection
		} else {
			return null
		}
	}

	def <T,U> List<T> select(AbstractNodeMappingCall<T, U> nodeMappingCall, U domainArgument) {
		if (nodeMappingCall instanceof NodeMappingCall<?,?>) {
			val nodeMappingCallCasted = nodeMappingCall as NodeMappingCall<T,U>
			val nodeObject = (nodeMappingCallCasted.selector as (Object)=>T).apply(domainArgument)
			return #[nodeObject]
		} else if (nodeMappingCall instanceof MultiNodeMappingCall<?,?>) {
			val nodeMappingCallCasted = nodeMappingCall as MultiNodeMappingCall<T,U>
			return (nodeMappingCallCasted.selector as (Object)=>List<T>).apply(domainArgument)
		}
	}
	
	protected def <T,U> List<XNode> execute(AbstractNodeMappingCall<T, U> nodeMappingCall, U domainArgument,
		TransformationContext context) {
		val nodeObjects = select(nodeMappingCall, domainArgument)
		val result = newArrayList
		for (nodeObject : nodeObjects)
			result += createNode(nodeObject, nodeMappingCall.nodeMapping, context)
		return result
	}

	def <T,U> List<T> select(AbstractConnectionMappingCall<T, U> connectionMappingCall, U domainArgument) {
		if (connectionMappingCall instanceof ConnectionMappingCall<?,?>) {
			val connectionMappingCasted = connectionMappingCall as ConnectionMappingCall<T,U>
			val connectionObject = (connectionMappingCasted.selector as (Object)=>T).apply(domainArgument)
			return #[connectionObject]
		} else if (connectionMappingCall instanceof MultiConnectionMappingCall<?,?>) {
			val connectionMappingCasted = connectionMappingCall as MultiConnectionMappingCall<T,U>
			return (connectionMappingCasted.selector as (Object)=>List<T>).apply(domainArgument)
		}
	}

	protected def <T,U> List<XConnection> execute(AbstractConnectionMappingCall<T, U> connectionMappingCall, U domainArgument,
		TransformationContext context) {
		val connectionObjects = select(connectionMappingCall, domainArgument)
			val result = newArrayList
		for (connectionObject : connectionObjects) {
			val connection = createConnection(connectionObject, connectionMappingCall.connectionMapping, context)
			result += connection
			createEndpoints(connectionMappingCall.connectionMapping, connectionObject, connection, context)
		}
		return result
	}

	protected def <T> createEndpoints(ConnectionMapping<T> connectionMapping, T connectionObject, XConnection connection,
		TransformationContext context) {
		if(connection.source == null && connectionMapping.source != null) 
			connection.source = connectionMapping.source?.execute(connectionObject, context).head
		if(connection.target == null && connectionMapping.target != null) 
			connection.target = connectionMapping.target?.execute(connectionObject, context).head
	}

	def <T> getDescriptor(T domainObject, AbstractMapping<T> mapping) {
		domainObjectProvider.createDescriptor(domainObject, mapping)
	}
}

class TransformationContext {

	XDiagram diagram

	new(XDiagram diagram) {
		this.diagram = diagram
	}

	def addNode(XNode node) {
		diagram.nodes += node
	}

	def addConnection(XConnection connection) {
		diagram.connections += connection
	}

	def <T> getConnection(DomainObjectDescriptor descriptor) {
		diagram.connections.findFirst[domainObject == descriptor]
	}

	def <T> getNode(DomainObjectDescriptor descriptor) {
		diagram.nodes.findFirst[domainObject == descriptor]
	}
}
