package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.NoSuchElementException

/**
 * Links a domain object (some POJO) to to an {@link XNode} or an {@link XConnection}.
 * 
 * A {@link DomainObjectProvider} translates between {@link DomainObjectDescriptor}s and
 * the real domain object. The descriptor must contain all information needed to recover 
 * the domain object.
 * 
 * This indirection serves two purposes:
 * <ol> 
 * <li>We cannot make assumptions on how a domain object can be serialized. So when we
 * store a diagram, we store the descriptors instead.</li>
 * <li>Domain objects might have different lifecycles than their associated diagram elements, 
 * i.e. it may be forbidden to store a reference to the real domain object across transaction
 * boundaries.</li>
 * </ol>
 */
interface DomainObjectDescriptor extends XModelProvider {
	def String getName()
	def String getId()
}

/**
 * Base implementation of a {@link DomainObjectDescriptor} that can be serialized and
 * uses its {@link DomainObjectProvider} to recover the domain object and execute a
 * lambda expression on it. 
 */
@ModelNode('id', 'name', 'provider')
abstract class DomainObjectDescriptorImpl<T> implements DomainObjectDescriptor {
	
	@FxProperty(readOnly=true) String id
	
	@FxProperty(readOnly=true) String name
	
	@FxProperty(readOnly=true) DomainObjectProvider provider

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
	
	/**
	 * Recover the domain object and execute the lambda expression on it.
	 * 
	 * @throws {@link NoSuchElementException} if the object cannot be recovered.
	 */
	def <U> U withDomainObject((T)=>U lambda) 
}

/**
 * Base class for {@link DomainObjectDescriptor}s whose domain object is constant and can 
 * be cached.
 */
abstract class CachedDomainObjectDescriptor<T> extends DomainObjectDescriptorImpl<T> {
	
	T cachedDomainObject

	new() {}
		
	new(T domainObject, String id, String name, DomainObjectProvider provider) {
		super(id, name, provider) 
		this.cachedDomainObject = domainObject
	}
	
	def getDomainObject() {
		cachedDomainObject ?: {
			cachedDomainObject = resolveDomainObject()
			if (cachedDomainObject == null)
				throw new NoSuchElementException('Element ' + id + ' not found')
			cachedDomainObject
		}
	}
	
	override <U> withDomainObject((T)=>U lambda) {
		lambda.apply(domainObject)
	}
	
	def T resolveDomainObject()
}

/**
 * A {@link DomainObjectDescriptor} that has a simple {@link String} as domain object.
 * Mainly used in the examples.
 */
@ModelNode('name') 
class StringDescriptor implements DomainObjectDescriptor {
	
	@FxProperty(readOnly=true) String name = null
	
	new(String name) {
		nameProperty.set(name)
	}
	
	override getId() {
		name
	}
}
