package de.fxdiagram.eclipse.commands

import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.DirtyStateBehavior
import de.fxdiagram.eclipse.FXDiagramView
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException

import static extension org.eclipse.ui.handlers.HandlerUtil.*
import de.fxdiagram.core.command.ParallelAnimationCommand
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.command.LazyCommand
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.core.command.AnimationCommand
import de.fxdiagram.core.command.AddRemoveCommand

class UpdateDiagramHandler extends AbstractHandler {

	override execute(ExecutionEvent event) throws ExecutionException {
		val view = event.activePart
		if (view instanceof FXDiagramView) {
			val diagram = view.currentRoot.diagram;
			val allShapes = <XShape>newArrayList
			allShapes += diagram.connections
			allShapes += diagram.nodes 
			val LazyCommand lazyCommand = [
				val deleteShapes = <XShape>newHashSet
				val addShapes = <XShape>newHashSet
				val commands = <AnimationCommand>newArrayList 
				val acceptor = new UpdateAcceptor() {
					override add(XShape shape) {
						addShapes += shape
					}
					
					override delete(XShape shape) {
						deleteShapes += shape
					}
					
					override morph(AnimationCommand command) {
						commands += command
					}
				}
				allShapes.forEach[
					getBehavior(DirtyStateBehavior)?.update(acceptor)		
				]
				if(!deleteShapes.empty) 
					commands += AddRemoveCommand.newRemoveCommand(diagram, deleteShapes) 
				if(!addShapes.empty) 
					commands += AddRemoveCommand.newAddCommand(diagram, addShapes) 
				new ParallelAnimationCommand => [
					it += commands
				]		
			]
			diagram.root.commandStack.execute(lazyCommand)
		}
		null
	}
}