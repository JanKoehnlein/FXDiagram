package de.fxdiagram.core.layout

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionKind
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

import static java.lang.Math.*

class ConnectionMorphCommand extends AbstractAnimationCommand {
	
	XConnection connection
	
	XConnectionKind fromKind
	XConnectionKind toKind
	
	List<Point2D> fromPoints
	List<Point2D> toPoints
	
	new(XConnection connection, XConnectionKind toKind, List<Point2D> toPoints) {
		this.connection = connection
		this.fromKind = connection.kind
		this.toKind = toKind
		this.fromPoints = newArrayList(connection.controlPoints.map[new Point2D(layoutX, layoutY)])
		this.toPoints = toPoints
	}
	
	override createExecuteAnimation(CommandContext context) {
		createMorphTransition(fromPoints, toKind, toPoints, context.defaultExecuteDuration)
	}

	override createUndoAnimation(CommandContext context) {
		createMorphTransition(toPoints, fromKind, fromPoints, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMorphTransition(fromPoints, toKind, toPoints, context.defaultUndoDuration)
	}
	
	def createMorphTransition(List<Point2D> fromPoints, XConnectionKind toKind, List<Point2D> toPoints, Duration duration) {
		val morph = new ParallelTransition
		connection.kind = toKind
		val controlPoints = connection.controlPoints
		connection.connectionRouter.growToSize(toPoints.size)
		for(i: 1..<controlPoints.size-1) {
			val index = min(toPoints.size-1, i)
			val toPoint = toPoints.get(index)
			val currentControlPoint = controlPoints.get(i)
			morph.children += createMoveTransition(currentControlPoint, new Point2D(currentControlPoint.layoutX, currentControlPoint.layoutY), toPoint, duration)
		}
		morph.onFinished = [
			connection.connectionRouter.shrinkToSize(toPoints.size)
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