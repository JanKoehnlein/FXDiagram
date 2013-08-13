package de.fxdiagram.core

import javafx.scene.Group

class XRoot extends Group {
	
	XRootDiagram diagram
	
	new() {
		diagram = new XRootDiagram(this)
		children += diagram
	}
	
	def getDiagram() {
		diagram
	}
	
}