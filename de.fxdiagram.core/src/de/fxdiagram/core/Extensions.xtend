package de.fxdiagram.core

import java.util.logging.Logger
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform

import static extension de.fxdiagram.core.geometry.TransformExtensions.*

class Extensions {

	def static localToRootDiagram(Node node, double x, double y) {
		localToRootDiagram(node, new Point2D(x, y))
	}

	def static Point2D localToRootDiagram(Node node, Point2D point) {
		switch node {
			case null: null
			XRootDiagram: point
			default: localToRootDiagram(node.parent, node.localToParent(point))
		}
	}

	def static Bounds localToRootDiagram(Node node, Bounds bounds) {
		switch node {
			case null: null
			XRootDiagram: bounds
			default: localToRootDiagram(node.parent, node.localToParent(bounds))
		}
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

	def static Transform localToDiagramTransform(Node node) {
		val transform = new Affine
		var currentNode = node
		while (currentNode.parent != null) {
			transform.leftMultiply(currentNode.localToParentTransform)
			currentNode = currentNode.parent
			if (currentNode instanceof XAbstractDiagram)
				return transform
		}
		null
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

	def static Logger getLogger(Object it) {
		Logger.getLogger(class.canonicalName)
	}
}
