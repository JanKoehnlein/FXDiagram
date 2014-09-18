package de.fxdiagram.lib.buttons

abstract class RapidButtonAction {
	
	def void perform(RapidButton button)
	 
	def isEnabled(RapidButton button) { true }
}
