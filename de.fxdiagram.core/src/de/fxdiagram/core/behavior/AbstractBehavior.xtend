package de.fxdiagram.core.behavior

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XActivatable
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.annotations.properties.FxProperty

abstract class AbstractBehavior implements XActivatable {
	
	XNode host 
	
	@FxProperty@ReadOnly boolean isActive
	
	new(XNode host) {
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