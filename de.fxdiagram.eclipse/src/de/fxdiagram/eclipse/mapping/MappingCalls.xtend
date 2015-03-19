package de.fxdiagram.eclipse.mapping

import de.fxdiagram.eclipse.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.lib.buttons.RapidButton
import javafx.geometry.Side
import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Data

/**
 * The execution of a {@link AbstractMapping} in a certain context.
 * 
 * Usually created in the {@link AbstractMapping#calls()} method by calling 
 * factory methods
 * <ul>
 * <li>{@link DiagramMapping#nodeFor()},</li> 
 * <li>{@link DiagramMapping#nodeForEach()},</li> 
 * <li>{@link DiagramMapping#connectionFor()},</li> 
 * <li>{@link DiagramMapping#connectionForEach()},</li> 
 * <li>{@link NodeMapping#inConnectionFor()},</li>
 * <li>{@link NodeMapping#inConnectionForEach()},</li>
 * <li>{@link NodeMapping#outConnectionFor()},</li> 
 * <li>{@link NodeMapping#outConnectionForEach()},</li> 
 * <li>{@link NodeMapping#nestedDiagramFor()},</li> 
 * <li>{@link ConnectionMapping#source()}, or</li>  
 * <li>{@link ConnectionMapping#target()}.</li>
 * </ul>
 * 
 * Connection mappings can also be defined as lazy, deferring their execution to a user action. 
 */
interface MappingCall<RESULT, ARG> {
	def AbstractMapping<RESULT> getMapping()
}

abstract class AbstractConnectionMappingCall<RESULT, ARG> implements MappingCall<RESULT, ARG>{

	(Side)=>Node imageFactory

	def isLazy() { imageFactory != null }

	/**
	 * Instead of immediately adding this connection and the connected node, create
	 * a {@link LazyConnectionMappingBehavior} that allows to explore this connection 
	 * using a {@link RapidButton}.
	 */
	def asButton((Side)=>Node imageFactory) {
		this.imageFactory = imageFactory
	}
	
	def getImage(Side side) {
		imageFactory.apply(side)
	} 

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