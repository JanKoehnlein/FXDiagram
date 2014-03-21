package de.fxdiagram.core.command

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.util.Duration
import de.fxdiagram.core.XRoot

class CommandContext {
	
	XRoot root
	
	AnimationQueue animationQueue = new AnimationQueue
	
	Duration defaultExecuteDuration = 1000.millis
	
	Duration defaultUndoDuration = 150.millis
	
	new(XRoot root) {
		this.root = root
	}
	
	def getRoot() {
		root	
	}
	
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