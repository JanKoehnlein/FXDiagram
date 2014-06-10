package de.fxdiagram.xtext.glue.mapping

abstract class AbstractMapping<T> {
		
	String id

	XDiagramConfig config
	
	new(XDiagramConfig config, String id) {
		this.config = config
		this.id = id
		config.addMapping(this)
	}
	
	def getConfig() { config }
	
	boolean initialized = false
	
	protected final def initialize() {
		if(!initialized)
			calls()
		initialized = true
	}
	
	protected def void calls() {}
		 
	def boolean isApplicable(Object domainObject) {
		true	
	}
	
	def String getID() {
		id
	}
}