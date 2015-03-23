package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XDiagram
import java.util.List

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to an {@link XDiagram}.
 * 
 * @see AbstractMapping
 */
 abstract class DiagramMapping<T> extends AbstractMapping<T> {
	
	List<AbstractNodeMappingCall<?, T>> nodes = newArrayList 
	List<AbstractConnectionMappingCall<?, T>> connections = newArrayList
	List<ConnectionMapping<?>> eagerConnections = newArrayList
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}
	
	def getNodes() { initialize ; nodes }
	def getConnections() { initialize ; connections }
	def getEagerConnections() { initialize; eagerConnections }
	
	def XDiagram createDiagram(IMappedElementDescriptor<T> descriptor) { new XDiagram }
	
	def <U> nodeFor(NodeMapping<U> nodeMapping, (T)=>U selector) {
		nodes += new NodeMappingCall(selector, nodeMapping)
	}

	def <U> connectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		connections += new ConnectionMappingCall(selector, connectionMapping)
	}

	def <U> nodeForEach(NodeMapping<U> nodeMapping, (T)=>Iterable<? extends U> selector) {
		nodes += new MultiNodeMappingCall(selector, nodeMapping)
	}

	def <U> connectionForEach(ConnectionMapping<U> connectionMapping, (T)=>Iterable<? extends U> selector) {
		connections += new MultiConnectionMappingCall(selector, connectionMapping)
	}
	
	def eagerly(ConnectionMapping<?>... connectionMapping) {
		eagerConnections += connectionMapping
	}
}




