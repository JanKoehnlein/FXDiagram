package de.fxdiagram.core.debug

import de.fxdiagram.core.XRootDiagram
import javafx.beans.value.ChangeListener
import javafx.geometry.Bounds
import javafx.scene.Node

class Debug {
	
	def static debugTranslation(Node node) {
		val ChangeListener<Number> debugger = [
			element, oldVal, newVal |
			new Exception('''
				Position changed from «oldVal» to «newVal»''').printStackTrace
		]
		var currentNode = node
		while(currentNode != null && !(currentNode instanceof XRootDiagram)) {
			currentNode.layoutXProperty.addListener(debugger)
			currentNode.layoutYProperty.addListener(debugger)
			currentNode.translateXProperty.addListener(debugger)
			currentNode.translateYProperty.addListener(debugger)
			currentNode = currentNode.parent
		}
	}
	
	def static debugSize(Node node) {
		val ChangeListener<Bounds> debugger = [
			element, oldVal, newVal |
			new Exception('''
				Bounds changed from
					«oldVal»
				to
					«newVal»
			''').printStackTrace
		]
		var currentNode = node
		while(currentNode != null && !(currentNode instanceof XRootDiagram)) {
			currentNode.boundsInLocalProperty.addListener(debugger)
//			currentNode.boundsInParentProperty.addListener(debugger)
			currentNode = currentNode.parent
		}
	}
}