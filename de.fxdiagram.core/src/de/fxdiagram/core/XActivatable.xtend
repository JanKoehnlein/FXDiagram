package de.fxdiagram.core

/** 
 * An interface for something that can be activated. 
 * This ususally happens when the corresponding element is added to the diagram.
 * Used to register listeners and add behavior.
 */
interface XActivatable {
	def void activate()
}