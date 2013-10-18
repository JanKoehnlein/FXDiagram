package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly


interface CloseBehavior extends Behavior {
	def void close()
}

abstract class AbstractCloseBehavior implements CloseBehavior {
	
	@FxProperty@ReadOnly boolean isActive
	
	override getBehaviorKey() {
		CloseBehavior
	}

	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
	}
	
	def protected doActivate() {
	}
}

