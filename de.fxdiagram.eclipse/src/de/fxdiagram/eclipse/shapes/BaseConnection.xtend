package de.fxdiagram.eclipse.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import de.fxdiagram.eclipse.behavior.OpenElementInEditorBehavior
import de.fxdiagram.eclipse.mapping.AbstractXtextDescriptor
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor

/**
 * Base implementation for a {@link XConnection} that belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector. 
 */
@ModelNode
class BaseConnection<T> extends XConnection  {
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			injectMembers
		]
	}
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
		injectMembers
	}

	override getDomainObject() {
		super.getDomainObject() as IMappedElementDescriptor<T>
	}

	protected def injectMembers() {
		val descriptor = getDomainObject
		if(descriptor instanceof AbstractXtextDescriptor<?>)
			descriptor.injectMembers(this)
	}
	
	override doActivate() {
		super.doActivate()
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}