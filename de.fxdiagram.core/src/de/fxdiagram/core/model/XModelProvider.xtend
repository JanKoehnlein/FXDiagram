package de.fxdiagram.core.model

/**
 * Something that provides a model, i.e. can be serializable.
 * 
 * @see {@link ModelNode} 
 */
interface XModelProvider {
		
	def void populate(ModelElementImpl element)
	
	/**
	 * Implementing classes can return <code>true</code> if the specific implementation should not be serialized.
	 */
	def boolean isTransient() {
		false
	}
}