package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XActivatable
import de.fxdiagram.core.XShape

abstract class AbstractBehavior <T extends XShape> implements XActivatable {
	
	T host 
	
	@FxProperty@ReadOnly boolean isActive
	
	new(T host) {
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