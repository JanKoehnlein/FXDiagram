package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.behavior.AddRapidButtonBehavior
import de.itemis.javafx.diagram.behavior.SelectionBehavior
import javafx.beans.value.ChangeListener
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Light$Distant
import javafx.scene.effect.Lighting

class ShapeContainer extends Group {
	
	Node node
	
	Diagram diagram
	
	DropShadow selectionEffect 
	
	Lighting mouseOverEffect
	
	AnchorPoints anchorPoints
	
	SelectionBehavior selectionBehavior
	
	AddRapidButtonBehavior rapidButtonBehavior 
	
	def void setNode(Node node) {
		this.node = node
		children += node		
		selectionBehavior = new SelectionBehavior(this)
		anchorPoints = new AnchorPoints(this)
		rapidButtonBehavior = new AddRapidButtonBehavior(this)
	}

	def setDiagram(Diagram diagram) {
		this.diagram = diagram
		val ChangeListener<Boolean> selectionListener = [
			observable, oldValue, newValue |
			if(newValue) 
				effect = getSelectionEffect
			else
				effect = null
		]
		selectionBehavior.selectedProperty.addListener(selectionListener)
		selectionBehavior.activate(diagram)
		rapidButtonBehavior.activate(diagram)
		onMouseEntered = [ 
			getSelectionEffect.input = getMouseOverEffect
			effect = if(selected) getSelectionEffect else getMouseOverEffect
		]
		onMouseExited = [ 
			getSelectionEffect.input = null
			effect = if(selected) getSelectionEffect else null
		]
	}
	
	def isSelected() {
		selectionBehavior.selectedProperty.get
	}

	def setSelected(boolean isSelected) {
		selectionBehavior.selectedProperty.set(isSelected)
	}
	
	def getSelectionBehavior() {
		selectionBehavior
	}
	
	def protected getSelectionEffect() {
		if(selectionEffect == null)
			selectionEffect = new DropShadow() => [
				offsetX = 5.0
				offsetY = 5.0
			]
		selectionEffect
	}
	
	def protected getMouseOverEffect() {
		if(mouseOverEffect == null)
 			mouseOverEffect = new Lighting => [
        		light = new Distant => [
        			elevation = 48
        			azimuth = -135
       			]
       			surfaceScale = 0.1
    		]
    	mouseOverEffect
	}
	
	def getAnchorPoints() {
		anchorPoints
	}	
	
	def getNode() {
		node
	}
}



