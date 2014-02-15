package de.fxdiagram.core.command

import de.fxdiagram.core.XShape
import javafx.animation.PathTransition
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration

class MoveCommand implements Command {

	XShape shape
	
	double targetX
	double targetY
	
	new(XShape shape, double fromX, double fromY) {
		this.shape = shape;
		this.targetX = fromX
		this.targetY = fromY
	}
	
	override execute(Duration duration) {
		null
	}
	
	override canUndo() {
		true
	}
	
	override undo(Duration duration) {
		createMoveTransition(duration)
	}
	
	protected def createMoveTransition(Duration duration) {
		val fromX = shape.layoutX
		val fromY = shape.layoutY
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
				elements += new LineTo(targetX, targetY)
			]
			onFinished = [
				shape => [
					layoutXProperty.unbind
					layoutYProperty.unbind
					layoutX = targetX
					targetX = fromX
					layoutY = targetY
					targetY = fromY
				]
			]
		]
	}
	
	override canRedo() {
		true
	}
	
	override redo(Duration duration) {
		createMoveTransition(duration)
	}
}