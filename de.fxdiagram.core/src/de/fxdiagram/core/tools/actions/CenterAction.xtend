package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.ViewportCommand
import de.fxdiagram.core.viewport.ViewportTransition
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class CenterAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.C
	}
	
	override getSymbol() {
		SymbolType.SELECTION2
	}
	
	override getTooltip() {
		'Center selection'
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
				val targetScale = min(1, 
						min(root.scene.width / selectionBounds.width, 
							root.scene.height / selectionBounds.height))
				return new ViewportTransition(root, selectionBounds.center, targetScale)
			} else {
				return null
			}
		]  
		root.commandStack.execute(command)
	}
}