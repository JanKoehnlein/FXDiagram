package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor

@ModelNode('domainObjectDescriptor')
abstract class XDomainObjectShape extends XShape {
	
	@FxProperty(readOnly=true) DomainObjectDescriptor domainObjectDescriptor

	new(DomainObjectDescriptor descriptor) {
		domainObjectDescriptorProperty.set(descriptor)
	}	
}