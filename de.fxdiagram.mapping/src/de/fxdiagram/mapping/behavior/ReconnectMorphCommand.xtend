package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
import javafx.animation.PathTransition
import javafx.animation.SequentialTransition
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.shape.Circle
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@FinalFieldsConstructor
class ReconnectMorphCommand extends AbstractAnimationCommand {
	val XConnection connection
	val XNode oldNode 
	val XNode newNode 
	val boolean isSource
	
	override createExecuteAnimation(CommandContext context) {
		morph(newNode, context.defaultExecuteDuration)
	}
	
	override createUndoAnimation(CommandContext context) {
		morph(oldNode, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		morph(newNode, context.defaultUndoDuration)
	}
	
	protected def morph(XNode nodeAfterMorph, Duration duration) {
		val from = if(isSource)
				connection.connectionRouter.findClosestSourceAnchor(connection.source, false)
			else
				connection.connectionRouter.findClosestTargetAnchor(connection.target, false)
		val to = if(isSource)
				connection.connectionRouter.findClosestSourceAnchor(nodeAfterMorph, false)
			else  
				connection.connectionRouter.findClosestTargetAnchor(nodeAfterMorph, false)
		val dummy = new Group => [
			translateX = from.x
			translateY = from.y
		]
		val dummyNode = new XNode () {
			override protected createNode() {
				new Circle(0)
			}
		} => [
			layoutXProperty.bind(dummy.translateXProperty)
			layoutYProperty.bind(dummy.translateYProperty)
		]
		new SequentialTransition => [
			children += new EmptyTransition => [
				onFinished = [
					connection.diagram.nodes += dummyNode 
					if(isSource)
						connection.source = dummyNode
					else 
						connection.target = dummyNode
				]
			]
			children += createPathTransition(from, to, dummyNode, dummy, nodeAfterMorph, duration)
		]
	}
	
	protected def createPathTransition(Point2D from, Point2D to, XNode dummyNode, Node dummy, XNode nodeAfterMorph, Duration duration) {
		new PathTransition => [
			node = dummy
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				elements += new MoveTo(from.x, from.y)
				elements += new LineTo(to.x, to.y)
			]
			onFinished = [
				if(isSource)
					connection.source = nodeAfterMorph
				else
					connection.target = nodeAfterMorph
				connection.diagram.nodes -= dummyNode 
			]
		]
	}
}