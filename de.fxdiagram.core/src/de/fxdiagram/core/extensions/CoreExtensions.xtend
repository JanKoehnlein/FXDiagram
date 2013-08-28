package de.fxdiagram.core.extensions

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform

import static extension de.fxdiagram.core.extensions.TransformExtensions.*

class CoreExtensions {

	def static isRootDiagram(Node node) {
		switch node { 
			XDiagram: return node.isRootDiagram
		} 
		false
	}

	def static localToRootDiagram(Node node, double x, double y) {
		localToRootDiagram(node, new Point2D(x, y))
	}

	def static Point2D localToRootDiagram(Node node, Point2D point) {
		switch node {
			case null: return null
			XDiagram: if(node.isRootDiagram) return point
		}
		localToRootDiagram(node.parent, node.localToParent(point))
	}

	def static Bounds localToRootDiagram(Node node, Bounds bounds) {
		switch node {
			case null: return null
			XDiagram: if(node.isRootDiagram) return bounds
		}
		localToRootDiagram(node.parent, node.localToParent(bounds))
	}

	def static localToDiagram(Node node, double x, double y) {
		localToDiagram(node, new Point2D(x, y))
	}

	def static Point2D localToDiagram(Node node, Point2D point) {
		switch node {
			case null: null
			XDiagram: point
			default: localToDiagram(node.parent, node.localToParent(point))
		}
	}

	def static Bounds localToDiagram(Node node, Bounds bounds) {
		switch node {
			case null: null
			XDiagram: bounds
			default: localToDiagram(node.parent, node.localToParent(bounds))
		}
	}

	def static Transform localToDiagramTransform(Node node) {
		val transform = new Affine
		var currentNode = node
		while (currentNode.parent != null) {
			transform.leftMultiply(currentNode.localToParentTransform)
			currentNode = currentNode.parent
			if (currentNode instanceof XDiagram)
				return transform
		}
		null
	}

	def static XDiagram getDiagram(Node it) {
		switch it {
			case null: null
			XDiagram: it
			default: getDiagram(it.parent)
		}
	}

	def static XDiagram getRootDiagram(Node it) {
		switch it {
			case null: null
			XDiagram: if(isRootDiagram) it else getRootDiagram(it.parentDiagram)
			default: getRootDiagram(it.parent)
		}
	}

	def static XRoot getRoot(Node it) {
		switch it {
			case null: null
			XRoot: it
			default: getRoot(it.parent)
		}
	}

	def static getTargetShape(MouseEvent event) {
		if (event.target instanceof Node)
			getContainerShape(event.target as Node)
		else
			null
	}

	def static XShape getContainerShape(Node it) {
		switch it {
			case null: null
			XShape: it
			default: getContainerShape(parent)
		}
	}

	def static getTargetButton(MouseEvent event) {
		if (event.target instanceof Node)
			getContainerButton(event.target as Node)
		else
			null
	}
	
	def static XRapidButton getContainerButton(Node it) {
		switch it {
			case null: null
			XRapidButton: it
			default: getContainerButton(parent)
		}
	}
	
	def static Iterable<? extends Node> getAllChildren(Parent node) {
		node.allChildrenInternal.toSet
	}

	protected def static Iterable<? extends Node> getAllChildrenInternal(Parent node) {
		node.childrenUnmodifiable + node.childrenUnmodifiable.filter(Parent).map[allChildren].flatten
	}

}
