package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.core.XConnection
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor

class BaseConnection<T> extends XConnection  {
	
	new(XtextDomainObjectDescriptor<T> descriptor) {
		super(descriptor)
	}

	protected def getDescriptor() {
		domainObject as XtextDomainObjectDescriptor<T>
	}
}