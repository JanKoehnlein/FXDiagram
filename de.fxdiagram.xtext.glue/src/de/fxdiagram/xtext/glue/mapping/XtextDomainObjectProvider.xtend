package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectProvider
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource

import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

@ModelNode
class XtextDomainObjectProvider implements DomainObjectProvider {

	override createDescriptor(Object handle) {
		if(handle instanceof MappedElement<?>) {
			switch it: handle.element {
				EObject: 
					return new XtextEObjectDescriptor(URI.toString, fullyQualifiedName, handle.mapping.config.ID, handle.mapping.ID, this)
				ESetting<?>: {
					return new XtextESettingDescriptor(owner.URI.toString, owner.fullyQualifiedName, reference, index, handle.mapping.config.ID, handle.mapping.ID, this)
				}
				default:
					return null
			}
		}
	}
	
	def <T> createMappedDescriptor(T domainObject, AbstractMapping<T> mapping) {
		 new MappedElement(domainObject, mapping).createDescriptor as AbstractXtextDescriptor<T>
	}
	
	def <T, U> createMappedDescriptor2(T domainObject, AbstractMapping<?> mapping) {
		 new MappedElement(domainObject, mapping).createDescriptor as AbstractXtextDescriptor<T>
	}
	
	def getFullyQualifiedName(EObject domainObject) {
		val resource = domainObject.eResource()
		val resourceServiceProvider = if (resource instanceof XtextResource) {
				resource.resourceServiceProvider
			} else {
				IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(resource.URI)
			}
		val qualifiedName = resourceServiceProvider?.get(IQualifiedNameProvider)?.getFullyQualifiedName(domainObject)
		if(qualifiedName != null) 
				return resourceServiceProvider.get(IQualifiedNameConverter)?.toString(qualifiedName)
			else 
				return 'unnamed'
	}
}





