package de.itemis.javafx.diagram

import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.MouseEvent

class Extensions {
	
	def static localToRoot(Node node, double x, double y) {
		node.scene.root.parentToLocal(node.localToScene(x, y))
 	}	
 	
	def static localToRoot(Node node, Point2D point) {
		node.scene.root.parentToLocal(node.localToScene(point))
 	}	
 	
	def static localToRoot(Node node, Bounds bounds) {
		node.scene.root.parentToLocal(node.localToScene(bounds))
 	}	
	
	def static localToDiagram(Node node, double x, double y) {
		localToDiagram(node, new Point2D(x, y))
	}	
	
	def static Point2D localToDiagram(Node node, Point2D point) {
		switch node {
			case null: null  
			XAbstractDiagram: point
			default: localToDiagram(node.parent, node.localToParent(point))
		} 
	}	
	
	def static Bounds localToDiagram(Node node, Bounds bounds) {
		switch node {
			case null: null  
			XAbstractDiagram: bounds
			default: localToDiagram(node.parent, node.localToParent(bounds))
		} 
	}	
	
	def static getTargetShape(MouseEvent event) {
		if(event.target instanceof Node) 
			getContainerShape(event.target as Node)
		else
			null
	}

	def static XRootDiagram getRootDiagram(Object it) {
		switch it {
			case null: null
			XNestedDiagram: getRootDiagram(it.parentDiagram)
			XRootDiagram: it
			Node: getRootDiagram(it.parent)
			default: null
		}
	}	
	
	def static XNode getContainerShape(Node it) {
		switch it {
			case null: null
			XNode: it
			default: getContainerShape(parent)
		}
	}
}