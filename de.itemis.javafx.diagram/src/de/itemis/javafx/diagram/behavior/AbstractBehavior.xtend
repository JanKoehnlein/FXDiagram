package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.Activateable
import de.itemis.javafx.diagram.XNode

abstract class AbstractBehavior implements Activateable {
	
	XNode host 
	
	new(XNode host) {
		this.host = host	
	}
	
	def getHost() {
		host
	}
}