package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.behavior.SelectionBehavior
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import de.fxdiagram.annotations.properties.Lazy

class XNode extends Group implements XActivatable {
	
	Node node
	
	@FxProperty@ReadOnly boolean isActive
	
	Effect mouseOverEffect
	Effect originalEffect
	
	SelectionBehavior selectionBehavior
	MoveBehavior moveBehavior
	AnchorPoints anchorPoints
	
	@FxProperty@Lazy double width 
	@FxProperty@Lazy double height
	
	new() {
		mouseOverEffect = createMouseOverEffect 
	}
	
	protected def createMouseOverEffect() {
		new InnerShadow
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
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
		switch node { XActivatable: node.activate }
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
	
	override minWidth(double height) {
		if(widthProperty != null)
			widthProperty.get
		else 
			super.minWidth(height)
	}
	
	override minHeight(double width) {
		if(heightProperty != null)
			heightProperty.get
		else
			super.minHeight(width)
	}
	
	override prefWidth(double height) {
		if(widthProperty != null)
			widthProperty.get
		else 
			super.prefWidth(height)
	}
	
	override prefHeight(double width) {
		if(heightProperty != null)
			heightProperty.get
		else
			super.prefHeight(width)
	}
	
	override maxWidth(double height) {
		if(widthProperty != null)
			widthProperty.get
		else 
			super.maxWidth(height)
	}
	
	override maxHeight(double width) {
		if(heightProperty != null)
			heightProperty.get
		else
			super.maxHeight(width)
	}
	
}



