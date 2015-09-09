package de.fxdiagram.eclipse.xtext

import de.fxdiagram.eclipse.changes.IChangeSource
import de.fxdiagram.eclipse.changes.ModelChangeBroker
import de.fxdiagram.eclipse.selection.ISelectionExtractor
import java.util.Map
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.ui.editor.XtextEditor
import org.eclipse.xtext.ui.editor.model.IXtextModelListener
import org.eclipse.swt.widgets.Display

class XtextSelectionExtractor implements ISelectionExtractor, IChangeSource {

	val Map<XtextEditor, IXtextModelListener> editor2listener= newHashMap 

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
	
	override addChangeListener(IWorkbenchPart part, ModelChangeBroker broker) {
		if (part instanceof XtextEditor) {
			val IXtextModelListener listener = [ 
				Display.^default.asyncExec [ broker.partChanged(part) ]
			]
			part.document.addModelListener(listener)
			editor2listener.put(part, listener)
		}
	}
	
	override removeChangeListener(IWorkbenchPart part, ModelChangeBroker broker) {
		if (part instanceof XtextEditor) {
			val listener = editor2listener.remove(part)
			if(listener != null)
				part.document.removeModelListener(listener)
		}
	}
	
}