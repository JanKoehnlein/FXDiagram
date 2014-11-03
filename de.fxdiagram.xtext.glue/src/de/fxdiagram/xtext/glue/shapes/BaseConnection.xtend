package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor

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