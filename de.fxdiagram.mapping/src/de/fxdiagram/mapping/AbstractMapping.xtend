package de.fxdiagram.mapping

import de.fxdiagram.mapping.execution.XDiagramConfig

/**
 * Describes a fixed mapping of a domain object represented by a {@link IMappedElementDescriptor} 
 * to a diagram element.
 * 
 * Mappings are collected in an {@link XDiagramConfiguration}.
 */
abstract class AbstractMapping<T> {
		
	String id
	String displayName
	XDiagramConfig config
	
	new(XDiagramConfig config, String id, String displayName) {
		this.config = config
		this.id = id
		this.displayName = displayName
		config.addMapping(this)
	}
	
	def getConfig() { config }
	
	boolean initialized = false
	
	protected final def initialize() {
		if(!initialized)
			calls()
		initialized = true
	}
	
	/**
	 * Executed when a mapping of this type is established. Add calls to other mappings here,
	 * if you want to recursively add further elements automatically.
	 */
	protected def void calls() {}
		 
	def boolean isApplicable(Object domainObject) {
		true	
	}
	
	def String getID() {
		id
	}
	
	def String getDisplayName() {
		displayName
	}
}