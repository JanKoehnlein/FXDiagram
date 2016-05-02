package de.fxdiagram.core.command

import de.fxdiagram.core.XShape
import javafx.animation.PathTransition
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration
import javafx.scene.Node
import de.fxdiagram.core.XNode

class MoveCommand extends AbstractAnimationCommand {

	Node node
	
	double fromX
	double fromY
	double toX
	double toY
	
	new(Node shape, double toX, double toY) {
		this.node = shape;
		this.fromX = shape.layoutX
		this.fromY = shape.layoutY
		this.toX = toX
		this.toY = toY
	}
	
	new(XShape shape, double fromX, double fromY, double toX, double toY) {
		this.node = shape;
		this.fromX = fromX
		this.fromY = fromY
		this.toX = toX
		this.toY = toY
	}
	
	override createExecuteAnimation(CommandContext context) {
		if(node instanceof XNode) {
			fromX -= node.placementGroup.layoutX
			fromY -= node.placementGroup.layoutY
			node.placementGroup => [
				layoutX = 0
				layoutY = 0	
			]
			node.placementHint = null
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
		if(node.layoutX == toX && node.layoutY == toY)
			return null;
		val dummyNode = new Group => [
			translateX = fromX
			translateY = fromY
		]
		node.layoutXProperty.bind(dummyNode.translateXProperty)
		node.layoutYProperty.bind(dummyNode.translateYProperty)
		new PathTransition => [
			it.node = dummyNode
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				elements += new MoveTo(fromX, fromY)
				elements += new LineTo(toX, toY)
			]
			onFinished = [
				node => [
					layoutXProperty.unbind
					layoutYProperty.unbind
					layoutX = toX
					layoutY = toY
				]
			]
		]
	}
}