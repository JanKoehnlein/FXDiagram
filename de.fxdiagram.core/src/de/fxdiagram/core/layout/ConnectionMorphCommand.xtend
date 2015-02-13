package de.fxdiagram.core.layout

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import java.util.List
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

class ConnectionMorphCommand extends AbstractAnimationCommand {
	
	XConnection connection
	
	XConnection.Kind fromKind
	XConnection.Kind toKind
	
	List<Point2D> fromPoints
	List<Point2D> toPoints
	
	new(XConnection connection, XConnection.Kind toKind, List<Point2D> toPoints) {
		this.connection = connection
		this.toKind = toKind
		this.toPoints = toPoints
	}
	
	override createExecuteAnimation(CommandContext context) {
		this.fromKind = connection.kind
		this.fromPoints = newArrayList(connection.controlPoints.map[new Point2D(layoutX, layoutY)])
		createMorphTransition(fromPoints, toKind, toPoints, context.executeDuration)
	}

	override createUndoAnimation(CommandContext context) {
		createMorphTransition(toPoints, fromKind, fromPoints, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMorphTransition(fromPoints, toKind, toPoints, context.defaultUndoDuration)
	}
	
	def createMorphTransition(List<Point2D> from, XConnection.Kind toKind, List<Point2D> to, Duration duration) {
		val morph = new ParallelTransition
		connection.kind = toKind
		connection.connectionRouter.growToSize(to.size)
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