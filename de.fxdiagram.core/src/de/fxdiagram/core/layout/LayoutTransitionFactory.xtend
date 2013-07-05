package de.fxdiagram.core.layout

import de.fxdiagram.core.XShape
import javafx.animation.PathTransition
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.QuadCurveTo
import javafx.util.Duration

import static java.lang.Math.*

class LayoutTransitionFactory {
	
	def createTransition(XShape shape, double endX, double endY, boolean curve, Duration duration) {
		// hack: a PathTransition modifies translateX/Y but we need to modify layoutX/Y
		// we use a dummy node instead and bind its translate properties to the layout properties 
		// for the duration of the Transition
		val dummyNode = new Group => [
			translateX = shape.layoutX
			translateY = shape.layoutY
		]
		val delegate = new PathTransition => [
			it.node = dummyNode
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				elements += new MoveTo(shape.layoutX, shape.layoutY)
				if(curve) {
					var double controlX
					var double controlY
					if(random > 0.5) {
						controlX = shape.layoutX
						controlY = endY
					} else {
						controlX = endX
						controlY = shape.layoutY
					}
						elements += new QuadCurveTo(controlX, controlY, endX, endY)
				} else {
					elements += new LineTo(endX, endY)
				}
			]
		]
		shape.layoutXProperty.bind(dummyNode.translateXProperty)
		shape.layoutYProperty.bind(dummyNode.translateYProperty)
		delegate.onFinished = [
			shape.layoutXProperty.unbind
			shape.layoutYProperty.unbind
		]
		delegate
	}
}