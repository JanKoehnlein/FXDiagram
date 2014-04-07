package de.fxdiagram.core.model

interface DomainObjectProvider extends XModelProvider {
	
	def <T> DomainObjectDescriptor createDescriptor(T domainObject)
}

interface DomainObjectProviderWithState extends DomainObjectProvider {
	def void copyState(DomainObjectProviderWithState other)	
}
