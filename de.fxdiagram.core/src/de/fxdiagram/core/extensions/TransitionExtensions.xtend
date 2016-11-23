package de.fxdiagram.core.extensions

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XShape
import de.fxdiagram.core.anchors.ConnectionMemento
import javafx.animation.ParallelTransition
import javafx.animation.PathTransition
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*
import javafx.animation.Transition

class TransitionExtensions {

	static def Transition createMoveTransition(XShape shape, Point2D from, Point2D to, boolean toManuallyPlaced, Duration duration) {
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
				shape.manuallyPlaced = toManuallyPlaced
			]
		]
	}

	static def Transition createMorphTransition(XConnection connection, ConnectionMemento fromMemento, ConnectionMemento toMemento,
		Duration duration) {
		val from = fromMemento.points
		val to = toMemento.points
		connection.connectionRouter.manhattanRouter.reroutingEnabled = false
		val morph = new ParallelTransition
		connection.connectionRouter.growToSize(to.size)
		val controlPoints = connection.controlPoints
		for (i : 1 ..< controlPoints.size - 1) {
			val fromPoint = from.get(min(from.size - 1, i))
			val toPoint = to.get(min(to.size - 1, i))
			val currentControlPoint = controlPoints.get(i)
			if (fromPoint.distance(toPoint) > EPSILON) {
				val toManuallyPlaced = toMemento.controlPoints.get(min(to.size - 1, i)).manuallyPlaced
				morph.children += createMoveTransition(currentControlPoint, fromPoint, toPoint, toManuallyPlaced, duration)
			}
		}
		morph.onFinished = [
			connection.connectionRouter.shrinkToSize(to.size)
			connection.kind = toMemento.kind
			connection.controlPoints.head => [
				val toCP = toMemento.controlPoints.head
				layoutX = toCP.layoutX
				layoutY = toCP.layoutY
				side = toCP.side
			]
			connection.controlPoints.last => [
				val toCP = toMemento.controlPoints.last
				layoutX = toCP.layoutX
				layoutY = toCP.layoutY
				side = toCP.side
			]
			connection.connectionRouter.manhattanRouter.reroutingEnabled = true
		]
		return morph
	}

}
