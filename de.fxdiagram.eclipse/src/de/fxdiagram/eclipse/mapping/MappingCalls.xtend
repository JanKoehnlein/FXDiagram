package de.fxdiagram.eclipse.mapping

import javafx.geometry.Side
import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.Data

interface MappingCall<RESULT, ARG> {
	def AbstractMapping<RESULT> getMapping()
}

abstract class AbstractConnectionMappingCall<RESULT, ARG> implements MappingCall<RESULT, ARG>{

	(Side)=>Node imageFactory

	def isLazy() { imageFactory != null }

	def makeLazy((Side)=>Node imageFactory) {
		this.imageFactory = imageFactory
	}
	
	def getImage(Side side) {
		imageFactory.apply(side)
	} 

	@Accessors String role

	def ConnectionMapping<RESULT> getConnectionMapping()
	
	override getMapping() {
		connectionMapping
	}
}

@Data
class ConnectionMappingCall<RESULT, ARG> extends AbstractConnectionMappingCall<RESULT, ARG> {
	(ARG)=>RESULT selector
	ConnectionMapping<RESULT> connectionMapping
	
}

@Data
class MultiConnectionMappingCall<RESULT, ARG> extends AbstractConnectionMappingCall<RESULT, ARG> {
	(ARG)=>Iterable<? extends RESULT> selector
	ConnectionMapping<RESULT> connectionMapping
}

abstract class AbstractNodeMappingCall<RESULT, ARG> implements MappingCall<RESULT, ARG> {
	def NodeMapping<RESULT> getNodeMapping()

	override getMapping() {
		nodeMapping
	}
}

@Data
class NodeMappingCall<RESULT, ARG> extends AbstractNodeMappingCall<RESULT, ARG> {
	(ARG)=>RESULT selector
	NodeMapping<RESULT> nodeMapping	
}

@Data
class MultiNodeMappingCall<RESULT, ARG> extends AbstractNodeMappingCall<RESULT, ARG> {
	(ARG)=>Iterable<? extends RESULT> selector
	NodeMapping<RESULT> nodeMapping	
}

@Data
class DiagramMappingCall<RESULT, ARG> implements MappingCall<RESULT, ARG> {
	(ARG)=>RESULT selector
	DiagramMapping<RESULT> diagramMapping
	
	override getMapping() {
		diagramMapping
	}
	
}