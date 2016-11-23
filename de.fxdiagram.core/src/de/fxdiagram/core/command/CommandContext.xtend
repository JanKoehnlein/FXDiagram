package de.fxdiagram.core.command

import de.fxdiagram.core.XRoot
import javafx.util.Duration

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class CommandContext {
	
	XRoot root
	
	AnimationQueue animationQueue = new AnimationQueue
	
	Duration defaultExecuteDuration = 1000.millis
	
	Duration defaultUndoDuration = 250.millis
	
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