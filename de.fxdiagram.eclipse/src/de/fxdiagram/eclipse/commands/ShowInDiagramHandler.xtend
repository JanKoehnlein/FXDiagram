package de.fxdiagram.eclipse.commands

import com.google.inject.Inject
import de.fxdiagram.eclipse.FXDiagramView
import de.fxdiagram.eclipse.mapping.XDiagramConfig
import org.apache.log4j.Logger
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbench
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.ui.editor.utils.EditorUtils

class ShowInDiagramHandler extends AbstractHandler {

	static val LOG = Logger.getLogger(ShowInDiagramHandler)

	@Inject EObjectAtOffsetHelper eObjectAtOffsetHelper
	
	@Inject IWorkbench workbench
	
	override isEnabled() {
		super.isEnabled()
	}
	
	override setEnabled(Object evaluationContext) {
		super.setEnabled(evaluationContext)
	}
	
	override Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			val editor = EditorUtils.getActiveXtextEditor(event)
			if (editor != null) {
				val selection = editor.selectionProvider.selection as ITextSelection
				editor.document.readOnly[
					val selectedElement = eObjectAtOffsetHelper.resolveElementAt(it,
							selection.offset)
					if (selectedElement != null) {
						val mappingCalls = XDiagramConfig.Registry.instance.configurations.map[getEntryCalls(selectedElement)].flatten
						if(!mappingCalls.empty) {
							val view = workbench.activeWorkbenchWindow.activePage.showView("de.fxdiagram.eclipse.FXDiagramView")
							if(view instanceof FXDiagramView) {
								view.revealElement(selectedElement, mappingCalls.head(), editor)
							} 
						}					
					}
					null
				]
			}
		} catch (Exception exc) {
			LOG.error("Error opening element in diagram", exc)
		}
		return null
	}
}
