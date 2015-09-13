package de.fxdiagram.core.command

import javafx.animation.Transition
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class EmptyTransition extends Transition {

	new() {
		super.cycleDuration = 0.millis
	}
	
	override protected interpolate(double frac) {
	}
}