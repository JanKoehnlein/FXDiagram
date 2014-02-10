package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.ModelNode

/**
 * Something that provides a model.
 * 
 * @see {@link ModelNode} 
 */
interface XModelProvider {
		
	def void populate(ModelElement element)
	
}