package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty


interface CloseBehavior extends Behavior {
	def void close()
}

abstract class AbstractCloseBehavior implements CloseBehavior {
	
	@FxProperty(readOnly=true) boolean isActive
	
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

