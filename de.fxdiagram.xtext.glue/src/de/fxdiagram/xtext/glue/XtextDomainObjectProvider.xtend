package de.fxdiagram.xtext.glue

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import javafx.collections.ObservableList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.ui.shared.Access

import static javafx.collections.FXCollections.*
import de.fxdiagram.xtext.glue.mapping.AbstractMapping

@ModelNode(#['diagramConfigs'])
class XtextDomainObjectProvider implements DomainObjectProvider {

	@FxProperty @ReadOnly ObservableList<XDiagramConfig> diagramConfigs = observableArrayList
	 
	override createDescriptor(Object it) {
		if (it instanceof MappedEObjectHandle<?>) 
			return new XtextDomainObjectDescriptor(URI.toString, fullyQualifiedName, mapping, this)
		return null
	}
	
	def <T, U extends EObject> createDescriptor(T domainObject, AbstractMapping<?> mapping) {
		new MappedEObjectHandle(domainObject as U, mapping as AbstractMapping<U>).createDescriptor as XtextDomainObjectDescriptor<T>
	}
	
	def addDiagramConfig(XDiagramConfig config) {
		if(!diagramConfigs.contains(config)) 
			return diagramConfigs.add(config)
		else
			return false
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
		if (resource instanceof XtextResource) {
			val resourceServiceProvider = resource.resourceServiceProvider
			val qualifiedName = resourceServiceProvider.get(IQualifiedNameProvider)?.getFullyQualifiedName(domainObject)
			this.fqn = if(qualifiedName != null)
					resourceServiceProvider.get(IQualifiedNameConverter)?.toString(qualifiedName)
		}
	}
	
	def getURI() { uri }
	def getFullyQualifiedName() { fqn }
	def getMapping() { mapping }
}

@ModelNode(#['provider', 'uri', 'fqn', 'mapping'])
class XtextDomainObjectDescriptor<ECLASS> implements DomainObjectDescriptor {

	@FxProperty @ReadOnly XtextDomainObjectProvider provider
	@FxProperty @ReadOnly String fqn
	@FxProperty @ReadOnly String uri
	@FxProperty @ReadOnly AbstractMapping<ECLASS> mapping

	new(String uri, String fqn, AbstractMapping<ECLASS> mapping, XtextDomainObjectProvider provider) {
		uriProperty.set(uri)
		fqnProperty.set(fqn)
		providerProperty.set(provider)
		mappingProperty.set(mapping)
	}

	override getName() {
		getFqn
	}

	override getId() {
		getFqn
	}

	def <T> T withDomainObject((ECLASS)=>T lambda) {
		val uriAsURI = URI.createURI(uri)
		val editor = revealInEditor
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
	
	def revealInEditor() {
		Access.getIURIEditorOpener.get.open(URI.createURI(uri), true)
	}
}
