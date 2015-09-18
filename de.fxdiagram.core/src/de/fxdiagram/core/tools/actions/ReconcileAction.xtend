package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.ReconcileBehavior
import de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor
import de.fxdiagram.core.command.AbstractCommand
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.command.AnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.LazyCommand
import de.fxdiagram.core.command.ParallelAnimationCommand
import de.fxdiagram.core.command.SequentialAnimationCommand
import de.fxdiagram.core.tools.actions.ReconcileAction.UpdateDirtyStateCommand
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

class ReconcileAction implements DiagramAction {
	
	override matches(KeyEvent event) {
		event.code == KeyCode.F5
	}
	
	override getSymbol() {
		SymbolType.TOOL
	}
	
	override getTooltip() {
		'Reconcile with model'
	}
	
	override perform(XRoot root) {
		val diagram = root.diagram
		val allShapes = <XDomainObjectShape>newArrayList
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
			allShapes.forEach [
				getBehavior(ReconcileBehavior)?.reconcile(acceptor)
			]
			if (!deleteShapes.empty)
				commands += AddRemoveCommand.newRemoveCommand(diagram, deleteShapes)
			if (!addShapes.empty)
				commands += AddRemoveCommand.newAddCommand(diagram, addShapes)
			new SequentialAnimationCommand => [
				it += new ParallelAnimationCommand => [
					it += commands
				]
				it += new UpdateDirtyStateCommand(allShapes)
			]
		]
		root.commandStack.execute(lazyCommand)
	}
	
	@FinalFieldsConstructor
	static class UpdateDirtyStateCommand extends AbstractCommand {

		val Iterable<XDomainObjectShape> allShapes

		override execute(CommandContext context) {
			allShapes.forEach [
				val behavior = getBehavior(ReconcileBehavior)
				if(behavior != null)
					behavior.showDirtyState(behavior.dirtyState)
			]
		}

		override undo(CommandContext context) {
			execute(context)
		}

		override redo(CommandContext context) {
			execute(context)
		}
	}
}