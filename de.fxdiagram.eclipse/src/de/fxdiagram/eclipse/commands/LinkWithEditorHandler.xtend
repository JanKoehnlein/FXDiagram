package de.fxdiagram.eclipse.commands

import de.fxdiagram.eclipse.FXDiagramView
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException

import static extension org.eclipse.ui.handlers.HandlerUtil.*

class LinkWithEditorHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		val view = event.activePart
		if(view instanceof FXDiagramView) {
			val oldState = event.command.toggleCommandState
			view.linkWithEditor = !oldState			
		}
		null
	}
}