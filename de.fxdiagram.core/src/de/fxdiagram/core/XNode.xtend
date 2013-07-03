package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Lazy
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.behavior.SelectionBehavior
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow

class XNode extends Parent implements XActivatable {
	
	static int instanceCount
	
	@FxProperty@ReadOnly boolean isActive
	@FxProperty@Lazy double width 
	@FxProperty@Lazy double height
	@FxProperty@ReadOnly String key
	
	Node node

	Effect mouseOverEffect
	Effect originalEffect
	
	SelectionBehavior selectionBehavior
	MoveBehavior moveBehavior
	AnchorPoints anchorPoints
	
	
	new() {
		mouseOverEffect = createMouseOverEffect
		key = class.simpleName + instanceCount
		instanceCount = instanceCount + 1
		
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
	
	protected def setKey(String key) {
		keyProperty.set(key)	
	}
	
	def getSelectionBehavior() { selectionBehavior }
	
	def getMoveBehavior() { moveBehavior }
	
	def getAnchorPoints() { anchorPoints }	
	
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



