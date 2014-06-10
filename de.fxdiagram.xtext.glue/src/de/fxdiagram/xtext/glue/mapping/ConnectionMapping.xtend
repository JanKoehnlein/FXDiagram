package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.shapes.BaseConnection

class ConnectionMapping<T> extends AbstractMapping<T> {

	NodeMappingCall<?, T> source
	NodeMappingCall<?, T> target

	new(XDiagramConfig config, String id) {
		super(config, id)
	}

	def XConnection createConnection(XtextDomainObjectDescriptor<T> descriptor) {
		new BaseConnection(descriptor)
	}

	def getSource() { initialize ; source }

	def getTarget() { initialize ; target }

	def <U> source(NodeMapping<U> nodeMapping, (T)=>U selector) {
		source = new NodeMappingCall(selector, nodeMapping)
	}

	def <U> target(NodeMapping<U> nodeMapping, (T)=>U selector) {
		target = new NodeMappingCall(selector, nodeMapping)
	}
}
