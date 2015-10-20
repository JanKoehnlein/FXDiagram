package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.DiagramReconcileBehavior

@ModelNode
class BaseDiagram<T> extends XDiagram {
	
	new(IMappedElementDescriptor<T> domainObjectDescriptor) {
		super(domainObjectDescriptor)
	}
	
	override doActivate() {
		super.doActivate
		addBehavior(new DiagramReconcileBehavior(this))
	}
	
}