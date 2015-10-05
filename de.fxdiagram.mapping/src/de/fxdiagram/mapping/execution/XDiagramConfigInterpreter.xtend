package de.fxdiagram.mapping.execution

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XLabel
import de.fxdiagram.core.XNode
import de.fxdiagram.mapping.AbstractConnectionMappingCall
import de.fxdiagram.mapping.AbstractLabelMapping
import de.fxdiagram.mapping.AbstractLabelMappingCall
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.AbstractNodeMappingCall
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.ConnectionMappingCall
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.DiagramMappingCall
import de.fxdiagram.mapping.LabelMappingCall
import de.fxdiagram.mapping.MultiConnectionMappingCall
import de.fxdiagram.mapping.MultiLabelMappingCall
import de.fxdiagram.mapping.MultiNodeMappingCall
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.NodeMappingCall
import de.fxdiagram.mapping.XDiagramConfig
import java.util.HashSet
import java.util.Set

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * Executes an {@link XDiagramConfig} on a given domain object.
 */
class XDiagramConfigInterpreter {

	def <T, U> createDiagram(T diagramObject, DiagramMapping<T> diagramMapping, boolean isOnDemand,
		InterpreterContext context) {
		if (!diagramMapping.isApplicable(diagramObject))
			return null
		val descriptor = diagramObject.getDescriptor(diagramMapping)
		val diagram = diagramMapping.createDiagram(descriptor)
		val newContext = new InterpreterContext(diagram, context)
		if (isOnDemand) {
			diagram.contentsInitializer = [
				descriptor.withDomainObject [ domainObject |
					populateDiagram(diagramMapping, domainObject, newContext)
					newContext.applyChanges
					val commandStack = diagram.root.commandStack
					newContext.executeCommands(commandStack)
					null
				]
			]
		} else {
			populateDiagram(diagramMapping, diagramObject, newContext)
		}
		diagram
	}

	protected def <T> populateDiagram(DiagramMapping<T> diagramMapping, T diagramObject, InterpreterContext context) {
		diagramMapping.nodes.forEach[execute(diagramObject, context)]
		diagramMapping.connections.forEach[execute(diagramObject, [], context)]
		context.isCreateNodes = false
		if (!diagramMapping.eagerConnections.empty) {
			val eagerConnections = new HashSet(diagramMapping.eagerConnections)
			diagramMapping.nodes.forEach[connectNodesEagerly(diagramObject, eagerConnections, context)]
		}
		context.isCreateNodes = true
	}

	protected def <T> connectNodesEagerly(AbstractNodeMappingCall<?, T> it, T diagramObject,
		Set<ConnectionMapping<?>> eagerConnections, InterpreterContext context) {
		val nodeObjects = select(diagramObject)
		val nodeMappingCasted = nodeMapping as NodeMapping<Object>
		for (nodeObject : nodeObjects) {
			val descriptor = nodeObject.getDescriptor(nodeMappingCasted)
			val node = context.getNode(descriptor)
			if (node != null) {
				nodeMappingCasted.incoming.filter[onDemand && eagerConnections.contains(mapping)].forEach [
					execute(nodeObject, [target = node], context)
				]
				nodeMappingCasted.outgoing.filter[onDemand && eagerConnections.contains(mapping)].forEach [
					execute(nodeObject, [source = node], context)
				]
			}
		}
	}

	protected def <T> createLabel(T labelObject, AbstractLabelMapping<T> labelMapping) {
		if (labelMapping.isApplicable(labelObject)) {
			val descriptor = labelObject.getDescriptor(labelMapping)
			if (descriptor != null) {
				val label = labelMapping.createLabel(descriptor, labelObject)
				labelMapping.config.initialize(label)
				return label
			}
		}
		return null
	}

	def <T, U> Iterable<T> select(AbstractLabelMappingCall<T, U> labelMappingCall, U domainArgument) {
		if (domainArgument == null)
			return #[]
		if (labelMappingCall instanceof LabelMappingCall<?, ?>) {
			val labelMappingCallCasted = labelMappingCall as LabelMappingCall<T,U>
			val labelObject = (labelMappingCallCasted.selector as (Object)=>T).apply(domainArgument)
			if (labelObject == null)
				return #[]
			else
				return #[labelObject]
		} else if (labelMappingCall instanceof MultiLabelMappingCall<?, ?>) {
			val labelMappingCallCasted = labelMappingCall as MultiLabelMappingCall<T,U>
			return (labelMappingCallCasted.selector as (Object)=>Iterable<T>).apply(domainArgument).filterNull
		}
	}

	def <T, U> Iterable<? extends XLabel> execute(AbstractLabelMappingCall<T, U> labelMappingCall, U domainArgument) {
		val labelObjects = select(labelMappingCall, domainArgument)
		val result = newArrayList
		for (labelObject : labelObjects)
			result += createLabel(labelObject, labelMappingCall.labelMapping)
		return result
	}

