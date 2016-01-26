package de.fxdiagram.core.behavior

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

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
		if(connection.kind != POLYLINE) {
			super.mouseDragged(it)
		} else {
			val controlPoints = connection.controlPoints
			if(controlPoints.size > 1) {
				val mouseInLocal = connection.sceneToLocal(sceneX, sceneY)
				var currentShortestDistance = Double.MAX_VALUE
				var currentSegmentIndex = -1
				var currentBestK = -1.0 
				for(i: 0..controlPoints.size-2) {
					val segStart = controlPoints.get(i).toPoint2D
					val segEnd = controlPoints.get(i+1).toPoint2D
					val segDirection = segEnd - segStart
					val segLength = segDirection.norm
					if(segLength > EPSILON) {
						val otherDirection = mouseInLocal - segStart
						val lambda = (segDirection.x * otherDirection.x + segDirection.y * otherDirection.y) 
							/ (segLength * segLength)
						val boundedLambda = min(max(0,lambda),1)
						val nearestOnSeg = linear(segStart, segEnd, boundedLambda)
						val dist = (nearestOnSeg - mouseInLocal).norm
						if(dist < currentShortestDistance) {
							currentSegmentIndex = i
							currentShortestDistance = dist
							currentBestK = (boundedLambda + i)/(controlPoints.size-1)
						}
					}
				}
				if(currentSegmentIndex != -1) {
					host.position = currentBestK
				}
			}
		}
	}
	
	private def toPoint2D(XControlPoint it) {
		new Point2D(layoutX, layoutY)
	}
	
	override startDrag(double screenX, double screenY) {
		if(host.connection.kind != POLYLINE) 
			super.startDrag(screenX, screenY)
		else 
			initialPosition = host.position
	}
	
	override getManuallyPlaced() {
		false
	}
	
	override protected createMoveCommand() {
		if(host.connection.kind != POLYLINE) 
			super.createMoveCommand
		else
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