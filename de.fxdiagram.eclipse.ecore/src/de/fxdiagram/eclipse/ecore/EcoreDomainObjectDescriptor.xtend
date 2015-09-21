package de.fxdiagram.eclipse.ecore

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.Collection
import java.util.NoSuchElementException
import org.eclipse.emf.common.ui.URIEditorInput
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.edit.domain.IEditingDomainProvider
import org.eclipse.ui.IEditorPart

import static org.eclipse.ui.PlatformUI.*
import java.util.Collections

@ModelNode('uri')
class EcoreDomainObjectDescriptor extends AbstractMappedElementDescriptor<EObject> {

	@FxProperty(readOnly = true) val String uri
	@FxProperty(readOnly = true) val String name
	
	new(String uri, String name, String mappingConfigID, String mappingID, EcoreDomainObjectProvider provider) {
		super(mappingConfigID, mappingID, provider)
		this.uriProperty.set(uri)
		this.nameProperty.set(name)
	}
	
	override <U> withDomainObject((EObject)=>U lambda) {
		val theURI = URI.createURI(uri)
		val editorID = workbench.editorRegistry.getDefaultEditor(theURI.lastSegment).id
		val editor = workbench.activeWorkbenchWindow.activePage.openEditor(new URIEditorInput(theURI.trimFragment), editorID)
		if(editor instanceof IEditingDomainProvider) {
			val element = editor.editingDomain.resourceSet.getEObject(theURI, true)
			if(element instanceof EObject) 
				return lambda.apply(element)
		}
		throw new NoSuchElementException('Cannot resolve EObject ' + uri)
	}
	
	override openInEditor(boolean select) {
		val theURI = URI.createURI(uri)
		val editorID = workbench.editorRegistry.getDefaultEditor(theURI.lastSegment).id
		val editor = workbench.activeWorkbenchWindow.activePage.openEditor(new URIEditorInput(theURI.trimFragment), editorID)
		if(editor instanceof IEditingDomainProvider) {
			val element = editor.editingDomain.resourceSet.getEObject(theURI, true)
			editor.setSelection(element)
			return editor
		}
		return null
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