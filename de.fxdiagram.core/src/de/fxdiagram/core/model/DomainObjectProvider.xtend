package de.fxdiagram.core.model

interface DomainObjectProvider extends XModelProvider {
	
	def Object resolveDomainObject(DomainObjectHandle handle) 
	
	def DomainObjectHandle createDomainObjectHandle(Object object)
	
}

