package de.itemis.javafx.diagram

import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import java.util.logging.Logger

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
	
	def static XAbstractDiagram getDiagram(Node it) {
		switch it {
			case null: null
			XAbstractDiagram: it
			default: getDiagram(it.parent)
		}
	}	
	
	def static XRootDiagram getRootDiagram(Node it) {
		switch it {
			case null: null
			XRootDiagram: it
			default: getRootDiagram(it.parent)
		}
	}	
	
	def static getTargetNode(MouseEvent event) {
		if(event.target instanceof Node) 
			getContainerNode(event.target as Node)
		else
			null
	}

	def static XNode getContainerNode(Node it) {
		switch it {
			case null: null
			XNode: it
			default: getContainerNode(parent)
		}
	}
	
	def static Logger getLogger(Object it) {
		Logger.getLogger(class.canonicalName)
	}
}