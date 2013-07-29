package de.fxdiagram.core.debug

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XRootDiagram
import javafx.beans.value.ChangeListener
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node

@Logging
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
			currentNode.layoutBoundsProperty.addListener(debugger)
			currentNode = currentNode.parent
		}
	}
	
	def static dumpLayout(Node it) {
		LOG.info('''Layout of «class.simpleName»: («layoutX»:«layoutY») «layoutBounds»''')
	}

	def static dumpBounds(Node it) {
		var message = '''
			Bounds of «class.simpleName»:
				(«layoutX»:«layoutY») «layoutBounds»
		 '''
		 var current = it
		 var currentPosition = new Point2D(layoutX, layoutY)
		 var currentBounds = layoutBounds
		 while(current.parent != null) {
		 	currentBounds = current.localToParent(currentBounds)
		 	currentPosition = current.localToParent(currentPosition)
		 	message = message + '''
		 		«null»	in «current.parent.class.simpleName»: («currentPosition.x»:«currentPosition.y») «currentBounds»
		 	'''
		 	current = current.parent
		 }
		 LOG.info(message)
	}
}