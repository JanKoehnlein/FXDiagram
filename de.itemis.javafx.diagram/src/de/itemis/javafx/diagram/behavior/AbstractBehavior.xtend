package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.XActivatable

abstract class AbstractBehavior implements XActivatable {
	
	XNode host 
	
	new(XNode host) {
		this.host = host	
	}
	
	def getHost() {
		host
	}
}