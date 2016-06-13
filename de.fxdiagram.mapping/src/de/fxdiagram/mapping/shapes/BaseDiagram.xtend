package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.DiagramReconcileBehavior
import static extension de.fxdiagram.mapping.shapes.BaseShapeInitializer.*
import de.fxdiagram.mapping.behavior.ConnectAllBehavior

@ModelNode
class BaseDiagram<T> extends XDiagram {
	
	new() {
		initializeLazily
	}
	
	new(IMappedElementDescriptor<T> domainObjectDescriptor) {
		super(domainObjectDescriptor)
	}
	
	override doActivate() {
		super.doActivate
		addBehavior(new DiagramReconcileBehavior(this))
		addBehavior(new ConnectAllBehavior(this))
	}
	
}