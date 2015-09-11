package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import java.util.NoSuchElementException

/**
 * Links a domain object (some POJO) to to an {@link XNode} or an {@link XConnection}.
 * 
 * A {@link DomainObjectProvider} translates between {@link DomainObjectDescriptor}s and
 * the real domain object. The descriptor must contain all information needed to recover 
 * the domain object.
 * 
 * Subclasses should implement {@link #equals(Object)} and {@link #hashCode()} to detect
 * equality without the need of a transaction.
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
	
	/**
	 * @returns a human readable name for the associated domain object 
	 */
	def String getName()
}

/**
 * Base class for {@link DomainObjectDescriptor}s whose domain object is constant and can 
 * be cached.
 */
@ModelNode('id', 'provider')
abstract class CachedDomainObjectDescriptor<T> implements DomainObjectDescriptor {
	
	@FxProperty(readOnly=true) String id
	@FxProperty(readOnly=true) DomainObjectProvider provider

	T cachedDomainObject

	new(T domainObject, String id, DomainObjectProvider provider) {
		providerProperty.set(provider)
		idProperty.set(id)
		this.cachedDomainObject = domainObject
	}
	
	override getName() {
		id
	}
	
	override equals(Object obj) {
		return obj instanceof CachedDomainObjectDescriptor<?> 
			&& (obj as CachedDomainObjectDescriptor<?>).domainObject == getDomainObject 
	}
	
	override hashCode() {
		getDomainObject().hashCode
	}

	def getDomainObject() {
		cachedDomainObject ?: {
			cachedDomainObject = resolveDomainObject()
			if (cachedDomainObject == null)
				throw new NoSuchElementException('Element ' + id + ' not found')
			cachedDomainObject
		}
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
}
