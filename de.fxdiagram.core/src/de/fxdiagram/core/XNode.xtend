package de.fxdiagram.core

import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.behavior.SelectionBehavior
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow

class XNode extends Group implements XActivatable {
	
	Node node
	
	boolean isActive
	
	Effect mouseOverEffect
	Effect originalEffect
	
	SelectionBehavior selectionBehavior
	MoveBehavior moveBehavior
	AnchorPoints anchorPoints
	
	new() {
		mouseOverEffect = createMouseOverEffect 
	}
	
	protected def createMouseOverEffect() {
		new InnerShadow
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActive = true
	}
	
	def doActivate() {	
		selectionBehavior = new SelectionBehavior(this)
		moveBehavior = new MoveBehavior(this)
		anchorPoints = new AnchorPoints(this)
 		selectionBehavior.activate()
		moveBehavior.activate()
		onMouseEntered = [
			originalEffect = node.effect
			node.effect = mouseOverEffect ?: originalEffect
		]
		onMouseExited = [ 
			node.effect = originalEffect
		]
	}

	def getNode() { node }
	
	def void setNode(Node node) {
		this.node = node
		children += node	
	}
	
	def getSelectionBehavior() { selectionBehavior }
	
	def getMoveBehavior() { moveBehavior }
	
	def getAnchorPoints() { anchorPoints }	
	
	def getKey() {
		toString
	}
}



