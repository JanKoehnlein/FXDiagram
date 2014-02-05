package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly

interface OpenBehavior extends Behavior {
	def void open()
}

abstract class AbstractOpenBehavior implements OpenBehavior {
	
	@FxProperty@ReadOnly boolean isActive
	
	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
	}
	
	override getBehaviorKey() {
		OpenBehavior
	}
	
	def protected doActivate() {
	}
}