package de.fxdiagram.core.behavior

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XActivatable

abstract class AbstractBehavior implements XActivatable {
	
	XNode host 
	
	boolean isActive
	
	new(XNode host) {
		this.host = host	
	}
	
	def getHost() {
		host
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActive = true
	}
	
	def protected void doActivate()
}