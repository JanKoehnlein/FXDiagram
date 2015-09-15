package de.fxdiagram.mapping.shapes

import de.fxdiagram.core.XLabel
import de.fxdiagram.mapping.IMappedElementDescriptor

import static extension de.fxdiagram.mapping.shapes.BaseShapeInitializer.*

class BaseNodeLabel<T> extends XLabel {
	new() {
		initializeLazily
	}

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate
	}
	
	override getType() {
		domainObjectDescriptor.mapping.ID
	}
}