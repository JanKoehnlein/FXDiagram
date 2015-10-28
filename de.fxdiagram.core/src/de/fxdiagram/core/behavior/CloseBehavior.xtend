package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty

interface CloseBehavior extends Behavior {
	def void close()
}

@Logging
abstract class AbstractCloseBehavior implements CloseBehavior {

	@FxProperty(readOnly=true) boolean isActive

	override getBehaviorKey() {
		CloseBehavior
	}

	override activate() {
		if (!isActive)
			try {
				doActivate
				isActiveProperty.set(true)
			} catch (Exception exc) {
				LOG.severe(exc.message)
				exc.printStackTrace
			}
	}

	def protected doActivate() {
	}
}

