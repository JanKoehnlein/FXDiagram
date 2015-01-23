package de.fxdiagram.eclipse.mapping

import de.fxdiagram.annotations.properties.ModelNode
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource

import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

@ModelNode(inherit=false)
class XtextDomainObjectProvider implements IMappedElementDescriptorProvider {

	override createDescriptor(Object handle) {
		null
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
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping) {
		switch it: domainObject {
			EObject: 
				return new XtextEObjectDescriptor(URI.toString, fullyQualifiedName, mapping.config.ID, mapping.ID, this)
					as IMappedElementDescriptor<T>
			ESetting<?>: {
				return new XtextESettingDescriptor(owner.URI.toString, owner.fullyQualifiedName, reference, index, mapping.config.ID, mapping.ID, this)
			}
			default:
				return null
		}
	}
	
}





