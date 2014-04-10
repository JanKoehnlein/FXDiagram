package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.simple.SimpleNode
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import java.util.List
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode
abstract class AbstractMapping<T> {
	
	@Property Class<T> typeGuard
	
	new(Class<T> typeGuard) {
		this.typeGuard = typeGuard
	}
	 
	def boolean isApplicable(Object domainObject) {
		typeGuard.isInstance(domainObject)	
	}
}

class DiagramMapping<T> extends AbstractMapping<T> {
	
	List<AbstractNodeMappingCall<?, T>> nodes = newArrayList 
	List<AbstractConnectionMappingCall<?, T>> connections = newArrayList()
	(XtextDomainObjectDescriptor<T>)=>XDiagram createDiagram = [ new XDiagram ]
	
	new(Class<T> typeGuard) {
		super(typeGuard)
	}
	
	def getNodes() { nodes }
	def getConnections() { connections }
	def getCreateDiagram() { createDiagram }
	def setCreateDiagram((XtextDomainObjectDescriptor<T>)=>XDiagram createDiagram) { this.createDiagram = createDiagram }
	
	def <U> nodeFor(NodeMapping<U> nodeMapping, (T)=>U selector) {
		nodes += new NodeMappingCall(selector, nodeMapping)
	}

	def <U> connectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		connections += new ConnectionMappingCall(selector, connectionMapping)
	}

	def <U> nodeForEach(NodeMapping<U> nodeMapping, (T)=>List<? extends U> selector) {
		nodes += new MultiNodeMappingCall(selector, nodeMapping)
	}

	def <U> connectionForEach(ConnectionMapping<U> connectionMapping, (T)=>List<? extends U> selector) {
		connections += new MultiConnectionMappingCall(selector, connectionMapping)
	}
}

class NodeMapping<T> extends AbstractMapping<T> {
	
	List<AbstractConnectionMappingCall<?,T>> outgoing = newArrayList
	List<AbstractConnectionMappingCall<?,T>> incoming = newArrayList()
	(XtextDomainObjectDescriptor<T>)=>XNode createNode = [ new SimpleNode(it) ]
	
	new(Class<T> typeGuard) {
		super(typeGuard)
	}
	
	def getOutgoing() { outgoing }
	def getIncoming() { incoming }
	def getCreateNode() { createNode }
	def setCreateNode((XtextDomainObjectDescriptor<T>)=>XNode createNode) { this.createNode = createNode }
	
	def <U> outConnectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		val call = new ConnectionMappingCall(selector, connectionMapping)
		outgoing += call
		call
	}
	
	def <U> inConnectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		val call = new ConnectionMappingCall(selector, connectionMapping)
		incoming += call
		call
	}
	
	def <U> outConnectionForEach(ConnectionMapping<U> connectionMapping, (T)=>List<? extends U> selector) {
		val call = new MultiConnectionMappingCall(selector, connectionMapping)
		outgoing += call
		call
	}
	
	def <U> inConnectionForEach(ConnectionMapping<U> connectionMapping, (T)=>List<? extends U> selector) {
		val call = new MultiConnectionMappingCall(selector, connectionMapping)
		incoming += call
		call
	}
}

class ConnectionMapping<T> extends AbstractMapping<T> {

	NodeMappingCall<?, T> source
	NodeMappingCall<?, T> target
	(XtextDomainObjectDescriptor<T>)=>XConnection createConnection = [ new XConnection(it) ]
	
	new(Class<T> typeGuard) {
		super(typeGuard)
	}
	
	def getCreateConnection() { createConnection }
	def setCreateConnection((XtextDomainObjectDescriptor<T>)=>XConnection createConnection) { this.createConnection = createConnection }
	def getSource() {
		source
	}
	def getTarget() {
		target
	}
	def <U> source(NodeMapping<U> nodeMapping, (T)=>U selector) {
		source = new NodeMappingCall(selector, nodeMapping)
	}

	def <U> target(NodeMapping<U> nodeMapping, (T)=>U selector) {
		target = new NodeMappingCall(selector, nodeMapping)
	}
}

