package de.fxdiagram.core.model

/**
 * Translates between {@link DomainObjectDescriptor}s and domain objects.
 * 
 * @see DomainObjectDescriptor
 */
interface DomainObjectProvider extends XModelProvider {
	
	def <T> DomainObjectDescriptor createDescriptor(T domainObject)
}

/**
 * A {@link DomainObjectProvider} that stores some application specific state,
 * that needs to be transfered to another instance, e.g. the current diagram's 
 * classloader when another diagram is loaded.
 */
interface DomainObjectProviderWithState extends DomainObjectProvider {
	def void copyState(DomainObjectProviderWithState from)	
}
