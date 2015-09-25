package de.fxdiagram.eclipse.ecore

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import org.eclipse.emf.ecore.ENamedElement
import org.eclipse.emf.ecore.util.EcoreUtil

@ModelNode
class EcoreDomainObjectProvider implements IMappedElementDescriptorProvider {
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<? extends T> mapping) {
		switch domainObject {
			ENamedElement: new EcoreDomainObjectDescriptor(EcoreUtil.getURI(domainObject).toString, domainObject.name, 
				mapping.config.ID, mapping.ID, this) as IMappedElementDescriptor<T>
			default: null 
		}
	}
	
	override <T> createDescriptor(T domainObject) {
	}
}