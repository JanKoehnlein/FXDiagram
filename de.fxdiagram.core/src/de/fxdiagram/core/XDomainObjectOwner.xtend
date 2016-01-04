package de.fxdiagram.core

import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.beans.property.ReadOnlyObjectProperty

interface XDomainObjectOwner {
	
	def DomainObjectDescriptor getDomainObjectDescriptor()
	
	def ReadOnlyObjectProperty<DomainObjectDescriptor> domainObjectDescriptorProperty()
	
	
}