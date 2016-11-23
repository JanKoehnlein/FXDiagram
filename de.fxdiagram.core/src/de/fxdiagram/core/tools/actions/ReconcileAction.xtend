package de.fxdiagram.core.tools.actions

import com.google.common.collect.HashMultimap
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.XNode
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
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

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
		val command = new SequentialAnimationCommand => [
			it += new UpdateDirtyStateCommand(diagram, false)
			it += getReconcileCommand(root)
			it += getReconcileCommand(root)
			it += new UpdateDirtyStateCommand(diagram, true)
		]
		root.commandStack.execute(command)
	}
	
	protected def LazyCommand getReconcileCommand(XRoot root) {
		 [
		 	val diagram = root.diagram
			val deleteShapes = HashMultimap.create
			val addShapes = HashMultimap.create
			val commands = <AnimationCommand>newArrayList
			val acceptor = new UpdateAcceptor() {
				override add(XShape shape, XDiagram diagram) {
					if(diagram != null)
						addShapes.put(diagram, shape)
				}

				override delete(XShape shape, XDiagram diagram) {
					if(diagram != null)
						deleteShapes.put(diagram, shape)
					if(shape instanceof XNode) 
						(shape.outgoingConnections + shape.incomingConnections).forEach[delete(it, it.diagram)]
					if(shape instanceof XDiagramContainer)
						(shape.innerDiagram.nodes + shape.innerDiagram.connections).forEach[delete(it, it.diagram)]
				}

				override morph(AnimationCommand command) {
					commands += command
				}
			}
			val diagramReconcileBehavior = diagram.getBehavior(ReconcileBehavior)
			if(diagramReconcileBehavior != null) 
				diagramReconcileBehavior.reconcile(acceptor)
			val allShapes = <XShape>newArrayList
			allShapes += diagram.connections
			allShapes += diagram.nodes
			allShapes -= deleteShapes.values
			allShapes.forEach [
				getBehavior(ReconcileBehavior)?.reconcile(acceptor)
			]
			deleteShapes.keySet.forEach[
				commands += AddRemoveCommand.newRemoveCommand(it, deleteShapes.get(it))
			]
			addShapes.keySet.forEach [
				commands += AddRemoveCommand.newAddCommand(it, addShapes.get(it))
			]
			return new ParallelAnimationCommand => [
				it += commands
			]
		]
	}
	
	@FinalFieldsConstructor
	static class UpdateDirtyStateCommand extends AbstractCommand {
		
		val XDiagram diagram
		
		val boolean isShow

		override execute(CommandContext context) {
			val diagramReconcileBehavior = diagram.getBehavior(ReconcileBehavior)
			diagramReconcileBehavior?.showDirtyState(diagramReconcileBehavior.dirtyState)
			val allShapes = <XDomainObjectShape>newArrayList
			allShapes += diagram.connections
			allShapes += diagram.nodes
			allShapes.forEach [
				val behavior = getBehavior(ReconcileBehavior)
				if(behavior != null) {
					if(isShow)
						behavior.showDirtyState(behavior.dirtyState)
					else
						behavior.hideDirtyState
				}
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