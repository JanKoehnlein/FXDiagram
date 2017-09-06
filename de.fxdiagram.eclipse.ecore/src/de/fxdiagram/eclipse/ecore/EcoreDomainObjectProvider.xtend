package de.fxdiagram.eclipse.ecore

import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import java.util.Collection
import java.util.Collections
import org.eclipse.emf.common.ui.URIEditorInput
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.ENamedElement
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.edit.domain.IEditingDomainProvider
import org.eclipse.ui.IEditorPart

import static org.eclipse.ui.PlatformUI.*
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

class EcoreDomainObjectProvider implements IMappedElementDescriptorProvider {

	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<? extends T> mapping) {
		switch domainObject {
			ENamedElement:
				return new EcoreDomainObjectDescriptor(domainObject.URI.toString, domainObject.name,
					mapping.config.ID, mapping.ID) as IMappedElementDescriptor<T>
			EReferenceWithOpposite:
				return new EReferenceWithOppositeDescriptor(
					domainObject.to.URI.toString, domainObject.to.name, 
					domainObject.fro?.URI?.toString, domainObject.fro?.name,
					mapping.config.ID, mapping.ID) as IMappedElementDescriptor<T>
			ESuperType:
				return new ESuperTypeDescriptor(
					domainObject.subType.URI.toString, domainObject.subType.name,
					domainObject.superType.URI.toString, domainObject.superType.name,
					mapping.config.ID, mapping.ID) as IMappedElementDescriptor<T>
			default:
				null
		}
	}

	override <T> createDescriptor(T domainObject) {
	}

	def EObject resolveEObject(String uri) {
		val theURI = URI.createURI(uri)
		val editor = openEditor(theURI, false)
		if (editor !== null)
			editor.editingDomain.resourceSet.getEObject(theURI, true)
		else
			null
	}

	def IEditingDomainProvider openEditor(URI uri, boolean select) {
		val editorID = workbench.editorRegistry.getDefaultEditor(uri.lastSegment).id
		val editor = workbench.activeWorkbenchWindow.activePage.openEditor(new URIEditorInput(uri.trimFragment),
			editorID)
		if (editor instanceof IEditingDomainProvider) {
			if(select) {
				val element = editor.editingDomain.resourceSet.getEObject(uri, true)
				editor.selection = element
			}
			return editor
		} else {
			return null
		}
	}
	
	protected def setSelection(IEditorPart editor, EObject selectedElement) {
		try {
			val method = editor.class.getMethod('setSelectionToViewer', Collection)
			method.invoke(editor, Collections.singletonList(selectedElement))			
		} catch (Exception exc) {
			exc.printStackTrace
		}
	}
	
	override postLoad() {
	}
	
}