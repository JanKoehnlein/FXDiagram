package de.fxdiagram.eclipse.commands

import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.DirtyStateBehavior
import de.fxdiagram.eclipse.FXDiagramView
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException

import static extension org.eclipse.ui.handlers.HandlerUtil.*

class UpdateDiagramHandler extends AbstractHandler {

	override execute(ExecutionEvent event) throws ExecutionException {
		val view = event.activePart
		if (view instanceof FXDiagramView) {
			val diagram = view.currentRoot.diagram;
			val allShapes = <XShape>newArrayList
			allShapes += diagram.nodes 
			allShapes += diagram.connections
			allShapes.forEach [
				getBehavior(DirtyStateBehavior)?.dirtyState				
			]
		}
		null
	}
}