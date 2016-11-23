package de.fxdiagram.core.extensions

import javafx.application.Platform
import javafx.util.Duration

class TimerExtensions {
	
	def static void defer(Runnable runnable, Duration time) {
		new Thread([|
			Thread.sleep(time.toMillis as long) 
			Platform.runLater(runnable)
		]).start
	}
}