package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly

interface DomainObjectHandle extends XModelProvider {
	def String getKey()
	def String getId()
	def Object getDomainObject()
}

@ModelNode(#['id', 'key', 'provider'])
class DomainObjectHandleImpl implements DomainObjectHandle {
	
	@FxProperty@ReadOnly DomainObjectProvider provider
	
	@FxProperty@ReadOnly String id
	
	@FxProperty@ReadOnly String key
	
	Object cachedDomainObject
	
	new(String id, String key, DomainObjectProvider provider) {
		idProperty.set(id)
		keyProperty.set(key)
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

@ModelNode(#['key', 'provider'])
class StringHandle implements DomainObjectHandle {
	
	@FxProperty@ReadOnly String key = null
	
	new(String key) {
		setKey(key)
	}
	
	def setKey(String key) {
		if(getKey() != null)
			throw new IllegalStateException("Cannot reset the key on a StringHandle")
		keyProperty.set(key)
	}
	
	override getId() {
		key
	}
	
	override getDomainObject() {
		key
	}
}
