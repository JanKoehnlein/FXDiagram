package de.fxdiagram.xtext.glue.commands

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.ui.PlatformUI
import de.fxdiagram.xtext.glue.FXDiagramView

class ClearDiagramHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		val view = PlatformUI.workbench.activeWorkbenchWindow.activePage.findView("org.eclipse.xtext.glue.FXDiagramView")
		if(view instanceof FXDiagramView)
			view.clear()
		null
	}
}