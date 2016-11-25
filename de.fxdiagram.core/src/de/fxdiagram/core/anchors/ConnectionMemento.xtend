package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnection.Kind
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
import java.util.List
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.extensions.TransitionExtensions.*
import de.fxdiagram.core.behavior.MoveBehavior

@Data
@FinalFieldsConstructor
class ConnectionMemento {

	val XConnection connection
	val Kind kind
	val List<XControlPoint> controlPoints

	new(XConnection connection) {
		this.connection = connection
		this.kind = connection.kind
		this.controlPoints = newArrayList
		this.controlPoints += connection.controlPoints.map [ orig |
			new XControlPoint => [
				type = orig.type
				layoutX = orig.layoutX
				layoutY = orig.layoutY
				side = orig.side
				manuallyPlaced = orig.manuallyPlaced
			]
		]
	}

	def hasChanged() {
		if (this.kind != connection.kind)
			return true
		if (this.controlPoints.size != connection.controlPoints.size)
			return true
		for (var i = 0; i < controlPoints.size; i++) {
			val thisCP = controlPoints.get(i)
			val thatCP = connection.controlPoints.get(i)
			if (thisCP.type != thatCP.type 
				|| thisCP.layoutX != thatCP.layoutX 
				|| thisCP.layoutY != thatCP.layoutY
				|| thisCP.side != thatCP.side)
				return true
		}
		return false
	}

	def getPoints() {
		controlPoints.map [
			new Point2D(layoutX, layoutY)
		].toList
	}

	def createChangeCommand() {
		if (hasChanged)
			new MorphCommand(connection, this)
		else
			null
	}

	static class MorphCommand extends AbstractAnimationCommand {

		XConnection connection

		ConnectionMemento from
		ConnectionMemento to

		new(XConnection connection, ConnectionMemento from) {
			this.connection = connection
			this.from = from
			this.to = new ConnectionMemento(connection)
		}

		override createExecuteAnimation(CommandContext context) {
			new EmptyTransition => [
				onFinished = [
					connection.controlPoints.forEach [
						getBehavior(MoveBehavior)?.reset
					]
				]
			]
		}

		override createUndoAnimation(CommandContext context) {
			createMorphTransition(connection, to, from, context.defaultUndoDuration)
		}

		override createRedoAnimation(CommandContext context) {
			createMorphTransition(connection, from, to, context.defaultUndoDuration)
		}
	}
}
