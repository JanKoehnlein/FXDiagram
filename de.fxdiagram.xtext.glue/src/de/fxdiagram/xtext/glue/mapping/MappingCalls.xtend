package de.fxdiagram.xtext.glue.mapping

import javafx.geometry.Side
import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.Data

interface MappingCall<T, ARG> {
	def AbstractMapping<T> getMapping()
}

abstract class AbstractConnectionMappingCall<T, ARG> implements MappingCall<T, ARG>{

	(Side)=>Node imageFactory

	def isLazy() { imageFactory != null }

	def makeLazy((Side)=>Node imageFactory) {
		this.imageFactory = imageFactory
	}
	
	def getImage(Side side) {
		imageFactory.apply(side)
	} 

	@Accessors String role

	def ConnectionMapping<T> getConnectionMapping()
	
	override getMapping() {
		connectionMapping
	}
}

@Data
class ConnectionMappingCall<T, ARG> extends AbstractConnectionMappingCall<T, ARG> {
	(ARG)=>T selector
	ConnectionMapping<T> connectionMapping
	
}

@Data
class MultiConnectionMappingCall<T, ARG> extends AbstractConnectionMappingCall<T, ARG> {
	(ARG)=>Iterable<? extends T> selector
	ConnectionMapping<T> connectionMapping
}

abstract class AbstractNodeMappingCall<T, ARG> implements MappingCall<T, ARG> {
	def NodeMapping<T> getNodeMapping()

	override getMapping() {
		nodeMapping
	}
}

@Data
class NodeMappingCall<T, ARG> extends AbstractNodeMappingCall<T, ARG> {
	(ARG)=>T selector
	NodeMapping<T> nodeMapping	
}

@Data
class MultiNodeMappingCall<T, ARG> extends AbstractNodeMappingCall<T, ARG> {
	(ARG)=>Iterable<? extends T> selector
	NodeMapping<T> nodeMapping	
}

@Data
class DiagramMappingCall<T, ARG> implements MappingCall<T, ARG> {
	(ARG)=>T selector
	DiagramMapping<T> diagramMapping
	
	override getMapping() {
		diagramMapping
	}
	
}