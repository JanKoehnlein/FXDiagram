package de.fxdiagram.core.behavior

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.XConnection.Kind.*

import static extension de.fxdiagram.core.extensions.ConnectionExtensions.*

class ConnectionLabelMoveBehavior extends MoveBehavior<XConnectionLabel> {
	
	double initialPosition = -1
	
	new(XConnectionLabel label) {
		super(label)
	}
	
	override protected hasMoved() {
		if(host.connection.kind != POLYLINE) 
			super.hasMoved
		else
			initialPosition != host.position
	}
	
	override mouseDragged(MouseEvent it) {
		val connection = host.connection
		val mouseInLocal = connection.sceneToLocal(sceneX, sceneY)
		val nearestPoint = mouseInLocal.getNearestPointOnConnection(connection.controlPoints.map[toPoint2D], connection.kind)
		if(nearestPoint != null)
			host.position = nearestPoint.parameter
	}
	
	override startDrag(double screenX, double screenY) {
		initialPosition = host.position
	}
	
	override getManuallyPlaced() {
		false
	}
	
	override protected createMoveCommand() {
		new AbstractAnimationCommand {
			
			double oldPosition
			double newPosition
			
			override createExecuteAnimation(CommandContext context) {
				oldPosition = initialPosition
				newPosition = host.position
				new EmptyTransition
			}
			
			override createUndoAnimation(CommandContext context) {
				new Timeline => [
					keyFrames += new KeyFrame(context.defaultUndoDuration,
						new KeyValue(host.positionProperty, oldPosition)
					)
				]
			}
			
			override createRedoAnimation(CommandContext context) {
				new Timeline => [
					keyFrames += new KeyFrame(context.defaultUndoDuration, 
						new KeyValue(host.positionProperty, newPosition)
					)
				]
			}
		}
	}
}