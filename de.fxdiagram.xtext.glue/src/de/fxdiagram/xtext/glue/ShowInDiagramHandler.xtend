package de.fxdiagram.xtext.glue

import com.google.inject.Inject
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import org.apache.log4j.Logger
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.jface.text.ITextSelection
import org.eclipse.ui.IWorkbench
import org.eclipse.xtext.resource.EObjectAtOffsetHelper
import org.eclipse.xtext.ui.editor.utils.EditorUtils

abstract class ShowInDiagramHandler extends AbstractHandler {

	static val LOG = Logger.getLogger(ShowInDiagramHandler)

	@Inject EObjectAtOffsetHelper eObjectAtOffsetHelper
	
	@Inject IWorkbench workbench
	
	XDiagramConfig diagramConfig
	
	override Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			val editor = EditorUtils.getActiveXtextEditor(event)
			if (editor != null) {
				val selection = editor.selectionProvider.selection as ITextSelection
				editor.document.readOnly[
					val selectedElement = eObjectAtOffsetHelper.resolveElementAt(it,
							selection.offset)
					if (selectedElement != null) {
						val mappings = getDiagramConfig.getMappings(selectedElement)
						if(!mappings.empty) {
							val view = workbench.activeWorkbenchWindow.activePage.showView("org.eclipse.xtext.glue.FXDiagramView")
							if(view instanceof FXDiagramView) {
								view.addConfig(getDiagramConfig)								
								view.revealElement(selectedElement, mappings.head(), editor)
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
	
	protected def XDiagramConfig getDiagramConfig() {
		diagramConfig ?: (diagramConfig = createDiagramConfig)
	}
	
	protected def XDiagramConfig createDiagramConfig()
}