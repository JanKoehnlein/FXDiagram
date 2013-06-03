package de.itemis.javafx.diagram.tools.chooser

import javafx.animation.Transition
import javafx.util.Duration
import static java.lang.Math.*

class XNodeChooserTransition extends Transition {

	AbstractXNodeChooser tool

	double startPosition
	double endPosition

	new(AbstractXNodeChooser tool) {
		this.tool = tool
		interpolator = [alpha|1 - (1 - alpha) * (1 - alpha)]
		onFinished = [
			tool.currentPosition = endPosition
		]
	}

	def setTargetPositionDelta(int targetPositionDelta) {
		startPosition = tool.currentPosition
		endPosition = (startPosition + targetPositionDelta) as int
		while (endPosition < 0) {
			startPosition = startPosition + tool.nodes.size
			endPosition = endPosition + tool.nodes.size
		}
		setDuration(2000)
		playFromStart
	}

	def setTargetPosition(double targetPosition) {
		if (targetPosition < tool.nodes.size) {
			startPosition = tool.currentPosition
			endPosition = targetPosition as int
			if (abs(targetPosition - startPosition) > tool.nodes.size / 2) {
				if (targetPosition > startPosition)
					startPosition = startPosition + tool.nodes.size()
				else
					endPosition = endPosition + tool.nodes.size()
			}
			setDuration(1000)
			playFromStart
		}
	}

	def resetTargetPosition() {
		setTargetPosition(endPosition)
	}

	protected def setDuration(double max) {
		val duration = min(max, abs((endPosition - startPosition) % tool.nodes.size) * 200)
		cycleDuration = Duration.millis(duration)
	}

	override protected interpolate(double alpha) {
		tool.currentPosition = interpolator.interpolate(startPosition, endPosition, alpha)
	}

}