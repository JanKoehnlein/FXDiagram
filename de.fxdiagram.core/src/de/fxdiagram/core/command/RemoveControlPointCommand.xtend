package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import java.util.ArrayList
import java.util.List
import java.util.Map
import java.util.Set
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.ParallelTransition
import javafx.animation.PathTransition
import javafx.animation.Timeline
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.XControlPoint.Type.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static de.fxdiagram.core.extensions.Point2DExtensions.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.ConnectionExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

/**
 * Will not remove anchor points.
 */
@FinalFieldsConstructor
class RemoveControlPointCommand extends AbstractAnimationCommand {
	
	val XConnection connection
	val List<XControlPoint> selectedPoints
	
	XConnection.Kind fromKind
	XConnection.Kind toKind
	
	List<Point2D> fromPoints
	List<Point2D> toPoints
	
	Map<Integer, XControlPoint> removedPoints
	
	protected def initialize() {
		this.fromKind = connection.kind
		this.fromPoints = newArrayList(connection.controlPoints.map[new Point2D(layoutX, layoutY)])
		val removePoints = calculateControlPointsToRemove(selectedPoints)
		val controlPoints = connection.controlPoints
		if(controlPoints.size - removePoints.size == 2) 
			toKind = POLYLINE
		else 
			toKind = connection.kind
		this.toPoints = calculateToPoints(removePoints)
		this.removedPoints = removePoints.toMap[controlPoints.indexOf(it)]
	}
	
	protected def calculateControlPointsToRemove(List<XControlPoint> selectedPoints) {
		val controlPoints = connection.controlPoints
		val removePoints = selectedPoints.toSet
		switch fromKind {
			case QUAD_CURVE:
				selectedPoints.forEach[
					val i = controlPoints.indexOf(it)
					if(i < controlPoints.size - 2)
						removePoints += controlPoints.get(i+1)
					else 
						removePoints += controlPoints.get(i-1)
				]
			case CUBIC_CURVE:
				selectedPoints.forEach[
					val i = controlPoints.indexOf(it)
					if(i < controlPoints.size - 3) {
						removePoints += controlPoints.get(i+1)
						removePoints += controlPoints.get(i+2)
					} else {
						removePoints += controlPoints.get(controlPoints.size-2)
						removePoints += controlPoints.get(controlPoints.size-3)
						removePoints += controlPoints.get(controlPoints.size-4)
					}
				]
		}
		removePoints.remove(controlPoints.head)
		removePoints.remove(controlPoints.last)
		return removePoints
	}
	
	protected def calculateToPoints(Set<XControlPoint> removePoints) {
		val controlPoints = connection.controlPoints
		val toPoints = newArrayList
		var XControlPoint lastRemaining = null
		var segmentRemoveCount = 0
		for(i: 0..<controlPoints.size) {
			val controlPoint = controlPoints.get(i)
			// exclude anchor points
			if(removePoints.contains(controlPoint)) {
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
							connection.connectionRouter.getNearestAnchor(
								connection.source, anchorRefPoint, connection.sourceArrowHead
							)
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
							connection.connectionRouter.getNearestAnchor(
								connection.target, anchorRefPoint, connection.targetArrowHead
							)
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
		return toPoints
	}
	
	protected def midPoint(XNode node) {
		node.localToRootDiagram(node.node.boundsInLocal.center)
	}
	
	override createExecuteAnimation(CommandContext context) {
		initialize
		createMorphTransition(fromPoints, toKind, toPoints, 
			emptyMap, removedPoints, context.executeDuration)
	}

	override createUndoAnimation(CommandContext context) {
		createMorphTransition(toPoints, fromKind, fromPoints, 
			removedPoints, emptyMap, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMorphTransition(fromPoints, toKind, toPoints, 
			emptyMap, removedPoints, context.defaultUndoDuration)
	}
	
	protected def createMorphTransition(
		List<Point2D> from,
		XConnection.Kind toKind, List<Point2D> to, 
		Map<Integer, XControlPoint> addBefore, 
		Map<Integer, XControlPoint> removeAfter, 
		Duration duration
	) {
		val morph = new ParallelTransition
		val label2position = newHashMap
		connection.labels.forEach [
			label2position.put(it, connection.at(position))
		] 
		connection.labels.forEach [ label |
			val startPointOnCurve = label2position.get(label)
				.getNearestPointOnConnection(from, fromKind)
			val endPointOnCurve = label2position.get(label)
				.getNearestPointOnConnection(to, toKind)
				label.position = startPointOnCurve.parameter
			morph.children += new Timeline => [
				keyFrames += new KeyFrame(0.millis, 
					new KeyValue(label.positionProperty, startPointOnCurve.parameter))
				keyFrames += new KeyFrame(duration, 
					new KeyValue(label.positionProperty, endPointOnCurve.parameter))
			]
		]
		val newControlPoints = new ArrayList(connection.controlPoints)
		addBefore.entrySet.sortBy[key].forEach[newControlPoints.add(key, value)]
		connection.controlPoints.setAll(newControlPoints)
		for(i: 1..<newControlPoints.size-1) {
			val fromPoint = from.get(i)
			val toPoint = to.get(i)
			val currentControlPoint = newControlPoints.get(i)
			if(fromPoint.distance(toPoint) > EPSILON)
				morph.children += createMoveTransition(
					currentControlPoint, fromPoint, toPoint, duration
				)
		}
		morph.onFinished = [
			connection.kind = toKind
			connection.connectionRouter.shrinkToSize(to.size)
			connection.controlPoints -= removeAfter.values
			connection.labels.forEach [
				position = label2position.get(it)
					.getNearestPointOnConnection(
						connection.controlPoints.map[toPoint2D], toKind)
					.parameter
			]
			connection.updateShapes
		]
		return morph		
	}
	
	protected def createMoveTransition(XControlPoint shape, 
		Point2D from, Point2D to, 
		Duration duration
	) {
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