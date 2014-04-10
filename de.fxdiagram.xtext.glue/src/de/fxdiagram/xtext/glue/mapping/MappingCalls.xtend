package de.fxdiagram.xtext.glue.mapping

import java.util.List

abstract class AbstractConnectionMappingCall<T, ARG> {
	boolean lazy = false
	def isLazy() { lazy }
	def makeLazy() { lazy = true } 
	@Property String role
	def ConnectionMapping<T> getConnectionMapping()
	
}

@Data
class ConnectionMappingCall<T, ARG> extends AbstractConnectionMappingCall<T, ARG> {
	(ARG)=>T selector
	ConnectionMapping<T> connectionMapping
}

@Data
class MultiConnectionMappingCall<T, ARG> extends AbstractConnectionMappingCall<T, ARG> {
	(ARG)=>List<? extends T> selector
	ConnectionMapping<T> connectionMapping
}

abstract class AbstractNodeMappingCall<T, ARG> {
	def NodeMapping<T> getNodeMapping()
}

@Data
class NodeMappingCall<T, ARG> extends AbstractNodeMappingCall<T, ARG> {
	(ARG)=>T selector
	NodeMapping<T> nodeMapping	
}

@Data
class MultiNodeMappingCall<T, ARG> extends AbstractNodeMappingCall<T, ARG> {
	(ARG)=>List<? extends T> selector
	NodeMapping<T> nodeMapping	
}

