package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.core.model.DomainObjectDescriptorImpl
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode

interface IMappedElementDescriptor<T> extends DomainObjectDescriptor {
	def AbstractMapping<T> getMapping()
	
	def <U> U withDomainObject((T)=>U lambda)
}

@ModelNode('mappingConfigID', 'mappingID')
abstract class AbstractMappedElementDescriptor<T> extends DomainObjectDescriptorImpl<T> implements IMappedElementDescriptor<T> {
	
	@FxProperty(readOnly) String mappingConfigID
	@FxProperty(readOnly) String mappingID
	AbstractMapping<T> mapping
	
	new(String id, String name, String mappingConfigID, String mappingID, IMappedElementDescriptorProvider provider) {
		super(id, name, provider)
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
}

interface IMappedElementDescriptorProvider extends DomainObjectProvider {
	def <T> IMappedElementDescriptor<T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping)
}