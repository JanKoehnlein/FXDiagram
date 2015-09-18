package de.fxdiagram.mapping.shapes

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.LabelReconcileBehavior

import static extension de.fxdiagram.mapping.shapes.BaseShapeInitializer.*

class BaseConnectionLabel<T> extends XConnectionLabel {
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
		addBehavior(new LabelReconcileBehavior(this))
	}
	
	override getType() {
		domainObjectDescriptor.mapping.ID
	}
}