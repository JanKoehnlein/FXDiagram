package de.fxdiagram.xtext.glue.mapping

import java.util.List

@Data
abstract class AbstractConnectionMappingCall<T> {
}

@Data
class ConnectionMappingCall<T, U> extends AbstractConnectionMappingCall<T> {
	(U)=>T selector
	ConnectionMapping<T> connectionMapping
}

@Data
class MultiConnectionMappingCall<T, U> extends AbstractConnectionMappingCall<T> {
	(U)=>List<? extends T> selector
	ConnectionMapping<T> connectionMapping
}

abstract class AbstractNodeMappingCall<T> {
}

@Data
class NodeMappingCall<T, U> extends AbstractNodeMappingCall<T> {
	(U)=>T selector
	NodeMapping<T> nodeMapping	
}

@Data
class MultiNodeMappingCall<T, U> extends AbstractNodeMappingCall<T> {
	(U)=>List<? extends T> selector
	NodeMapping<T> nodeMapping	
}

