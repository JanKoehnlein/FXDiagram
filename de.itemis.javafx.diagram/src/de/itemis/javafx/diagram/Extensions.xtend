package de.itemis.javafx.diagram

import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.geometry.Bounds
import javafx.scene.input.MouseEvent

class Extensions {
	def localToRoot(Node node, double x, double y) {
		node.scene.root.parentToLocal(node.localToScene(x, y))
	}	
	
	def localToRoot(Node node, Point2D point) {
		node.scene.root.parentToLocal(node.localToScene(point))
	}	
	
	def localToRoot(Node node, Bounds bounds) {
		node.scene.root.parentToLocal(node.localToScene(bounds))
	}	
	
	def getTargetShape(MouseEvent event) {
		if(event.target instanceof Node) 
			getContainerShape(event.target as Node)
		else
			null
	}
	
	def XNode getContainerShape(Node it) {
		switch it {
			case null: 
				null
			XNode:
				it
			default: getContainerShape(parent)
		}
	}
}