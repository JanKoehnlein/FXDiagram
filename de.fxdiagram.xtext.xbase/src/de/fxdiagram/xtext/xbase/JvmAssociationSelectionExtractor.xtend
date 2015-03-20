package de.fxdiagram.xtext.xbase

import de.fxdiagram.eclipse.commands.ISelectionExtractor
import de.fxdiagram.eclipse.commands.ISelectionExtractor.Acceptor
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.jface.text.ITextSelection

class JvmAssociationSelectionExtractor implements ISelectionExtractor {
	
	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		if(activePart instanceof XtextEditor) {
			val selection = activePart.selectionProvider.selection as ITextSelection
			activePart.document.readOnly [
				try {
					val associations = resourceServiceProvider.get(IJvmModelAssociations)
					val eObjectAtOffsetHelper = resourceServiceProvider.get(EObjectAtOffsetHelper)
					val selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, selection.offset)
					if (selectedElement != null) {
						val primary = associations.getPrimaryJvmElement(selectedElement)
						if(primary != null)
							acceptor.accept(primary)
					}
				} catch(Exception exc) {
					// ignore
				}
				null
			]
		}
	}
	
}