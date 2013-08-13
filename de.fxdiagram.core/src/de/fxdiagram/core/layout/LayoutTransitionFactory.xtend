package de.fxdiagram.core.layout

import de.fxdiagram.core.XShape
import javafx.animation.PathTransition
import javafx.scene.Group
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.QuadCurveTo
import javafx.util.Duration

import static de.fxdiagram.core.layout.LayoutTransitionStyle.*

class LayoutTransitionFactory {

	def createTransition(XShape shape, double endX, double endY, LayoutTransitionStyle style, Duration duration) {
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
				switch style {
					case STRAIGHT:
						elements += new LineTo(endX, endY)
					case CURVE_XFIRST:
						elements += new QuadCurveTo(endX, shape.layoutY, endX, endY)
					case CURVE_YFIRST:
						elements += new QuadCurveTo(shape.layoutX, endY, endX, endY)
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

enum LayoutTransitionStyle {
	STRAIGHT,
	CURVE_XFIRST,
	CURVE_YFIRST
}
