package de.itemis.javafx.diagram

import java.util.List
import javafx.scene.Group
import javafx.scene.layout.Pane

abstract class XAbstractDiagram extends Pane implements XActivatable {
	
	List<XNode> nodes = newArrayList
	List<XConnection> connections = newArrayList
	List<XRapidButton> buttons = newArrayList
	
	boolean isActive
	
	def Group getNodeLayer()
	def Group getButtonLayer()
	def Group getConnectionLayer()
	
	override activate() {
		if(!isActive)
			doActivate
		isActive = true
	}
	
	def void doActivate() {
		(nodes + connections + buttons).forEach[activate]
	} 
	 	 
	def addNode(XNode node) {
		nodeLayer.children += node
		node.diagram = this
		internalAddNode(node)
		if(isActive)
			node.activate
	}
	
	def internalAddNode(XNode node) {
		nodes += node
	}
	
	def addConnection(XConnection connection) {
		connectionLayer.children += connection
		connection.diagram = this
		internalAddConnection(connection)
		if(isActive)
			connection.activate
	}

	def internalAddConnection(XConnection connection) {
		connections += connection
	}
	
	def addButton(XRapidButton button) {
		buttonLayer.children += button
		button.diagram = this
		internalAddButton(button)
		if(isActive)
			button.activate
	}

	def internalAddButton(XRapidButton button) {
		buttons += button
	}
	
	def getNodes() {
		nodes
	}
	
	def getButtons() {
		buttons
	}
	
	def getConnections() {
		connections
	}
	
	def protected isActive() {
		isActive
	}
}