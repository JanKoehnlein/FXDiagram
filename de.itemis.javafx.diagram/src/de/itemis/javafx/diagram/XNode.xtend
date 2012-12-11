package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.behavior.MoveBehavior
import de.itemis.javafx.diagram.behavior.SelectionBehavior
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow

class XNode extends Group implements Activateable {
	
	Node node
	
	Effect mouseOverEffect
	
	Effect originalEffect
	
	AnchorPoints anchorPoints
	
	SelectionBehavior selectionBehavior
	
	MoveBehavior moveBehavior
	
	
	@Property XDiagram diagram
	
	new() {
		mouseOverEffect = new InnerShadow
	}
	
	def void setNode(Node node) {
		this.node = node
		children += node		
	}

	override activate() {	
		selectionBehavior = new SelectionBehavior(this)
		moveBehavior = new MoveBehavior(this)
		anchorPoints = new AnchorPoints(this)
		selectionBehavior.activate()
		moveBehavior.activate()
		onMouseEntered = [ 
			originalEffect = node.effect
			node.effect = mouseOverEffect
		]
		onMouseExited = [ 
			node.effect = originalEffect
		]
	}
	
	def getSelectionBehavior() {
		selectionBehavior
	}
	
	def getMoveBehavior() {
		moveBehavior
	}
	
	def getAnchorPoints() {
		anchorPoints
	}	
	
	def getNode() {
		node
	}
}



