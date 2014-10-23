package de.fxdiagram.lib.buttons

import de.fxdiagram.core.XNode

abstract class RapidButtonAction {
	
	def void perform(RapidButton button)
	 
	def isEnabled(XNode host) { true }
}
