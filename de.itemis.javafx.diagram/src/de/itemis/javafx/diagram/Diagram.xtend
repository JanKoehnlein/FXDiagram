package de.itemis.javafx.diagram

import java.util.List
import javafx.scene.Group
import javafx.scene.Node

class Diagram {
	
	Group rootPane = new Group()
	
	Group nodeLayer = new Group()
	
	Group connectionLayer = new Group();

	Group buttonLayer = new Group();

	List<ShapeContainer> shapes = newArrayList()
	
	List<Connection> connections = newArrayList()
	
	new() {
		rootPane.children += nodeLayer
		rootPane.children += connectionLayer
		rootPane.children += buttonLayer
	}
	 	 
	def addShape(ShapeContainer shape) {
		nodeLayer.children += shape
		shape.diagram = this
		shapes += shape
	}
	
	def addConnection(Connection connection) {
		connectionLayer.children += connection
		connections += connection
	}
	
	def addButton(Node button) {
		buttonLayer.children += button
	}
	
	def getShapes() {
		shapes
	}
	
	def getConnections() {
		connections
	}
	
	def getRootPane() {
		rootPane
	}
}