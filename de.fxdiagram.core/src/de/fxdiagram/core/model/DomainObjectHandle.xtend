package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode

interface DomainObjectDescriptor extends XModelProvider {
	def String getName()
	def String getId()
	def Object getDomainObject()
}

@ModelNode(#['id', 'name', 'provider'])
class DomainObjectDescriptorImpl<T> implements DomainObjectDescriptor {
	
	@FxProperty /* @ReadOnly */ DomainObjectProvider provider
	
	@FxProperty /* @ReadOnly */ String id
	
	@FxProperty /* @ReadOnly */ String name
	
	T cachedDomainObject
	
	new(String id, String name, DomainObjectProvider provider) {
		idProperty.set(id)
		nameProperty.set(name)
		providerProperty.set(provider)
	}
	
	override T getDomainObject() {
		if(cachedDomainObject == null)
			cachedDomainObject = provider.resolveDomainObject(this) as T
		return cachedDomainObject
	}
	
	override equals(Object obj) {
		return class==obj.class && id == (obj as DomainObjectDescriptor).id
	}
	
}

@ModelNode(#['name'])
class StringDescriptor implements DomainObjectDescriptor {
	
	@FxProperty /* @ReadOnly */String name = null
	
	new(String name) {
		nameProperty.set(name)
	}
	
	override getId() {
		name
	}
	
	override getDomainObject() {
		name
	}
}
