package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.behavior.ConnectionReconcileBehavior

/**
 * Base implementation for a {@link XConnection} that belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector. 
 */
@ModelNode
class BaseConnection<T> extends XConnection  {
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate
		addBehavior(new ConnectionReconcileBehavior(this))
	}
}