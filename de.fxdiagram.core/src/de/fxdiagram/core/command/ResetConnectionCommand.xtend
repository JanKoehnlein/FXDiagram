package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.anchors.ConnectionMemento
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.extensions.TransitionExtensions.*

/**
 * Will not remove anchor points.
 */
@FinalFieldsConstructor
class ResetConnectionCommand extends AbstractAnimationCommand {
	
	val XConnection connection
	
	ConnectionMemento from
	ConnectionMemento to
	
	protected def createToMemento() {
		switch connection.kind {
			case RECTILINEAR: {
				val newPoints = newArrayList
				newPoints += connection.connectionRouter.manhattanRouter.defaultPoints
				return new ConnectionMemento(connection, connection.kind, newPoints)
			}
			default: 
				throw new UnsupportedOperationException('Implementation missing')
		}
	}
	
	override createExecuteAnimation(CommandContext context) {
		from = new ConnectionMemento(connection)
		to = createToMemento
		createMorphTransition(connection, from, to, context.executeDuration)
	}

	override createUndoAnimation(CommandContext context) {
		createMorphTransition(connection, to, from, context.executeDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMorphTransition(connection, from, to, context.executeDuration)
	}
}