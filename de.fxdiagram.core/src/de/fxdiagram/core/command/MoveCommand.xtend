package de.fxdiagram.core.command

import de.fxdiagram.core.XShape
import javafx.animation.PathTransition
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration

class MoveCommand extends AbstractAnimationCommand {

	XShape shape
	
	double fromX
	double fromY
	double toX
	double toY
	
	new(XShape shape, double toX, double toY) {
		this.shape = shape;
		this.fromX = shape.layoutX
		this.fromY = shape.layoutY
		this.toX = toX
		this.toY = toY
	}
	
	new(XShape shape, double fromX, double fromY, double toX, double toY) {
		this.shape = shape;
		this.fromX = fromX
		this.fromY = fromY
		this.toX = toX
		this.toY = toY
	}
	
	override createExecuteAnimation(CommandContext context) {
		createMoveTransition(fromX, fromY, toX, toY, context.executeDuration)
	}
	
	override createUndoAnimation(CommandContext context) {
		createMoveTransition(toX, toY, fromX, fromY, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMoveTransition(fromX, fromY, toX, toY, context.defaultUndoDuration)
	}
	
	protected def createMoveTransition(double fromX, double fromY, double toX, double toY, Duration duration) {
		if(shape.layoutX == toX && shape.layoutY == toY)
			return null;
		val dummyNode = new Group => [
			translateX = fromX
			translateY = fromY
		]
		shape.layoutXProperty.bind(dummyNode.translateXProperty)
		shape.layoutYProperty.bind(dummyNode.translateYProperty)
		new PathTransition => [
			it.node = dummyNode
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				elements += new MoveTo(fromX, fromY)
				elements += new LineTo(toX, toY)
			]
			onFinished = [
				shape => [
					layoutXProperty.unbind
					layoutYProperty.unbind
					layoutX = toX
					layoutY = toY
				]
			]
		]
	}
}