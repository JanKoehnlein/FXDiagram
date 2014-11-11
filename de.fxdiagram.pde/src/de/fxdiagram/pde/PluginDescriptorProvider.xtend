package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.xtext.glue.mapping.AbstractMapping
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptorProvider
import org.eclipse.pde.core.plugin.IPluginImport
import org.eclipse.pde.core.plugin.IPluginModelBase

@ModelNode
public class PluginDescriptorProvider implements DomainObjectProvider, IMappedElementDescriptorProvider {
	
	override <T> createDescriptor(T domainObject) {
	}
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping) {
		switch domainObject {
			IPluginModelBase: {
				new PluginDescriptor(domainObject.pluginBase.id, domainObject.pluginBase.version, 
					domainObject.getResourceString(domainObject.pluginBase.name), 
					mapping.config.ID, mapping.ID, this)
					as IMappedElementDescriptor<T>
			}
			IPluginImport: {
				new PluginImportDescriptor(
					domainObject.parent.pluginBase.id,
					domainObject.parent.pluginBase.version, 
					domainObject.id,
					domainObject.version,
					mapping.config.ID, mapping.ID,
					this) as IMappedElementDescriptor<T>
			}
			default: 
				null
		}
	}
}

