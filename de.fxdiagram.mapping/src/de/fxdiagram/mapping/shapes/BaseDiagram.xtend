package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.behavior.ConnectAllBehavior
import de.fxdiagram.mapping.reconcile.DiagramReconcileBehavior

@ModelNode
class BaseDiagram<T> extends XDiagram {
	
	new(IMappedElementDescriptor<T> domainObjectDescriptor) {
		super(domainObjectDescriptor)
	}

	override postLoad() {
		domainObjectDescriptor?.mapping?.config?.initialize(this)
	}
	
	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate
		addBehavior(new DiagramReconcileBehavior(this))
		addBehavior(new ConnectAllBehavior(this))
	}
	
}