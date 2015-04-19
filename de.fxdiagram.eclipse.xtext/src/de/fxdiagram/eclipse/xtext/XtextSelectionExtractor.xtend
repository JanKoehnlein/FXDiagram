package de.fxdiagram.eclipse.xtext

import de.fxdiagram.eclipse.selection.ISelectionExtractor
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.ui.editor.XtextEditor

class XtextSelectionExtractor implements ISelectionExtractor {

	override addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor) {
		if (activePart instanceof XtextEditor) {
			val selection = activePart.selectionProvider.selection as ITextSelection
			return activePart.document.readOnly [
				val eObjectAtOffsetHelper = resourceServiceProvider.get(EObjectAtOffsetHelper)
				val selectedElement = eObjectAtOffsetHelper.resolveElementAt(it, selection.offset)
				acceptor.accept(selectedElement)
			]
		}
		return false
	}
}