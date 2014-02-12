package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode

interface DomainObjectHandle extends XModelProvider {
	def String getName()
	def String getId()
	def Object getDomainObject()
}

@ModelNode(#['id', 'name', 'provider'])
class DomainObjectHandleImpl implements DomainObjectHandle {
	
	@FxProperty /* @ReadOnly */ DomainObjectProvider provider
	
	@FxProperty /* @ReadOnly */ String id
	
	@FxProperty /* @ReadOnly */ String name
	
	Object cachedDomainObject
	
	new(String id, String name, DomainObjectProvider provider) {
		idProperty.set(id)
		nameProperty.set(name)
		providerProperty.set(provider)
	}
	
	override getDomainObject() {
		if(cachedDomainObject == null)
			cachedDomainObject = provider.resolveDomainObject(this)
		return cachedDomainObject
	}
	
	override equals(Object obj) {
		return obj instanceof DomainObjectHandle && class==obj.class && id == (obj as DomainObjectHandle).id
	}
	
}

@ModelNode(#['name'])
class StringHandle implements DomainObjectHandle {
	
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
