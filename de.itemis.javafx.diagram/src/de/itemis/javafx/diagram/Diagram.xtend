package de.itemis.javafx.diagram

import java.util.List
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.input.MouseEvent

class Diagram {
	
	Group rootPane = new Group()
	
	Group nodeLayer = new Group()
	
	Group connectionLayer = new Group();

	List<ShapeContainer> shapes = newArrayList()
	
	List<Connection> connections = newArrayList()
	 	 
	new() {
		rootPane.children += nodeLayer
		rootPane.children += connectionLayer
	}
	 	 
	def addShape(ShapeContainer shape) {
		nodeLayer.children += shape
		shapes += shape
		rootPane.addEventFilter(MouseEvent::MOUSE_PRESSED) [
			val targetShape = getTargetShape(it)
			if(targetShape == null || (!targetShape.selected && !shortcutDown))
				shapes.filter[selected].forEach[selected = false]				
			for(s: shapes.filter[selected]) {
				s.mousePressed(it)				
			}
		]
		rootPane.addEventFilter(MouseEvent::MOUSE_DRAGGED) [
			for(s: shapes.filter[selected]) {
				s.mouseDragged(it)				
			}
		]
	}
	
	def protected getTargetShape(MouseEvent event) {
		if(event.target instanceof Node) 
			getContainerShape(event.target as Node)
		else
			null
	}
	
	def protected ShapeContainer getContainerShape(Node it) {
		if(it == null)
			null
		else if(shapes.contains(it)) 
			it as ShapeContainer
		else 
			getContainerShape(parent)
	}
	
	def addConnection(Connection connection) {
		connectionLayer.children += connection
		connections += connection
	}
	
	def getRootPane() {
		rootPane
	}
}