package de.fxdiagram.core.command

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import javafx.geometry.Point2D
import javafx.util.Duration

import static de.fxdiagram.core.extensions.TransitionExtensions.*

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
		if(shape instanceof XNode) {
			fromX -= shape.placementGroup.layoutX
			fromY -= shape.placementGroup.layoutY
			shape.placementGroup => [
				layoutX = 0
				layoutY = 0	
			]
			shape.placementHint = null
		}
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
		createMoveTransition(shape, new Point2D(fromX, fromY), new Point2D(toX, toY), duration)
	}
}