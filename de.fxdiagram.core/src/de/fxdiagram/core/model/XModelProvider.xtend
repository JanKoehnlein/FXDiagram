package de.fxdiagram.core.model

/**
 * Something that provides a model.
 * 
 * @see {@link ModelNode} 
 */
interface XModelProvider {
		
	def void populate(ModelElementImpl element)
	
}