package de.fxdiagram.core.model

/**
 * Something that provides a model, i.e. can be serializable.
 * 
 * @see {@link ModelNode} 
 */
interface XModelProvider {
		
	def void populate(ModelElementImpl element)
	
}