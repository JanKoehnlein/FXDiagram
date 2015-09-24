package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.command.ViewportCommand
import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransition
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class RevealAction implements DiagramAction {
	
	Iterable<? extends XShape> nodes
	
	new(Iterable<? extends XShape> nodes) {
		this.nodes = nodes
	}
	
	new() {
	}
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.R
	}
	
	override getSymbol() {
		null
	}
	
	override getTooltip() {
		'Reveal selection'
	}
	
	protected def getBoundsInScene(XRoot root) {
		val theNodes = nodes ?: 
			if(root.currentSelection.empty) 
		  		root.diagram.nodes + root.diagram.connections
			else
		  		root.currentSelection
		return theNodes.map[localToScene(boundsInLocal)].reduce[$0+$1]
	}
	
	override perform(XRoot root) {
		val ViewportCommand command = [
			val boundsInScene = getBoundsInScene(root)
			if(boundsInScene == null) 
				return null
			val scene = root.scene
			if(scene.width < boundsInScene.width || scene.height < boundsInScene.height) {
				// diagram smaller than bounds => scale to fit
				val boundsInDiagram = root.diagram.sceneToLocal(boundsInScene)
				if(boundsInDiagram.width > EPSILON && boundsInDiagram.height > EPSILON) {
					val targetScale =  
							min(scene.width / boundsInDiagram.width, 
								scene.height / boundsInDiagram.height)
					return new ViewportTransition(root, boundsInDiagram.center, targetScale , 0)
				} else {
					return null
				} 
			}
			val deltaX = if(boundsInScene.minX < 0)
					-boundsInScene.minX
				else if(boundsInScene.maxX > scene.width)
					scene.width - boundsInScene.maxX
			val deltaY = if(boundsInScene.minY < 0)
					-boundsInScene.minY
				else if(boundsInScene.maxY > scene.height)
					scene.height - boundsInScene.maxY 
			val currentViewport = root.diagram.viewportTransform
			val targetViewport = new ViewportMemento(
				currentViewport.translateX + deltaX, 
				currentViewport.translateY + deltaY,
				currentViewport.scale,
				currentViewport.rotate)
			return new ViewportTransition(root, targetViewport, 500.millis)
		]
		root.commandStack.execute(command)
	}
}