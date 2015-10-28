package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import javafx.scene.Node

@Logging
abstract class AbstractHostBehavior <T extends Node> implements Behavior {
	
	T host 
	
	@FxProperty(readOnly=true) boolean isActive
	
	new(T host) {
		this.host = host	
	}
	
	def getHost() {
		host
	}
	
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
	
	def protected void doActivate()
}