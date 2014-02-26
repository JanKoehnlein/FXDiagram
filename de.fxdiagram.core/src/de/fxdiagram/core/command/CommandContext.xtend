package de.fxdiagram.core.command

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.util.Duration

class CommandContext {
	
	AnimationQueue animationQueue = new AnimationQueue
	
	Duration defaultExecuteDuration = 1000.millis
	
	Duration defaultUndoDuration = 150.millis
	
	def getAnimationQueue() {
		animationQueue
	} 
	
	def getDefaultExecuteDuration() {
		defaultExecuteDuration
	}
	
	def getDefaultUndoDuration() {
		defaultUndoDuration
	}
}