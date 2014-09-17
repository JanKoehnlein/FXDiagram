package de.fxdiagram.xtext.glue.mapping

import com.google.inject.Injector
import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.ui.PlatformUI
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.ui.shared.Access

@ModelNode
class XtextDomainObjectProvider implements DomainObjectProvider {

	override createDescriptor(Object it) {
		if (it instanceof MappedEObjectHandle<?>) 
			return new XtextDomainObjectDescriptor(URI.toString, fullyQualifiedName, mapping.config.ID, mapping.ID, this)
		return null
	}
	
	def <T, U extends EObject> createDescriptor(T domainObject, AbstractMapping<?> mapping) {
		new MappedEObjectHandle(domainObject as U, mapping as AbstractMapping<U>).createDescriptor as XtextDomainObjectDescriptor<T>
	}
}

class MappedEObjectHandle<MODEL extends EObject> {
	URI uri
	String fqn
	AbstractMapping<MODEL> mapping
	
	new(MODEL domainObject, AbstractMapping<MODEL> mapping) {
		val resource = domainObject.eResource()
		this.uri = EcoreUtil.getURI(domainObject)
		this.mapping = mapping
		this.fqn = 'unnamed'
		val resourceServiceProvider = if (resource instanceof XtextResource) {
				resource.resourceServiceProvider
			} else {
				IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(resource.URI)
			}
		val qualifiedName = resourceServiceProvider?.get(IQualifiedNameProvider)?.getFullyQualifiedName(domainObject)
		this.fqn = if(qualifiedName != null)
				resourceServiceProvider.get(IQualifiedNameConverter)?.toString(qualifiedName)
	}
	
	def getURI() { uri }
	def getFullyQualifiedName() { fqn }
	def getMapping() { mapping }
}

@ModelNode('provider', 'uri', 'fqn', 'mappingConfigID', 'mappingID')
@Logging
class XtextDomainObjectDescriptor<ECLASS> implements DomainObjectDescriptor {

	@FxProperty(readOnly) XtextDomainObjectProvider provider
	@FxProperty(readOnly) String fqn
	@FxProperty(readOnly) String uri
	@FxProperty(readOnly) String mappingConfigID
	@FxProperty(readOnly) String mappingID
	AbstractMapping<ECLASS> mapping

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

	def AbstractMapping<ECLASS> getMapping() {
		if(mapping == null) {
			val config = XDiagramConfig.Registry.instance.getConfigByID(mappingConfigID)
			mapping = config.getMappingByID(mappingID) as AbstractMapping<ECLASS>
		}
		mapping
	}

	def <T> T withDomainObject((ECLASS)=>T lambda) {
		val uriAsURI = URI.createURI(uri)
		val editor = openInEditor(false)
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


