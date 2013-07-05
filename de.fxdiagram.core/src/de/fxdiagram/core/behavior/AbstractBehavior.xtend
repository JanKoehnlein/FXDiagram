package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XActivatable
import de.fxdiagram.core.XShape

abstract class AbstractBehavior implements XActivatable {
	
	XShape host 
	
	@FxProperty@ReadOnly boolean isActive
	
	new(XShape host) {
		this.host = host	
	}
	
	def getHost() {
		host
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
	}
	
	def protected void doActivate()
}