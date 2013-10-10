package de.fxdiagram.core.extensions

import javafx.util.Duration
import javafx.application.Platform

class TimerExtensions {
	
	def static void defer(Runnable runnable, Duration time) {
		new Thread([|
			Thread.sleep(time.toMillis as long) 
			Platform.runLater(runnable)
		]).start
	}
}