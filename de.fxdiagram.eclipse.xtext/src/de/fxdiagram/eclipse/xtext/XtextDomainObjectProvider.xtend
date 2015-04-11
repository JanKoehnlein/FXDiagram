package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource

import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
import org.eclipse.emf.common.util.URI
import java.util.Map
import org.eclipse.ui.IEditorReference
import org.eclipse.ui.PlatformUI
import org.eclipse.xtext.ui.shared.Access
import org.eclipse.ui.IEditorInput
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IWorkbenchPage
import static org.eclipse.ui.IWorkbenchPage.*

/**
 * A {@link DomainObjectProvider} for Xtext based domain objects.
 */
@ModelNode
class XtextDomainObjectProvider implements IMappedElementDescriptorProvider {

	Map<URI, CachedEditor> editorCache = newHashMap

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
	
	def getCachedEditor(URI elementURI, boolean isSelect, boolean isActivate) {
		val uri = elementURI.trimFragment
		val activePage = PlatformUI.getWorkbench.activeWorkbenchWindow.activePage
		val cachedEditor = editorCache.get(uri)?.findOn(activePage)
		if(cachedEditor != null) {
			if(isActivate) 
				cachedEditor.site.page.activate(cachedEditor)				
			return cachedEditor
		}
		val activePart = activePage.activePart
		val editor = Access.IURIEditorOpener.get.open(elementURI, isSelect)
		editorCache.put(uri, new CachedEditor(editor)) 
		if(!isActivate)
			activePage.activate(activePart)
		return editor
	}
	
	static class CachedEditor {
		IEditorInput editorInput
		String editorID
		
		new(IEditorPart editor) {
			this.editorInput = editor.editorInput
			this.editorID = editor.editorSite.id
		}
		
		def findOn(IWorkbenchPage page) {
			page
				.findEditors(editorInput, editorID, MATCH_ID + MATCH_INPUT)
				?.map[getEditor(false)]
				?.filterNull
				?.head
		}
	}
}





