package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.IMappedElementDescriptorProvider
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.ui.IEditorInput
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IWorkbenchPage
import org.eclipse.ui.PlatformUI
import org.eclipse.xtext.resource.ILocationInFileProvider
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.ui.shared.Access

import static org.eclipse.ui.IWorkbenchPage.*

/**
 * A {@link DomainObjectProvider} for Xtext based domain objects.
 */
@ModelNode
class XtextDomainObjectProvider implements IMappedElementDescriptorProvider {

	Map<URI, CachedEditor> editorCache = newHashMap

	val idFactory = new XtextEObjectID.Factory 

	override createDescriptor(Object handle) {
		null
	}
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping) {
		switch it: domainObject {
			EObject: {
				return new XtextEObjectDescriptor(createXtextEObjectID, mapping.config.ID, mapping.ID, this) as IMappedElementDescriptor<T>
			}
			ESetting<?>: {
				return new XtextESettingDescriptor(owner.createXtextEObjectID, target.createXtextEObjectID, reference, index, mapping.config.ID, mapping.ID, this)
			}
			default:
				return null
		}
	}
	
	def createXtextEObjectID(EObject element) {
		idFactory.createXtextEObjectID(element)
	}
	
	/**
	 * Avoids expensive switching of active parts on subsequent withDomainObject operations. 
	 */	
	def getCachedEditor(XtextEObjectID elementID, boolean isSelect, boolean isActivate) {
		val activePage = PlatformUI.getWorkbench.activeWorkbenchWindow.activePage
		val resourceURI = elementID.URI.trimFragment
		val cachedEditor = editorCache.get(resourceURI)?.findOn(activePage)
		if(cachedEditor instanceof XtextEditor) {
			if(isActivate) {
				cachedEditor.site.page.activate(cachedEditor)
				if(isSelect) {
					cachedEditor.document.readOnly [
						val eObject = elementID.resolve(resourceSet)
						val locationInFileProvider = resourceServiceProvider.get(ILocationInFileProvider)
						val textRegion = locationInFileProvider.getSignificantTextRegion(eObject)
						if(textRegion != null) 
							cachedEditor.selectAndReveal(textRegion.offset, textRegion.length)
						null
					]
				}				
			}
			return cachedEditor
		}
		val activePart = activePage.activePart
		val editor = Access.IURIEditorOpener.get.open(resourceURI, isSelect)
		editorCache.put(resourceURI, new CachedEditor(editor)) 
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





