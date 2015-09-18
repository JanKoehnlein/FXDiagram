package de.fxdiagram.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.mapping.shapes.BaseConnection
import java.util.List

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to an {@link XConnection}.
 * 
 * @see AbstractMapping
 */
class ConnectionMapping<T> extends AbstractMapping<T> {

	NodeMappingCall<?, T> source
	NodeMappingCall<?, T> target
	List<AbstractLabelMappingCall<?,T>> labels = newArrayList

	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}

	/**
	 * Called to instantiate the {@link XConnection} of this mapping. Override 
	 * if you want to change the way the {@link XConnection} should look like,
	 * e.g. add a label or arrow heads.
	 */
	def XConnection createConnection(IMappedElementDescriptor<T> descriptor) {
		new BaseConnection(descriptor)
	}

	def getSource() { initialize ; source }

	def getTarget() { initialize ; target }

	def getLabels() { initialize ; labels }

	def <U> source(NodeMapping<U> nodeMapping, (T)=>U selector) {
		source = new NodeMappingCall(selector, nodeMapping)
	}

	def <U> target(NodeMapping<U> nodeMapping, (T)=>U selector) {
		target = new NodeMappingCall(selector, nodeMapping)
	}
	
	def <U> labelFor(ConnectionLabelMapping<U> labelMapping, (T)=>U selector) {
		val call = new LabelMappingCall(selector, labelMapping)
		labels += call
		call
	}
	
	def <U> labelForEach(ConnectionLabelMapping<U> labelMapping, (T)=>Iterable<? extends U> selector) {
		val call = new MultiLabelMappingCall(selector, labelMapping)
		labels += call
		call
	}
}