	def <T> createNode(T nodeObject, NodeMapping<T> nodeMapping, InterpreterContext context) {
		if (nodeMapping.isApplicable(nodeObject)) {
			val descriptor = nodeObject.getDescriptor(nodeMapping)
			if (!context.isCreateDuplicateNodes) {
				val existingNode = context.getNode(descriptor)
				if (existingNode != null || !context.isCreateNodes)
					return existingNode
			}
			val node = nodeMapping.createNode(descriptor)
			nodeMapping.config.initialize(node)
			context.addNode(node)
			if (context.isCreateConnections) {
				nodeMapping.incoming.forEach [
					if (!onDemand)
						execute(nodeObject, [target = node], context)
				]
				nodeMapping.outgoing.forEach [
					if (!onDemand)
						execute(nodeObject, [source = node], context)
				]
			}
			nodeMapping.labels.forEach [
				node.labels += execute(nodeObject)
			]
			if (node instanceof XDiagramContainer)
				node.innerDiagram = nodeMapping.nestedDiagram?.execute(nodeObject, context)
			return node
		} else {
			return null
		}
	}

	def <T> createConnection(T connectionObject, ConnectionMapping<T> connectionMapping,
		(XConnection)=>void initializer, InterpreterContext context) {
		if (connectionMapping.isApplicable(connectionObject)) {
			val connectionMappingCasted = connectionMapping as ConnectionMapping<T>
			val descriptor = connectionObject.getDescriptor(connectionMappingCasted)
			val existingConnection = context.getConnection(descriptor)
			if (existingConnection != null || !context.isCreateConnections)
				return existingConnection
			val connection = connectionMappingCasted.createConnection(descriptor)
			connectionMappingCasted.config.initialize(connection)
			connectionMapping.labels.forEach [
				connection.labels += execute(connectionObject).filter(XConnectionLabel)
			]
			initializer.apply(connection)
			createEndpoints(connectionMapping, connectionObject, connection, context)
			if (connection.source != null && connection.target != null) {
				context.addConnection(connection)
				return connection
			}
		}
		return null
	}

	def <T, U> Iterable<T> select(AbstractNodeMappingCall<T, U> nodeMappingCall, U domainArgument) {
		if (domainArgument == null)
			return #[]
		if (nodeMappingCall instanceof NodeMappingCall<?, ?>) {
			val nodeMappingCallCasted = nodeMappingCall as NodeMappingCall<T,U>
			val nodeObject = (nodeMappingCallCasted.selector as (Object)=>T).apply(domainArgument)
			if (nodeObject == null)
				return #[]
			else
				return #[nodeObject]
		} else if (nodeMappingCall instanceof MultiNodeMappingCall<?, ?>) {
			val nodeMappingCallCasted = nodeMappingCall as MultiNodeMappingCall<T,U>
			return (nodeMappingCallCasted.selector as (Object)=>Iterable<T>).apply(domainArgument).filterNull
		}
	}

	def <T, U> Iterable<XNode> execute(AbstractNodeMappingCall<T, U> nodeMappingCall, U domainArgument,
		InterpreterContext context) {
		val nodeObjects = select(nodeMappingCall, domainArgument)
		val result = newArrayList
		for (nodeObject : nodeObjects)
			result += createNode(nodeObject, nodeMappingCall.nodeMapping, context)
		return result
	}

	def <T, U> Iterable<T> select(AbstractConnectionMappingCall<T, U> connectionMappingCall, U domainArgument) {
		if (domainArgument == null)
			return #[]
		if (connectionMappingCall instanceof ConnectionMappingCall<?, ?>) {
			val connectionMappingCasted = connectionMappingCall as ConnectionMappingCall<T,U>
			val connectionObject = (connectionMappingCasted.selector as (Object)=>T).apply(domainArgument)
			if (connectionObject == null)
				return #[]
			else
				return #[connectionObject]
		} else if (connectionMappingCall instanceof MultiConnectionMappingCall<?, ?>) {
			val connectionMappingCasted = connectionMappingCall as MultiConnectionMappingCall<T,U>
			return (connectionMappingCasted.selector as (Object)=>Iterable<T>).apply(domainArgument).filterNull
		}
	}

	def <T, U> Iterable<XConnection> execute(AbstractConnectionMappingCall<T, U> connectionMappingCall,
		U domainArgument, (XConnection)=>void initializer, InterpreterContext context) {
		val connectionObjects = select(connectionMappingCall, domainArgument)
		val result = newArrayList
		for (connectionObject : connectionObjects) {
			val connection = createConnection(connectionObject, connectionMappingCall.connectionMapping, initializer,
				context)
			result += connection
		}
		return result
	}

	protected def <T> createEndpoints(ConnectionMapping<T> connectionMapping, T connectionObject,
		XConnection connection, InterpreterContext context) {
		if (connection.source == null && connectionMapping.source != null)
			connection.source = connectionMapping.source?.execute(connectionObject, context).head
		if (connection.target == null && connectionMapping.target != null)
			connection.target = connectionMapping.target?.execute(connectionObject, context).head
	}

	def <T, U> XDiagram execute(DiagramMappingCall<T, U> diagramMappingCall, U domainArgument,
		InterpreterContext context) {
		val diagramObject = diagramMappingCall.selector.apply(domainArgument)
		if (diagramObject == null)
			return null
		val result = createDiagram(diagramObject, diagramMappingCall.mapping as DiagramMapping<T>,
			diagramMappingCall.isOnDemand, context)
		return result
	}

	def <T> getDescriptor(T domainObject, AbstractMapping<T> mapping) {
		mapping.config.domainObjectProvider.createMappedElementDescriptor(domainObject, mapping)
	}
}


