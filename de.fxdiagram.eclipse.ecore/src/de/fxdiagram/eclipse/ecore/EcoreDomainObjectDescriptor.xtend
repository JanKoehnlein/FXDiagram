package de.fxdiagram.eclipse.ecore

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.Collection
import java.util.Collections
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.ui.IEditorPart

@ModelNode('uri')
class EcoreDomainObjectDescriptor extends AbstractMappedElementDescriptor<EObject> {

	@FxProperty(readOnly = true) val String uri
	@FxProperty(readOnly = true) val String name
	
	new(String uri, String name, String mappingConfigID, String mappingID) {
		super(mappingConfigID, mappingID)
		this.uriProperty.set(uri)
		this.nameProperty.set(name)
	}
	
	override EcoreDomainObjectProvider getProvider() {
		super.provider as EcoreDomainObjectProvider
	}
	
	override <U> withDomainObject((EObject)=>U lambda) {
		val element = provider.resolveEObject(uri)
		if(element != null) 
			return lambda.apply(element)
		else
			throw new NoSuchElementException('Cannot resolve EObject ' + uri)
	}
	
	override openInEditor(boolean select) {
		val theURI = URI.createURI(uri)
		provider.openEditor(theURI, select)
	}
	
	protected def setSelection(IEditorPart editor, EObject selectedElement) {
		try {
			val method = editor.class.getMethod('setSelectionToViewer', Collection)
			method.invoke(editor, Collections.singletonList(selectedElement))			
		} catch (Exception exc) {
			exc.printStackTrace
		}
	}
	
	override equals(Object obj) {
		if(obj instanceof EcoreDomainObjectDescriptor) 
			return obj.uri == uri && obj.name == name
		else
			return false
	}
	
	override hashCode() {
		super.hashCode() + 563 * uri.hashCode + 547 * name.hashCode 
	}
	
}

