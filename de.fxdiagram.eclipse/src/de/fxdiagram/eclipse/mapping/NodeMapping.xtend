package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XNode
import de.fxdiagram.eclipse.shapes.BaseNode
import java.util.List

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to an {@link XNode}.
 * 
 * @see AbstractMapping
 */
 class NodeMapping<T> extends AbstractMapping<T> {
	
	List<AbstractConnectionMappingCall<?,T>> outgoing = newArrayList
	List<AbstractConnectionMappingCall<?,T>> incoming = newArrayList()
	DiagramMappingCall<?,T> nestedDiagram = null
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}
	
	def getOutgoing() { initialize; outgoing }
	def getIncoming() { initialize; incoming }
	def getNestedDiagram() { initialize; nestedDiagram } 
	
	def XNode createNode(IMappedElementDescriptor<T> descriptor) { new BaseNode(descriptor) }
	
	def <U> outConnectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		val call = new ConnectionMappingCall(selector, connectionMapping)
		outgoing += call
		call
	}
	
	def <U> inConnectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		val call = new ConnectionMappingCall(selector, connectionMapping)
		incoming += call
		call
	}
	
	def <U> nestedDiagramFor(DiagramMapping<U> connectionMapping, (T)=>U selector) {
		nestedDiagram = new DiagramMappingCall(selector, connectionMapping)
	}
	
	def <U> outConnectionForEach(ConnectionMapping<U> connectionMapping, (T)=>Iterable<? extends U> selector) {
		val call = new MultiConnectionMappingCall(selector, connectionMapping)
		outgoing += call
		call
	}
	
	def <U> inConnectionForEach(ConnectionMapping<U> connectionMapping, (T)=>Iterable<? extends U> selector) {
		val call = new MultiConnectionMappingCall(selector, connectionMapping)
		incoming += call
		call
	}
}