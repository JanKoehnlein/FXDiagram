package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XLabel
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.LabelReconcileBehavior

@ModelNode
class BaseNodeLabel<T> extends XLabel {

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