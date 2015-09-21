package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import org.eclipse.osgi.service.resolver.BundleDescription

@ModelNode
public class BundleDescriptorProvider implements IMappedElementDescriptorProvider {
	
	override <T> createDescriptor(T domainObject) {
	}
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping) {
		switch domainObject {
			BundleDescription: {
				new BundleDescriptor(domainObject.symbolicName, domainObject.version.toString, 
					mapping.config.ID, mapping.ID, this)
					as IMappedElementDescriptor<T>
			}
			BundleDependency: {
				new BundleDependencyDescriptor(
					domainObject.kind,
					domainObject.owner.symbolicName,
					domainObject.owner.version.toString, 
					domainObject.dependency.symbolicName,
					domainObject.versionRange.toString,
					mapping.config.ID, mapping.ID,
					this) as IMappedElementDescriptor<T>
			}
			default: 
				null
		}
	}
}

