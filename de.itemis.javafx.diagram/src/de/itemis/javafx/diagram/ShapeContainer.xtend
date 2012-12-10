package de.itemis.javafx.diagram

import de.itemis.javafx.diagram.behavior.AddRapidButtonBehavior
import javafx.beans.value.ChangeListener
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.layout.BorderPane
import de.itemis.javafx.diagram.behavior.SelectionBehavior

class ShapeContainer extends Group {
	
	Node node
	
	Diagram diagram
	
	Effect selectionEffect 
	
	AnchorPoints anchorPoints
	
	SelectionBehavior selectionBehavior
	
	AddRapidButtonBehavior rapidButtonBehavior 
	
	def setNode(Node node) {
		this.node = node
		children += node		
		BorderPane::setMargin(node, new Insets(3,3,3,3))
		val ChangeListener<Boolean> selectionListener = [
			observable, oldValue, newValue |
			if(newValue) 
				effect = selectionEffect
			else
				effect = null
		]
		selectionBehavior = new SelectionBehavior(this)
		selectionBehavior.selectedProperty.addListener(selectionListener)
		anchorPoints = new AnchorPoints(this)
		rapidButtonBehavior = new AddRapidButtonBehavior(this)
	}


	def setDiagram(Diagram diagram) {
		this.diagram = diagram
		selectionBehavior.activate(diagram)
		rapidButtonBehavior.activate(diagram)
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
	
	def getAnchorPoints() {
		anchorPoints
	}	
	
	def getNode() {
		node
	}
}



