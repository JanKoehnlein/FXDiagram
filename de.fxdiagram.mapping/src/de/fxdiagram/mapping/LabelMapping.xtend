package de.fxdiagram.mapping

import de.fxdiagram.core.XLabel
import de.fxdiagram.mapping.shapes.BaseConnectionLabel
import de.fxdiagram.core.XConnectionLabel

abstract class AbstractLabelMapping<T> extends AbstractMapping<T> {
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}

	def XLabel createLabel(IMappedElementDescriptor<T> descriptor)
	
	def String getText(T element) {
		''
	}
}

class ConnectionLabelMapping<T> extends AbstractLabelMapping<T> {
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}
	
	override XConnectionLabel createLabel(IMappedElementDescriptor<T> descriptor) {
		new BaseConnectionLabel(descriptor) => [
			it.text.text = descriptor.withDomainObject[text]
		]
	}
}

class NodeLabelMapping<T> extends AbstractLabelMapping<T> {
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}
	
	override XLabel createLabel(IMappedElementDescriptor<T> descriptor) {
		new XLabel(descriptor) => [
			it.text.text = descriptor.withDomainObject[text]
		]
	}
}