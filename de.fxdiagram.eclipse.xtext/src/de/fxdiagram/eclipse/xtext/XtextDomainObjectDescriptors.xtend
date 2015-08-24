package de.fxdiagram.eclipse.xtext

import com.google.inject.Injector
import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.XtextEditor

import static extension org.eclipse.emf.common.util.URI.*
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
import java.util.Collection

/**
 * A {@link DomainObjectDescriptor} that points to an {@link EObject} from an Xtext document.
 * 
 * Xtext objects only exist in a read-only transaction on an Xtext editor's document.
 * They can be recovered from their URI.
 * The descriptor also allows to use the {@link Injector} of the langage the domain object 
 * belongs to.
 */
class XtextEObjectDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ECLASS> {
	new() {}
	
	new(String uri, String fqn, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(uri, fqn, mappingConfigID, mappingID, provider)
	}
	
	override <T> T withDomainObject((ECLASS)=>T lambda) {
		val uriAsURI = URI.createURI(uri)
		val editor = openInEditor(false)
		if (editor instanceof XtextEditor) {
			editor.document.readOnly [ XtextResource it |
				val domainObject = resourceSet.getEObject(uriAsURI, true)
				if(domainObject == null)
					throw new NoSuchElementException('Xtext element ' + fqn + ' does not exist')
				lambda.apply(domainObject as ECLASS)
			]
		}
	}
	
	override equals(Object obj) {
		if(obj instanceof XtextEObjectDescriptor<?>) 
			obj.uri == uri  
		else
			false		
	}
	
	override hashCode() {
		103 * uri.hashCode
	}
}

/**
 * A {@link DomainObjectDescriptor} that points to an {@link ESetting} from an Xtext document.
 * 
 * Xtext objects only exist in a read-only transaction on an Xtext editor's document.
 * They can be recovered from their URI.
 */

@ModelNode('referenceURI', 'index')
class XtextESettingDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ESetting<ECLASS>> {
	
	@FxProperty(readOnly=true) String referenceURI
	@FxProperty(readOnly=true) int index
	EReference reference
	
	new() {}
	
	new(String uri, String fqn, EReference reference, int index, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(uri, fqn, mappingConfigID, mappingID, provider)
		this.reference = reference
		this.referenceURIProperty.set(reference.URI.toString) 
		this.indexProperty.set(index)
	}
	
	def getReference() {
		reference ?: (reference = resolveReference) 
	}
	
	private def resolveReference() {
		val uri = referenceURI.createURI
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val reference = ePackage?.eResource?.getEObject(uri.fragment)
		if(reference instanceof EReference)
			return reference 
		else
			throw new IllegalArgumentException('Cannot resolve EReference ' + referenceURI)
	}
	
	override <T> T withDomainObject((ESetting<ECLASS>)=>T lambda) {
		val uriAsURI = URI.createURI(uri)
		val editor = openInEditor(false)
		if (editor instanceof XtextEditor) {
			editor.document.readOnly [ XtextResource it |
				val owner = resourceSet.getEObject(uriAsURI, true) as ECLASS
				if(owner == null) 
					throw new NoSuchElementException('EReference owner ' + uri + ' not found')
				if(!owner.eIsSet(reference) || reference.isMany && (owner.eGet(reference) as Collection<?>).size < index)
					throw new NoSuchElementException('Referenced element ' + uri + ' not found')
				val setting = new ESetting(owner, reference, index)
				lambda.apply(setting)
			]
		}
	}
	
	override equals(Object obj) {
		if(obj instanceof XtextESettingDescriptor<?>) 
			obj.uri == uri && getReference == obj.getReference && index == obj.index  
		else
			false		
	}
	
	override hashCode() {
		103 * uri.hashCode + 37 * getReference.hashCode + 11 * index
	}
}

@Logging
abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING> extends AbstractMappedElementDescriptor<ECLASS_OR_ESETTING> {

	new() {}
	
	new(String uri, String fqn, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(uri, fqn, mappingConfigID, mappingID, provider)
	}

	override getName() {
		nameProperty.get?.split('\\.')?.last
	}
	
	def String getFqn() {
		nameProperty.get
	}

	def getUri() {
		id
	}

	override openInEditor(boolean isSelect) {
		(provider as XtextDomainObjectProvider).getCachedEditor(URI.createURI(uri), isSelect, isSelect)
	}
	
	def injectMembers(Object it) {
		val resourceServiceProvider = getResourceServiceProvider()
		if(resourceServiceProvider == null) 
			LOG.severe('Cannot find IResourceServiceProvider for ' + uri)
		else
			resourceServiceProvider.get(Injector).injectMembers(it)
	}
	
	protected def getResourceServiceProvider() {
		IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI(uri))
	}
}

