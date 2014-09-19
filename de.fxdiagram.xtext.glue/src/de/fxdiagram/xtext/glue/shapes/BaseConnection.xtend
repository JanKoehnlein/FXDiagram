package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor

@ModelNode
class BaseConnection<T> extends XConnection  {
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			if(newVal instanceof AbstractXtextDescriptor<?>)
				newVal.injectMembers(this)
		]
	}
	
	new(AbstractXtextDescriptor<T> descriptor) {
		super(descriptor)
		descriptor.injectMembers(this)
	}

	protected def getDescriptor() {
		domainObject as AbstractXtextDescriptor<T>
	}
}