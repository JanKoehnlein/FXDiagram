package de.fxdiagram.eclipse.commands

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.ui.PlatformUI
import de.fxdiagram.eclipse.FXDiagramView

class ClearDiagramHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		val view = PlatformUI.workbench.activeWorkbenchWindow.activePage.findView("de.fxdiagram.eclipse.FXDiagramView")
		if(view instanceof FXDiagramView)
			view.clear()
		null
	}
}