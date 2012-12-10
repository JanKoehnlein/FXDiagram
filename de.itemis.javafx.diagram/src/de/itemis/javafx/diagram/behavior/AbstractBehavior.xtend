package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.ShapeContainer
import de.itemis.javafx.diagram.Diagram

abstract class AbstractBehavior {
	
	ShapeContainer host 
	
	new(ShapeContainer host) {
		this.host = host	
	}
	
	def getHost() {
		host
	}
	
	def protected void activate(Diagram diagram)
}