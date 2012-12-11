package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.behavior.AddRapidButtonBehavior
import de.itemis.javafx.diagram.behavior.SelectionBehavior
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.Light$Distant
import javafx.scene.effect.Lighting
import de.itemis.javafx.diagram.behavior.MoveBehavior

class XNode extends Group implements Activateable {
	
	Node node
	
	Lighting mouseOverEffect
	
	AnchorPoints anchorPoints
	
	SelectionBehavior selectionBehavior
	
	MoveBehavior moveBehavior
	
	AddRapidButtonBehavior rapidButtonBehavior
	
	@Property XDiagram diagram
	
	new() {
//		mouseOverEffect = new Lighting => [
//        	light = new Distant => [
//				elevation = 48
//				azimuth = -135
//   			]
//   			surfaceScale = 0.1
//		]
	}
	
	def void setNode(Node node) {
		this.node = node
		children += node		
	}

	override activate() {	
		selectionBehavior = new SelectionBehavior(this)
		moveBehavior = new MoveBehavior(this)
		anchorPoints = new AnchorPoints(this)
		rapidButtonBehavior = new AddRapidButtonBehavior(this)
		selectionBehavior.activate()
		moveBehavior.activate()
		rapidButtonBehavior.activate()
		onMouseEntered = [ 
			node.effect = mouseOverEffect
		]
		onMouseExited = [ 
			node.effect = null
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



