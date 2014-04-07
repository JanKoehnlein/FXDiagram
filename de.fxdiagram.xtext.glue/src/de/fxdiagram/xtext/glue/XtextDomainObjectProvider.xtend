package de.fxdiagram.xtext.glue

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.ui.shared.Access

@ModelNode
class XtextDomainObjectProvider implements DomainObjectProvider {

	override createDescriptor(Object domainObject) {
		if (domainObject instanceof EObject) {
			val resource = domainObject.eResource()
			if (resource instanceof XtextResource) {
				val resourceServiceProvider = resource.resourceServiceProvider
				if (resourceServiceProvider != null) {
					val fqn = resourceServiceProvider.get(IQualifiedNameProvider)?.getFullyQualifiedName(domainObject)
					val fqnString = if(fqn != null)
							resourceServiceProvider.get(IQualifiedNameConverter)?.toString(fqn)
						else
							'unnamed'
					val uri = EcoreUtil.getURI(domainObject)?.toString
					if (fqnString != null && uri != null)
						return new XtextDomainObjectDescriptor(uri, fqnString, this)
				}
			}
		}
		return null
	}

	override resolveDomainObject(DomainObjectDescriptor descriptor) {
		throw new UnsupportedOperationException("Need a transaction to access EObjects")
	}
}

@Data
class XtextEObjectHandle<ECLASS> {

	URI uri

	def <T> T withEObject((ECLASS)=>T lambda) {
		val editor = Access.getIURIEditorOpener.get.open(getUri, true)
		if (editor instanceof XtextEditor) {
			editor.document.readOnly [
				lambda.apply(resourceSet.getEObject(getUri, true) as ECLASS)
			]
		}
	}
}

@ModelNode(#['provider', 'uri', 'fqn'])
class XtextDomainObjectDescriptor<ECLASS> implements DomainObjectDescriptor {

	@FxProperty @ReadOnly XtextDomainObjectProvider provider
	@FxProperty @ReadOnly String fqn
	@FxProperty @ReadOnly String uri

	new(String uri, String fqn, XtextDomainObjectProvider provider) {
		uriProperty.set(uri)
		fqnProperty.set(fqn)
		providerProperty.set(provider)
	}

	override getName() {
		getFqn
	}

	override getId() {
		getFqn
	}

	override getDomainObject() {
		throw new UnsupportedOperationException("Need a transaction to access EObjects")
	}
	
	def <T> T withDomainObject((ECLASS)=>T lambda) {
		val uriAsURI = URI.createURI(uri)
		val editor = Access.getIURIEditorOpener.get.open(uriAsURI, true)
		if (editor instanceof XtextEditor) {
			editor.document.readOnly [
				lambda.apply(resourceSet.getEObject(uriAsURI, true) as ECLASS)
			]
		}
	}
	
	override equals(Object obj) {
		if(obj instanceof XtextDomainObjectDescriptor<?>) 
			obj.uri == uri  
		else
			false		
	}
	
	override hashCode() {
		103 * uri.hashCode
	}
	
	def void revealInEditor() {
		Access.getIURIEditorOpener.get.open(URI.createURI(uri), true)
	}
}
