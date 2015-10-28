package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.logging.Logging

interface OpenBehavior extends Behavior {
	def void open()
}

@Logging
abstract class AbstractOpenBehavior implements OpenBehavior {
	
	@FxProperty(readOnly=true) boolean isActive
	
	override activate() {
		if(!isActive)
			try {
				doActivate
				isActiveProperty.set(true)
			} catch (Exception exc) {
				LOG.severe(exc.message)
				exc.printStackTrace
			}
	}
	
	override getBehaviorKey() {
		OpenBehavior
	}
	
	def protected doActivate() {
	}
}