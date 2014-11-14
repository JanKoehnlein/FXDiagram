package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.eclipse.mapping.AbstractMapping
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider
import org.eclipse.pde.core.plugin.IPluginModelBase

@ModelNode
public class PluginDescriptorProvider implements DomainObjectProvider, IMappedElementDescriptorProvider {
	
	override <T> createDescriptor(T domainObject) {
	}
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping) {
		switch domainObject {
			IPluginModelBase: {
				new PluginDescriptor(domainObject.pluginBase.id, domainObject.pluginBase.version, 
					mapping.config.ID, mapping.ID, this)
					as IMappedElementDescriptor<T>
			}
			PluginDependency: {
				new PluginDependencyDescriptor(
					domainObject.owner.pluginBase.id,
					domainObject.owner.pluginBase.version, 
					domainObject.dependency.pluginBase.id,
					domainObject.versionRange.toString,
					mapping.config.ID, mapping.ID,
					this) as IMappedElementDescriptor<T>
			}
			default: 
				null
		}
	}
}

