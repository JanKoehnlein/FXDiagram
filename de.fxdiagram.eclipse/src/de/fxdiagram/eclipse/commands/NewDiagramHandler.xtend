package de.fxdiagram.eclipse.commands

import de.fxdiagram.eclipse.FXDiagramView
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.ui.PlatformUI
import org.eclipse.core.commands.AbstractHandler

class NewDiagramHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		val view = PlatformUI.workbench.activeWorkbenchWindow.activePage.findView("de.fxdiagram.eclipse.FXDiagramView")
		if(view instanceof FXDiagramView)
			view.createNewTab()
		null
	}
}