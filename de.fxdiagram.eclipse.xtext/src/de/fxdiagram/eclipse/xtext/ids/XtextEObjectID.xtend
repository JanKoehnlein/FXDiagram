package de.fxdiagram.eclipse.xtext.ids

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource

import static extension org.eclipse.emf.common.util.URI.*
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

interface XtextEObjectID {
	def QualifiedName getQualifiedName()

	def URI getURI()

	def EClass getEClass()

	def EObject resolve(ResourceSet resourceSet)
	
	def IResourceServiceProvider getResourceServiceProvider()

	class Factory {
		def XtextEObjectID createXtextEObjectID(EObject it) {
			var currentName = qualifiedName
			if (currentName != null)
				return new DefaultXtextEObjectID(currentName, eClass, URI)
			var EObject currentParent = it
			do {
				currentParent = currentParent.eContainer
				if (currentParent == null)
					return new UnnamedXtextEObjectID(eClass, URI)
				currentName = currentParent.qualifiedName
			} while (currentName == null)
			val parentID = currentParent.createXtextEObjectID
			val fragment = URI.fragment
			val namedParentFragment = parentID.URI.fragment
			val relativeFragment = if (fragment.startsWith(namedParentFragment))
					fragment.substring(namedParentFragment.length)
				else
					'#' + fragment
			return new RelativeXtextEObjectID(parentID, eClass, URI, relativeFragment)
		}

		protected def getQualifiedName(EObject domainObject) {
			domainObject.eResource.resourceServiceProvider?.get(IQualifiedNameProvider)?.
				getFullyQualifiedName(domainObject)
		}

		protected def getResourceServiceProvider(Resource resource) {
			val resourceServiceProvider = if (resource instanceof XtextResource) {
					resource.resourceServiceProvider
				} else {
					IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(resource.URI)
				}
			resourceServiceProvider
		}
	}
}

@ModelNode('eClassURIAsString', 'uriAsString')
abstract class AbstractXtextEObjectID implements XtextEObjectID {

	EClass eClass
	URI uri

	@FxProperty(readOnly=true) String eClassURIAsString
	@FxProperty(readOnly=true) String uriAsString

	new(EClass eClass, URI elementURI) {
		this.uri = elementURI
		this.uriAsStringProperty.set(elementURI.toString)
		this.eClass = eClass
		this.eClassURIAsStringProperty.set(eClass.URI.toString)
	}

	override getURI() {
		uri ?: {
			uri = uriAsString.createURI
		}
	}

	override getEClass() {
		eClass ?: ( eClass = getEClass(EClassURIAsString) )
	}

	protected def getEClass(String uriAsString) {
		if (uriAsString == null)
			return null
		val eClassURI = uriAsString.createURI
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(eClassURI.trimFragment.toString)
		if (ePackage == null)
			throw new NoSuchElementException('Cannot find EPackage ' + eClassURI.trimFragment)
		eClass = ePackage.eResource.getEObject(eClassURI.fragment) as EClass
		if (eClass == null)
			throw new NoSuchElementException('Cannot find EClass ' + eClassURI)
		eClass
	}

	override equals(Object obj) {
		if (obj instanceof AbstractXtextEObjectID)
			return obj.URI.trimFragment == URI.trimFragment && obj.getEClass == getEClass
		else
			return false
	}

	override hashCode() {
		getURI.hashCode * 29 + getEClass.hashCode * 173
	}
	
	override def getResourceServiceProvider() {
		IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI)
	}
}
