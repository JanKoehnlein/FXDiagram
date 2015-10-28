package de.fxdiagram.lib.buttons

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XButton
import de.fxdiagram.core.XNode
import javafx.geometry.Side
import javafx.scene.Node
import javafx.scene.Parent

/**
 * A button that pops up when the mouse hovers over its {@link #host}.
 * 
 * @see RapidButtonBehavior
 */
@Logging
class RapidButton extends Parent implements XButton {
	
	@FxProperty(readOnly=true) boolean isActive
	
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
		try {
			doActivate
			isActiveProperty.set(true)
		} catch (Exception exc) {
			LOG.severe(exc.message)
			exc.printStackTrace
		}
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

