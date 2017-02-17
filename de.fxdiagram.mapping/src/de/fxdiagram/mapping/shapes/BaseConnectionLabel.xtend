package de.fxdiagram.mapping.shapes

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.LabelReconcileBehavior
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode
class BaseConnectionLabel<T> extends XConnectionLabel {

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}
	
	override postLoad() {
		domainObjectDescriptor?.mapping?.config?.initialize(this)
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