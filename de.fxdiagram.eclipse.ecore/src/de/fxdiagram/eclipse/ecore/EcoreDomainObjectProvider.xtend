package de.fxdiagram.eclipse.ecore

import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import org.eclipse.emf.ecore.ENamedElement
import org.eclipse.emf.ecore.util.EcoreUtil

class EcoreDomainObjectProvider implements IMappedElementDescriptorProvider {
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<? extends T> mapping) {
		switch domainObject {
			ENamedElement: new EcoreDomainObjectDescriptor(EcoreUtil.getURI(domainObject).toString, domainObject.name, 
				mapping.config.ID, mapping.ID) as IMappedElementDescriptor<T>
			default: null 
		}
	}
	
	override <T> createDescriptor(T domainObject) {
	}
}