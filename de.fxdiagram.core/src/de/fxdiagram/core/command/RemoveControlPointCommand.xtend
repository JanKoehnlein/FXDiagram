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

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static de.fxdiagram.core.extensions.Point2DExtensions.*
import static java.lang.Math.*
import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.XControlPoint.Type.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import java.util.ArrayList

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
		this.fromKind = connection.kind
		this.fromPoints = newArrayList(connection.controlPoints.map[new Point2D(layoutX, layoutY)])
		val controlPoints = connection.controlPoints
		val reallyRemovedPoints = removeControlPoints.toSet
		switch fromKind {
			case QUAD_CURVE:
				removeControlPoints.forEach[
					val i = controlPoints.indexOf(it)
					if(i < controlPoints.size - 2)
						reallyRemovedPoints += controlPoints.get(i+1)
					else 
						reallyRemovedPoints += controlPoints.get(i-1)
				]
			case CUBIC_CURVE:
				removeControlPoints.forEach[
					val i = controlPoints.indexOf(it)
					if(i < controlPoints.size - 3) {
						reallyRemovedPoints += controlPoints.get(i+1)
						reallyRemovedPoints += controlPoints.get(i+2)
					} else {
						reallyRemovedPoints += controlPoints.get(controlPoints.size-2)
						reallyRemovedPoints += controlPoints.get(controlPoints.size-3)
						reallyRemovedPoints += controlPoints.get(controlPoints.size-4)
					}
				]
		}
		reallyRemovedPoints.remove(controlPoints.head)
		reallyRemovedPoints.remove(controlPoints.last)
		if(controlPoints.size - reallyRemovedPoints.size == 2) 
			toKind = POLYLINE
		else 
			toKind = connection.kind
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
					for(j: 1..segmentRemoveCount) {
						if(toKind == POLYLINE) {
							toPoints += linear(
								segmentStart, 
								segmentEnd, 
								j as double / (segmentRemoveCount + 1)
							)
						} else {
							if(controlPoint.type != CONTROL_POINT)
								toPoints += segmentEnd
							else
								toPoints += segmentStart
						}
					}
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
		val newControlPoints = new ArrayList(connection.controlPoints)
		addBefore.entrySet.sortBy[key].forEach[newControlPoints.add(key, value)]
		connection.controlPoints.setAll(newControlPoints)
		for(i: 1..<newControlPoints.size-1) {
			val fromPoint = from.get(min(from.size-1, i))
			val toPoint = to.get(min(to.size-1, i))
			val currentControlPoint = newControlPoints.get(i)
			if(fromPoint.distance(toPoint) > EPSILON)
				morph.children += createMoveTransition(currentControlPoint, fromPoint, toPoint, duration)
		}
		morph.onFinished = [
			connection.kind = toKind
			connection.connectionRouter.shrinkToSize(to.size)
			connection.controlPoints -= removeAfter.values
			connection.controlPoints.forEach [
				getBehavior(MoveBehavior)?.setManuallyPlaced(false)
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