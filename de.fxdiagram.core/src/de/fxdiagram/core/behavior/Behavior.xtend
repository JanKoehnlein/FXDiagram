package de.fxdiagram.core.behavior

import de.fxdiagram.core.XActivatable

interface Behavior extends XActivatable {
	
	def Class<? extends Behavior> getBehaviorKey()
	
}