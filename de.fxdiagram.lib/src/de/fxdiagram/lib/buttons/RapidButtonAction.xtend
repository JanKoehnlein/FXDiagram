package de.fxdiagram.lib.buttons

import de.fxdiagram.annotations.properties.FxProperty

abstract class RapidButtonAction {
	
	@FxProperty boolean enabled = true
	
	def void perform(RapidButton button)
	 
}
