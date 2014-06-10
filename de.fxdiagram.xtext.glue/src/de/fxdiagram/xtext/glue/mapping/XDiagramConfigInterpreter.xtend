package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider
import de.fxdiagram.core.XDiagram
import de.fxdiagram.lib.simple.OpenableDiagramNode

class XDiagramConfigInterpreter {

	XtextDomainObjectProvider domainObjectProvider

	new(XtextDomainObjectProvider domainObjectProvider) {
		this.domainObjectProvider = domainObjectProvider
	}
	
	def <T> createDiagram(T diagramObject, DiagramMapping<T> diagramMapping, InterpreterContext context) {
		if (!diagramMapping.isApplicable(diagramObject))
			return null
		val diagram = diagramMapping.createDiagram(diagramObject.getDescriptor(diagramMapping))
		context.diagram = diagram
		diagramMapping.nodes.forEach[execute(diagramObject, context)]
		diagramMapping.connections.forEach[execute(diagramObject, [], context)]
		diagram
	}

	protected def <T> createNode(T nodeObject, NodeMapping<T> nodeMapping, InterpreterContext context) {
		if (nodeMapping.isApplicable(nodeObject)) {
			val descriptor = nodeObject.getDescriptor(nodeMapping)
			val existingNode = context.getNode(descriptor)
			if (existingNode != null)
				return existingNode
			val node = nodeMapping.createNode(descriptor)
			context.addNode(node)
			nodeMapping.incoming.forEach[
				if(!lazy || context.isIgnoreLazy) 
					execute(nodeObject, [target = node], context) 
			]
			nodeMapping.outgoing.forEach[
				if(!lazy || context.isIgnoreLazy) 
					execute(nodeObject, [source = node], context)
			]
			if(node instanceof OpenableDiagramNode)
				node.innerDiagram = nodeMapping.nestedDiagram?.execute(nodeObject, context)
			return node
		} else {
			return null
		}
	}

	protected def <T> createConnection(T connectionObject, ConnectionMapping<T> connectionMapping, InterpreterContext context) {
		if (connectionMapping.isApplicable(connectionObject)) {
			val connectionMappingCasted = connectionMapping as ConnectionMapping<T>
			val descriptor = connectionObject.getDescriptor(connectionMappingCasted)
			val existingConnection = context.getConnection(descriptor)
			if (existingConnection != null)
				return existingConnection
			val connection = connectionMappingCasted.createConnection(descriptor)
			connection => [
				onMouseClicked = [
					if (clickCount == 2)
						descriptor.revealInEditor
				]
			]
			return connection
		} else {
			return null
		}
	}

	def <T,U> Iterable<T> select(AbstractNodeMappingCall<T, U> nodeMappingCall, U domainArgument) {
		if (nodeMappingCall instanceof NodeMappingCall<?,?>) {
			val nodeMappingCallCasted = nodeMappingCall as NodeMappingCall<T,U>
			val nodeObject = (nodeMappingCallCasted.selector as (Object)=>T).apply(domainArgument)
			return #[nodeObject]
		} else if (nodeMappingCall instanceof MultiNodeMappingCall<?,?>) {
			val nodeMappingCallCasted = nodeMappingCall as MultiNodeMappingCall<T,U>
			return (nodeMappingCallCasted.selector as (Object)=>Iterable<T>).apply(domainArgument)
		}
	}
	
	def <T,U> Iterable<XNode> execute(AbstractNodeMappingCall<T, U> nodeMappingCall, U domainArgument,
		InterpreterContext context) {
		val nodeObjects = select(nodeMappingCall, domainArgument)
		val result = newArrayList
		for (nodeObject : nodeObjects)
			result += createNode(nodeObject, nodeMappingCall.nodeMapping, context)
		return result
	}

	def <T,U> Iterable<T> select(AbstractConnectionMappingCall<T, U> connectionMappingCall, U domainArgument) {
		if (connectionMappingCall instanceof ConnectionMappingCall<?,?>) {
			val connectionMappingCasted = connectionMappingCall as ConnectionMappingCall<T,U>
			val connectionObject = (connectionMappingCasted.selector as (Object)=>T).apply(domainArgument)
			return #[connectionObject]
		} else if (connectionMappingCall instanceof MultiConnectionMappingCall<?,?>) {
			val connectionMappingCasted = connectionMappingCall as MultiConnectionMappingCall<T,U>
			return (connectionMappingCasted.selector as (Object)=>Iterable<T>).apply(domainArgument)
		}
	}

	protected def <T,U> Iterable<XConnection> execute(AbstractConnectionMappingCall<T, U> connectionMappingCall, U domainArgument,
		(XConnection)=>void initializer, InterpreterContext context) {
		val connectionObjects = select(connectionMappingCall, domainArgument)
			val result = newArrayList
		for (connectionObject : connectionObjects) {
			val connection = createConnection(connectionObject, connectionMappingCall.connectionMapping, context)
			result += connection
			initializer.apply(connection)
			createEndpoints(connectionMappingCall.connectionMapping, connectionObject, connection, context)
			context.addConnection(connection)
		}
		return result
	}

	protected def <T> createEndpoints(ConnectionMapping<T> connectionMapping, T connectionObject, XConnection connection,
		InterpreterContext context) {
		if(connection.source == null && connectionMapping.source != null) 
			connection.source = connectionMapping.source?.execute(connectionObject, context).head
		if(connection.target == null && connectionMapping.target != null) 
			connection.target = connectionMapping.target?.execute(connectionObject, context).head
	}

	def <T,U> XDiagram execute(DiagramMappingCall<T, U> diagramMappingCall, U domainArgument,
		InterpreterContext context) {
		val diagramObject = diagramMappingCall.selector.apply(domainArgument)
		val result = createDiagram(diagramObject, diagramMappingCall.diagramMapping, new InterpreterContext => [
			isIgnoreLazy = true
		])
		return result
	}

	def <T> getDescriptor(T domainObject, AbstractMapping<T> mapping) {
		domainObjectProvider.createDescriptor(domainObject, mapping)
	}
}


