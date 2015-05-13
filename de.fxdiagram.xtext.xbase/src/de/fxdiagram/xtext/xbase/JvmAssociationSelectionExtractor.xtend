package de.fxdiagram.xtext.xbase

import de.fxdiagram.eclipse.selection.ISelectionExtractor
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.IGrammarAccess
import org.eclipse.xtext.Grammar

class JvmAssociationSelectionExtractor implements ISelectionExtractor {
	
	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		if(activePart instanceof XtextEditor) {
			val selection = activePart.selectionProvider.selection as ITextSelection
			return activePart.document.readOnly [
				try {
					// TODO: Fix when https://bugs.eclipse.org/bugs/show_bug.cgi?id=467213 is addressed
					val grammarAccess = resourceServiceProvider.get(IGrammarAccess)
					if(grammarAccess.grammar.usesXbase) {
						val associations = resourceServiceProvider.get(IJvmModelAssociations)
						val eObjectAtOffsetHelper = resourceServiceProvider.get(EObjectAtOffsetHelper)
						val selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, selection.offset)
						if (selectedElement != null) {
							val primary = associations.getPrimaryJvmElement(selectedElement)
							if(primary != null)
								return acceptor.accept(primary)
						}
					}
				} catch(Exception exc) {
					// ignore
				}
				return false
			]
		}
		return false
	}
	
	
	protected def boolean usesXbase(Grammar it) {
		return name == 'org.eclipse.xtext.xbase.Xbase' || usedGrammars.exists[usesXbase]
	}
}