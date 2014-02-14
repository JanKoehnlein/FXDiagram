package de.fxdiagram.core.model

interface DomainObjectProvider extends XModelProvider {
	
	def Object resolveDomainObject(DomainObjectDescriptor descriptor) 
	
	def DomainObjectDescriptor createDescriptor(Object domainObject)
}

interface DomainObjectProviderWithState extends DomainObjectProvider {
	def void copyState(DomainObjectProviderWithState other)	
}
