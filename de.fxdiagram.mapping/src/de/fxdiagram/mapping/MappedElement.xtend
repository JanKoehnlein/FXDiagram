package de.fxdiagram.mapping

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider

/**
 * A {@link DomainObjectDescriptor} that describes a domain object that can be mapped 
 * to a diagram element as described by an {@link AbstractMapping}.
 * 
 * Each domain object can only appear once with the same mapping in the same diagram.
 */
interface IMappedElementDescriptor<T> extends DomainObjectDescriptor {
	def AbstractMapping<T> getMapping()
	
	def <U> U withDomainObject((T)=>U lambda)
	
	def Object openInEditor(boolean select)
}

/**
 * Base implementation of {@link IMappedElementDescriptor}.
 */
@ModelNode('provider', 'mappingConfigID', 'mappingID')
abstract class AbstractMappedElementDescriptor<T> implements IMappedElementDescriptor<T> {
	
	@FxProperty(readOnly=true) String mappingConfigID
	@FxProperty(readOnly=true) String mappingID
	@FxProperty(readOnly=true) IMappedElementDescriptorProvider provider
	
	AbstractMapping<T> mapping
	
	new(String mappingConfigID, String mappingID, IMappedElementDescriptorProvider provider) {
		this.providerProperty.set(provider)
		this.mappingConfigIDProperty.set(mappingConfigID)
		this.mappingIDProperty.set(mappingID)
	}
	
	override AbstractMapping<T> getMapping() {
		if(mapping == null) {
			val config = XDiagramConfig.Registry.instance.getConfigByID(mappingConfigID)
			mapping = config.getMappingByID(mappingID) as AbstractMapping<T>
		}
		mapping
	}
	
	override equals(Object obj) {
		if(obj instanceof AbstractMappedElementDescriptor<?>)
			return mappingConfigID == obj.mappingConfigID
				&& mappingID == obj.mappingID 
		else 
			return false
	}
	
	override hashCode() {
		31 * mappingConfigID.hashCode + 37 * mappingID.hashCode
	}
	
}

/**
 * A {@link DomainObjectProvider} that translates between domain objects with a mapping 
 * and {@link IMappedElementDescriptor}s.
 */
interface IMappedElementDescriptorProvider extends DomainObjectProvider {
	def <T> IMappedElementDescriptor<T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping)
}