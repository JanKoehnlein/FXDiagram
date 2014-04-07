package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import java.util.List
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.lib.simple.SimpleNode

abstract class BaseMapping<T> {
	
	@Property Class<T> typeGuard
	
	new(Class<T> typeGuard) {
		this.typeGuard = typeGuard
	}
	 
	def boolean isApplicable(Object domainObject) {
		typeGuard.isInstance(domainObject)	
	}
}

class DiagramMapping<T> extends BaseMapping<T> {
	
	List<AbstractNodeMappingCall<T>> nodes = newArrayList 
	List<AbstractConnectionMappingCall<T>> connections = newArrayList()
	(XtextDomainObjectDescriptor<T>)=>XDiagram createDiagram = [ new XDiagram ]
	
	new(Class<T> typeGuard) {
		super(typeGuard)
	}
	
	def getNodes() { nodes }
	def getConnections() { connections }
	def getCreateDiagram() { createDiagram }
	def setCreateDiagram((XtextDomainObjectDescriptor<T>)=>XDiagram createDiagram) { this.createDiagram = createDiagram }
	
	def <U> addNodeFor(NodeMapping<U> nodeMapping, (T)=>U selector) {
		nodes += new NodeMappingCall(selector, nodeMapping)
	}

	def <U> addConnectionFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		connections += new ConnectionMappingCall(selector, connectionMapping)
	}

	def <U> addNodeForEach(NodeMapping<U> nodeMapping, (T)=>List<? extends U> selector) {
		nodes += new MultiNodeMappingCall(selector, nodeMapping)
	}

	def <U> addConnectionForEach(ConnectionMapping<U> connectionMapping, (T)=>List<? extends U> selector) {
		connections += new MultiConnectionMappingCall(selector, connectionMapping)
	}
}

class NodeMapping<T> extends BaseMapping<T> {
	
	List<AbstractConnectionMappingCall<T>> outgoing = newArrayList
	List<AbstractConnectionMappingCall<T>> incoming = newArrayList()
	(XtextDomainObjectDescriptor<T>)=>XNode createNode = [ new SimpleNode(it) ]
	
	new(Class<T> typeGuard) {
		super(typeGuard)
	}
	
	def getOutgoing() { outgoing }
	def getIncoming() { incoming }
	def getCreateNode() { createNode }
	def setCreateNode((XtextDomainObjectDescriptor<T>)=>XNode createNode) { this.createNode = createNode }
	
	def <U> addOutgoingFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		outgoing += new ConnectionMappingCall(selector, connectionMapping)
	}
	
	def <U> addIncomingFor(ConnectionMapping<U> connectionMapping, (T)=>U selector) {
		incoming += new ConnectionMappingCall(selector, connectionMapping)
	}
	
	def <U> addOutgoingForEach(ConnectionMapping<U> connectionMapping, (T)=>List<? extends U> selector) {
		outgoing += new MultiConnectionMappingCall(selector, connectionMapping)
	}
	
	def <U> addIncomingForEach(ConnectionMapping<U> connectionMapping, (T)=>List<? extends U> selector) {
		incoming += new MultiConnectionMappingCall(selector, connectionMapping)
	}
}

class ConnectionMapping<T> extends BaseMapping<T> {

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
	def <U> setSource(NodeMapping<U> nodeMapping, (T)=>U selector) {
		source = new NodeMappingCall(selector, nodeMapping)
	}

	def <U> setTarget(NodeMapping<U> nodeMapping, (T)=>U selector) {
		target = new NodeMappingCall(selector, nodeMapping)
	}
}

