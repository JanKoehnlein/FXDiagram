package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly

interface DomainObjectDescriptor extends XModelProvider {
	def String getName()
	def String getId()
}

@ModelNode(#['id', 'name', 'provider'])
abstract class DomainObjectDescriptorImpl<T> implements DomainObjectDescriptor {
	
	@FxProperty @ReadOnly String id
	
	@FxProperty @ReadOnly String name
	
	@FxProperty @ReadOnly DomainObjectProvider provider

	new() {}
	
	new(String id, String name, DomainObjectProvider provider) {
		idProperty.set(id)
		nameProperty.set(name)
		providerProperty.set(provider)
	}
	
	override equals(Object obj) {
		return obj != null && class==obj.class && id.equals((obj as DomainObjectDescriptor).id)
	}
	
	override hashCode() {
		id.hashCode
	}
	
	def <U> U withDomainObject((T)=>U lambda)
}

abstract class CachedDomainObjectDescriptor<T> extends DomainObjectDescriptorImpl<T> {
	
	T cachedDomainObject

	new() {}
		
	new(T domainObject, String id, String name, DomainObjectProvider provider) {
		super(id, name, provider) 
		this.cachedDomainObject = domainObject
	}
	
	def getDomainObject() {
		cachedDomainObject ?: (cachedDomainObject = resolveDomainObject())
	}
	
	override <U> withDomainObject((T)=>U lambda) {
		lambda.apply(domainObject)
	}
	
	def T resolveDomainObject()
}

@ModelNode(#['name']) 
class StringDescriptor implements DomainObjectDescriptor {
	
	@FxProperty @ReadOnly String name = null
	
	new(String name) {
		nameProperty.set(name)
	}
	
	override getId() {
		name
	}
}
