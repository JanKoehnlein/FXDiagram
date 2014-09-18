package de.fxdiagram.lib.buttons

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XButton
import de.fxdiagram.core.XNode
import javafx.geometry.Side
import javafx.scene.Node
import javafx.scene.Parent

class RapidButton extends Parent implements XButton {
	
	@FxProperty(readOnly) boolean isActive
	
	XNode host
	
	RapidButtonAction action
	
	Side position
	
	new(XNode host, Side position, Node image, 
		RapidButtonAction action) {
		this.host = host
		this.action = action
		this.position = position
		children += image
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
	}

	def doActivate() {
		onMousePressed = [ 
			action.perform(this)
			consume
		]
	}

	def getPosition() { position }

	def getHost() {	host }

	def getAction() { action }	
}

