package de.fxdiagram.xtext.glue.mapping

import com.google.inject.Injector
import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.ui.PlatformUI
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.ui.shared.Access

import static extension org.eclipse.emf.common.util.URI.*
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

class XtextEObjectDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ECLASS> {
	new() {}
	
	new(String uri, String fqn, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(uri, fqn, mappingConfigID, mappingID, provider)
	}
	
	override <T> T withDomainObject((ECLASS)=>T lambda) {
		val uriAsURI = URI.createURI(uri)
		val editor = openInEditor(false)
		if (editor instanceof XtextEditor) {
			editor.document.readOnly [
				lambda.apply(resourceSet.getEObject(uriAsURI, true) as ECLASS)
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

@ModelNode('referenceURI', 'index')
class XtextESettingDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ESetting<ECLASS>> {
	
	@FxProperty(readOnly) String referenceURI
	@FxProperty(readOnly) int index
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
			editor.document.readOnly [
				val owner = resourceSet.getEObject(uriAsURI, true) as ECLASS
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

@ModelNode('provider', 'uri', 'fqn', 'mappingConfigID', 'mappingID')
@Logging
abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING> implements DomainObjectDescriptor {

	@FxProperty(readOnly) XtextDomainObjectProvider provider
	@FxProperty(readOnly) String fqn
	@FxProperty(readOnly) String uri
	@FxProperty(readOnly) String mappingConfigID
	@FxProperty(readOnly) String mappingID
	AbstractMapping<ECLASS_OR_ESETTING> mapping

	new(String uri, String fqn, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		uriProperty.set(uri)
		fqnProperty.set(fqn)
		providerProperty.set(provider)
		mappingIDProperty.set(mappingID)
		mappingConfigIDProperty.set(mappingConfigID)
	}

	override getName() {
		getFqn?.split('\\.')?.last
	}

	override getId() {
		getFqn
	}

	def AbstractMapping<ECLASS_OR_ESETTING> getMapping() {
		if(mapping == null) {
			val config = XDiagramConfig.Registry.instance.getConfigByID(mappingConfigID)
			mapping = config.getMappingByID(mappingID) as AbstractMapping<ECLASS_OR_ESETTING>
		}
		mapping
	}
	
	abstract def <T> T withDomainObject((ECLASS_OR_ESETTING)=>T lambda)
	
	def openInEditor(boolean isSelect) {
		if(isSelect) {
			Access.IURIEditorOpener.get.open(URI.createURI(uri), isSelect)
		} else {
			val activePage = PlatformUI.getWorkbench.activeWorkbenchWindow.activePage
			val activePart = activePage.activePart
			val editor = Access.IURIEditorOpener.get.open(URI.createURI(uri), isSelect)
			activePage.activate(activePart)
			return editor
		}
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

