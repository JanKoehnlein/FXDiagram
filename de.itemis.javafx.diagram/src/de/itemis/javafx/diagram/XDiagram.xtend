package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.behavior.RapidButton
import java.util.List
import javafx.scene.Group
import de.itemis.javafx.diagram.tools.ZoomTool
import de.itemis.javafx.diagram.tools.SelectionTool

class XDiagram {
	
	Group rootPane = new Group()
	
	Group nodeLayer = new Group()
	
	Group connectionLayer = new Group();

	Group buttonLayer = new Group();

	List<XNode> nodes = newArrayList()
	
	List<XConnection> connections = newArrayList()

	List<RapidButton> buttons = newArrayList()
	
	boolean isActive
	
	new() {
		rootPane.children += nodeLayer
		rootPane.children += connectionLayer
		rootPane.children += buttonLayer
	}
	
	def addNode(XNode node) {
		nodeLayer.children += node
		nodes += node
		node.diagram = this
		if(isActive)
			node.activate
	}
	
	def addConnection(XConnection connection) {
		connectionLayer.children += connection
		connections += connection
		if(isActive)
			connection.activate
	}
		
	def addButton(RapidButton button) {
		buttonLayer.children += button
		buttons += button
		if(isActive)
			button.activate
	}
	
	def activate() {
		(nodes + connections + buttons).forEach[activate()]
		new ZoomTool(this)   
		new SelectionTool(this)     
		isActive = true
	}
	 	 
	def getShapes() {
		nodes
	}
	
	def getConnections() {
		connections
	}
	
	def getRootPane() {
		rootPane
	}
}