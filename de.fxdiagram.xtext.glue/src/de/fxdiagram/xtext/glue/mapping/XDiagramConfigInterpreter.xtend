package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.simple.OpenableDiagramNode

class XDiagramConfigInterpreter {

	def <T> createDiagram(T diagramObject, DiagramMapping<T> diagramMapping, InterpreterContext context) {
		if (!diagramMapping.isApplicable(diagramObject))
			return null
		val diagram = diagramMapping.createDiagram(diagramObject.getDescriptor(diagramMapping))
		context.diagram = diagram
		diagramMapping.nodes.forEach[execute(diagramObject, context, true)]
		diagramMapping.connections.forEach[execute(diagramObject, [], context, true)]
		diagramMapping.nodes.forEach[connectNodesEagerly(diagramObject, context)]
		diagram
	}

	protected def <T> connectNodesEagerly(AbstractNodeMappingCall<?,T> it, T diagramObject, InterpreterContext context) {
		val nodeObjects = select(diagramObject)
		val nodeMappingCasted = nodeMapping as NodeMapping<Object>
		for(nodeObject: nodeObjects) {
			val descriptor = nodeObject.getDescriptor(nodeMappingCasted)
			val node = context.getNode(descriptor)
			if(node != null) {
				nodeMappingCasted.incoming.filter[lazy].forEach[
					execute(nodeObject, [target = node], context, false) 
				]
				nodeMappingCasted.outgoing.filter[lazy].forEach[
					execute(nodeObject, [source = node], context, false) 
				]
			}
		}
	}

	protected def <T> createNode(T nodeObject, NodeMapping<T> nodeMapping, InterpreterContext context, boolean isCreateOnDemand) {
		if (nodeMapping.isApplicable(nodeObject)) {
			val descriptor = nodeObject.getDescriptor(nodeMapping)
			val existingNode = context.getNode(descriptor)
			if (existingNode != null || !isCreateOnDemand)
				return existingNode
			val node = nodeMapping.createNode(descriptor)
			context.addNode(node)
			nodeMapping.incoming.forEach[
				if(!lazy) 
					execute(nodeObject, [target = node], context, true) 
			]
			nodeMapping.outgoing.forEach[
				if(!lazy) 
					execute(nodeObject, [source = node], context, true)
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
		InterpreterContext context, boolean isCreateNodeOnDemand) {
		val nodeObjects = select(nodeMappingCall, domainArgument)
		val result = newArrayList
		for (nodeObject : nodeObjects)
			result += createNode(nodeObject, nodeMappingCall.nodeMapping, context, isCreateNodeOnDemand)
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
		(XConnection)=>void initializer, InterpreterContext context, boolean isCreateEndpointsOnDemand) {
		val connectionObjects = select(connectionMappingCall, domainArgument)
			val result = newArrayList
		for (connectionObject : connectionObjects) {
			val connection = createConnection(connectionObject, connectionMappingCall.connectionMapping, context)
			result += connection
			initializer.apply(connection)
			createEndpoints(connectionMappingCall.connectionMapping, connectionObject, connection, context, isCreateEndpointsOnDemand)
			if(connection.source != null && connection.target != null)
				context.addConnection(connection)
		}
		return result
	}

	protected def <T> createEndpoints(ConnectionMapping<T> connectionMapping, T connectionObject, XConnection connection,
		InterpreterContext context, boolean isCreateEndpointsOnDemand) {
		if(connection.source == null && connectionMapping.source != null) 
			connection.source = connectionMapping.source?.execute(connectionObject, context, isCreateEndpointsOnDemand).head
		if(connection.target == null && connectionMapping.target != null) 
			connection.target = connectionMapping.target?.execute(connectionObject, context, isCreateEndpointsOnDemand).head
	}

	def <T,U> XDiagram execute(DiagramMappingCall<T, U> diagramMappingCall, U domainArgument,
		InterpreterContext context) {
		val diagramObject = diagramMappingCall.selector.apply(domainArgument)
		val result = createDiagram(diagramObject, diagramMappingCall.diagramMapping, new InterpreterContext)
		return result
	}

	def <T> getDescriptor(T domainObject, AbstractMapping<T> mapping) {
		 mapping.config.domainObjectProvider.createMappedElementDescriptor(domainObject, mapping)
	}
}


