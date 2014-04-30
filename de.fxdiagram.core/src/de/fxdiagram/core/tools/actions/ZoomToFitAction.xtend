package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import eu.hansolo.enzo.radialmenu.Symbol
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.viewport.ViewportTransition
import de.fxdiagram.core.command.ViewportCommand

class ZoomToFitAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && !shiftDown && code == KeyCode.F
	}
	
	override getSymbol() {
		Symbol.Type.ZOOM_IN
	}

	override perform(XRoot root) {
		val ViewportCommand command = [
			val elements = 
				if(root.currentSelection.empty) 
			  		root.diagram.nodes + root.diagram.connections
			  	else
			  		root.currentSelection
			val selectionBounds = elements.map[localToRootDiagram(snapBounds)].reduce[a,b|a+b]
			if(selectionBounds != null && selectionBounds.width > EPSILON && selectionBounds.height > EPSILON) {
				val targetScale =  
						min(root.scene.width / selectionBounds.width, 
							root.scene.height / selectionBounds.height)
				new ViewportTransition(root, selectionBounds.center, targetScale, 0) 
			} else {
				null
			}
		]
		root.commandStack.execute(command)
	}
}