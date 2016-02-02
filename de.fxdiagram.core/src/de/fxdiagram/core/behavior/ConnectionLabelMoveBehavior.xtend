package de.fxdiagram.core.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.MouseEvent
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Shape

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BezierExtensions.*
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
		val mouseInLocal = connection.sceneToLocal(sceneX, sceneY)
		switch connection.kind { 
			case POLYLINE:
				mouseInLocal.dragOnPolyline(connection)
			case QUAD_CURVE:
				mouseInLocal.dragOnSpline(connection, QuadCurve, [$0.at($1)])
			case CUBIC_CURVE:
				mouseInLocal.dragOnSpline(connection, CubicCurve, [$0.at($1)])
			default:
				super.mouseDragged(it)
		}
	}
	
	protected def dragOnPolyline(Point2D mouseInLocal, XConnection connection) {
		val controlPoints = connection.controlPoints
		if(controlPoints.size > 1) {
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
	
	protected def <T extends Shape> dragOnSpline(Point2D mouseInLocal, XConnection connection, Class<T> curveType, (T, double)=>Point2D at)  {
		val curves = (connection.node as Group).children.filter(curveType)
		var currentBestDist = Double.MAX_VALUE
		var currentBestK = -1.0 
		for(i: 0..<curves.size) {
			var left = 0.0
			var right = 1.0
			val curve = curves.get(i)
			var currentDistLeft = norm(at.apply(curve, left) - mouseInLocal)
			var currentDistRight = norm(at.apply(curve, right) - mouseInLocal)
			while (right-left > EPSILON) {
				val mid = (left + right) / 2 
			  	val distMid = norm(at.apply(curve, mid) - mouseInLocal)
			  	if(currentDistRight < currentDistLeft) {
			  		left = mid
			  		currentDistLeft = distMid
			  	} else {
			  		right = mid
			  		currentDistRight = distMid
			  	}
			}
			if(currentDistLeft < currentBestDist) {
				currentBestDist = currentDistLeft
				currentBestK = (left + i)/(curves.size)
			}			
			host.position = currentBestK
		}
	}
	
	private def toPoint2D(XControlPoint it) {
		new Point2D(layoutX, layoutY)
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