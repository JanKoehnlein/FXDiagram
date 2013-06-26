package de.fxdiagram.core.layout

import de.fxdiagram.core.XNode
import javafx.animation.PathTransition
import javafx.scene.Group
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.QuadCurveTo
import javafx.util.Duration
import static java.lang.Math.*

class LayoutTransitionFactory {
	
	def createTransition(XNode node, double endX, double endY, Duration duration) {
		// hack: a PathTransition modifies translateX/Y but we need to modify layoutX/Y
		// we use a dummy node instead and bind its translate properties to the layout properties 
		// for the duration of the Transition
		val dummyNode = new Group
		val delegate = new PathTransition => [
			it.node = dummyNode
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				var double controlX
				var double controlY
				if(random > 0.5) {
					controlX = node.layoutX
					controlY = endY
				} else {
					controlX = endX
					controlY = node.layoutY
				}
				elements += new MoveTo(node.layoutX, node.layoutY)
				elements += new QuadCurveTo(controlX, controlY, endX, endY)
			]
		]
		node.layoutXProperty.bind(dummyNode.translateXProperty)
		node.layoutYProperty.bind(dummyNode.translateYProperty)
		delegate.onFinished = [
			node.layoutXProperty.unbind
			node.layoutYProperty.unbind
		]
		delegate
	}
}