package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.List
import java.util.Map
import javafx.animation.ParallelTransition
import javafx.animation.PathTransition
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

class RemoveControlPointCommand extends AbstractAnimationCommand {
	
	val XConnection connection
	
	val XConnection.Kind fromKind
	val XConnection.Kind toKind
	
	val List<Point2D> fromPoints
	val List<Point2D> toPoints
	
	val Map<Integer, XControlPoint> removedPoints
	
	/**
	 * Removing anchor points is ignored
	 */
	new(XConnection connection, List<XControlPoint> removeControlPoints) {
		this.connection = connection
		this.toKind = POLYLINE
		this.fromKind = connection.kind
		this.fromPoints = newArrayList(connection.controlPoints.map[new Point2D(layoutX, layoutY)])
		
		val controlPoints = connection.controlPoints
		val reallyRemovedPoints = removeControlPoints.toSet
		reallyRemovedPoints.remove(controlPoints.head)
		reallyRemovedPoints.remove(controlPoints.last)
		toPoints = newArrayList
		var XControlPoint lastRemaining = null
		var segmentRemoveCount = 0
		for(i: 0..<controlPoints.size) {
			val controlPoint = controlPoints.get(i)
			// exclude anchor points
			if(reallyRemovedPoints.contains(controlPoint)) {
				segmentRemoveCount++
			} else {
				if(segmentRemoveCount > 0) {
					val hasRemainingControlPoints = 
						lastRemaining != controlPoints.head
					 	|| controlPoint != controlPoints.last
					val segmentStart = 
						if(lastRemaining == controlPoints.head) {
							val anchorRefPoint = 
								if(hasRemainingControlPoints)
									new Point2D(controlPoint.layoutX, controlPoint.layoutY)
								else 
									connection.target.midPoint
							connection.connectionRouter.getNearestAnchor(connection.source, anchorRefPoint, connection.sourceArrowHead)
						} else {
							new Point2D(lastRemaining.layoutX, lastRemaining.layoutY)
						}
					val segmentEnd = 
						if(controlPoint == controlPoints.last) {
							val anchorRefPoint = 
								if(hasRemainingControlPoints)
									new Point2D(lastRemaining.layoutX, lastRemaining.layoutY)
								else 
									connection.source.midPoint
							connection.connectionRouter.getNearestAnchor(connection.target, anchorRefPoint, connection.targetArrowHead)
						} else {
							new Point2D(controlPoint.layoutX, controlPoint.layoutY)
						}
					for(j: 1..segmentRemoveCount) 
						toPoints += linear(
							segmentStart, 
							segmentEnd, 
							j as double / (segmentRemoveCount + 1)
						)
					segmentRemoveCount = 0
				} 
				lastRemaining = controlPoint
				toPoints += new Point2D(controlPoint.layoutX, controlPoint.layoutY)	
			}
		}
		this.removedPoints = reallyRemovedPoints.toMap[controlPoints.indexOf(it)]
	}
	
	protected static def midPoint(XNode node) {
		node.localToRootDiagram(node.node.boundsInLocal.center)
	}
	
	override createExecuteAnimation(CommandContext context) {
		createMorphTransition(fromPoints, toKind, toPoints, emptyMap, removedPoints, context.executeDuration)
	}

	override createUndoAnimation(CommandContext context) {
		createMorphTransition(toPoints, fromKind, fromPoints, removedPoints, emptyMap, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMorphTransition(fromPoints, toKind, toPoints, emptyMap, removedPoints, context.defaultUndoDuration)
	}
	
	def createMorphTransition(
		List<Point2D> from,
		XConnection.Kind toKind, List<Point2D> to, 
		Map<Integer, XControlPoint> addBefore, 
		Map<Integer, XControlPoint> removeAfter, 
		Duration duration
	) {
		val morph = new ParallelTransition
		connection.kind = toKind
		addBefore.entrySet.sortBy[key].forEach[connection.controlPoints.add(key, value)]
		val controlPoints = connection.controlPoints
		for(i: 1..<controlPoints.size-1) {
			val fromPoint = from.get(min(from.size-1, i))
			val toPoint = to.get(min(to.size-1, i))
			val currentControlPoint = controlPoints.get(i)
			if(fromPoint.distance(toPoint) > EPSILON)
				morph.children += createMoveTransition(currentControlPoint, fromPoint, toPoint, duration)
		}
		morph.onFinished = [
			connection.connectionRouter.shrinkToSize(to.size)
			connection.controlPoints -= removeAfter.values
			connection.controlPoints.forEach [
				getBehavior(MoveBehavior)?.setIsManuallyPlaced(false)
			]
			connection.updateShapes
		]
		return morph		
	}
	
	protected def createMoveTransition(XControlPoint shape, Point2D from, Point2D to, Duration duration) {
		val dummyNode = new Group => [
			translateX = from.x
			translateY = from.y
		]
		shape.layoutXProperty.bind(dummyNode.translateXProperty)
		shape.layoutYProperty.bind(dummyNode.translateYProperty)
		new PathTransition => [
			it.node = dummyNode
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				elements += new MoveTo(from.x, from.y)
				elements += new LineTo(to.x, to.y)
			]
			onFinished = [
				shape => [
					layoutXProperty.unbind
					layoutYProperty.unbind
					layoutX = to.x
					layoutY = to.y
				]
			]
		]
	}
}