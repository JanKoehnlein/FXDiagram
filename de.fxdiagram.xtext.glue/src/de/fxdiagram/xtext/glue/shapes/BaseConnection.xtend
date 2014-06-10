package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor

@ModelNode
class BaseConnection<T> extends XConnection  {
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			if(newVal instanceof XtextDomainObjectDescriptor<?>)
				newVal.injectMembers(this)
		]
	}
	
	new(XtextDomainObjectDescriptor<T> descriptor) {
		super(descriptor)
		descriptor.injectMembers(this)
	}

	protected def getDescriptor() {
		domainObject as XtextDomainObjectDescriptor<T>
	}
}