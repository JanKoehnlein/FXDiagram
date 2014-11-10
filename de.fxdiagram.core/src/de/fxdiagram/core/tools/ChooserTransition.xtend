package de.fxdiagram.core.tools

import javafx.animation.Transition
import static extension javafx.util.Duration.*
import static java.lang.Math.*

class ChooserTransition extends Transition {

	AbstractBaseChooser tool

	double startPosition
	double endPosition

	new(AbstractBaseChooser tool) {
		this.tool = tool
		interpolator = [alpha|1 - (1 - alpha) * (1 - alpha)]
		onFinished = [
			tool.currentPosition = endPosition
		]
	}

	def setTargetPositionDelta(int targetPositionDelta) {
		startPosition = tool.getCurrentPosition
		endPosition = (startPosition + targetPositionDelta) as int
		while (endPosition < 0) {
			startPosition = startPosition + tool.getNodes.size
			endPosition = endPosition + tool.getNodes.size
		}
		setDuration(2000)
		playFromStart
	}

	def setTargetPosition(double targetPosition) {
		if (targetPosition < tool.getNodes.size) {
			startPosition = tool.getCurrentPosition
			endPosition = targetPosition as int
			if (abs(targetPosition - startPosition) > tool.getNodes.size / 2) {
				if (targetPosition > startPosition)
					startPosition = startPosition + tool.getNodes.size()
				else
					endPosition = endPosition + tool.getNodes.size()
			}
			setDuration(1000)
			playFromStart
		}
	}

	def resetTargetPosition() {
		setTargetPosition(endPosition)
	}

	protected def setDuration(double max) {
		val duration = min(max, abs((endPosition - startPosition) % tool.getNodes.size) * 200)
		cycleDuration = duration.millis
	}

	override protected interpolate(double alpha) {
		tool.currentPosition = interpolator.interpolate(startPosition, endPosition, alpha)
	}

}