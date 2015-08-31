package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.XDiagramConfig
import org.eclipse.xtext.resource.IResourceServiceProvider
import com.google.inject.Injector

@ModelNode('provider', 'mappingConfigID', 'mappingID')
abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING> implements IMappedElementDescriptor<ECLASS_OR_ESETTING>, DomainObjectDescriptor {
	@FxProperty(readOnly=true) XtextDomainObjectProvider provider
	@FxProperty(readOnly=true) String mappingConfigID
	@FxProperty(readOnly=true) String mappingID
	
	AbstractMapping<ECLASS_OR_ESETTING> mapping

	new(String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		providerProperty.set(provider)
		mappingConfigIDProperty.set(mappingConfigID)
		mappingIDProperty.set(mappingID)
	}
	
	override AbstractMapping<ECLASS_OR_ESETTING> getMapping() {
		if(mapping == null) {
			val config = XDiagramConfig.Registry.instance.getConfigByID(mappingConfigID)
			mapping = config.getMappingByID(mappingID) as AbstractMapping<ECLASS_OR_ESETTING>
		}
		mapping
	}
	
	def void injectMembers(Object object) {
		resourceServiceProvider.get(Injector).injectMembers(object)
	}

	abstract protected def IResourceServiceProvider getResourceServiceProvider()
	
	override equals(Object obj) {
		if(obj instanceof AbstractXtextDescriptor<?>)
			return provider == obj.provider
				&& mappingConfigID == obj.mappingConfigID
				&& mappingID == obj.mappingID 
		else 
			return false
	}
	
	override hashCode() {
		31 * mappingConfigID.hashCode + 37 * mappingID.hashCode + 79 * provider.hashCode
	}
	
}