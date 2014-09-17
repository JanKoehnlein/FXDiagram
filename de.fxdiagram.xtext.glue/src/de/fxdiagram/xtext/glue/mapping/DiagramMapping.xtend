package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XDiagram
import java.util.List

abstract class DiagramMapping<T> extends AbstractMapping<T> {
	
	List<AbstractNodeMappingCall<?, T>> nodes = newArrayList 
	List<AbstractConnectionMappingCall<?, T>> connections = newArrayList()
	
	new(XDiagramConfig config, String id) {
		super(config, id)
	}
	
	def getNodes() { initialize ; nodes }
	def getConnections() { initialize ; connections }
	def XDiagram createDiagram(XtextDomainObjectDescriptor<T> descriptor) { new XDiagram }
	
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
}




