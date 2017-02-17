package de.fxdiagram.pde

import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import org.eclipse.osgi.service.resolver.BundleDescription

public class BundleDescriptorProvider implements IMappedElementDescriptorProvider {
	
	override <T> createDescriptor(T domainObject) {
	}
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<? extends T> mapping) {
		switch domainObject {
			BundleDescription: {
				new BundleDescriptor(domainObject.symbolicName, domainObject.version.toString, 
					mapping.config.ID, mapping.ID)
					as IMappedElementDescriptor<T>
			}
			BundleDependency: {
				new BundleDependencyDescriptor(
					domainObject.kind,
					domainObject.owner.symbolicName,
					domainObject.owner.version.toString, 
					domainObject.dependency.symbolicName,
					domainObject.versionRange.toString,
					mapping.config.ID, mapping.ID) as IMappedElementDescriptor<T>
			}
			default: 
				null
		}
	}
	
	override postLoad() {
	}
}

