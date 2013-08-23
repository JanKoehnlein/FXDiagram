package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import de.fxdiagram.core.behavior.MoveBehavior

abstract class XShape extends Parent implements XActivatable {

	@FxProperty@ReadOnly Node node

	@FxProperty boolean selected
	
	@FxProperty@ReadOnly boolean isActive
	
	protected def setNode(Node node) {
		nodeProperty.set(node)
		children.setAll(node)
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
		selectedProperty.addListener [
			property, oldVlaue, newValue |
			selectionFeedback(newValue)
			if(newValue)
				toFront
		]
	}
	
	def void selectionFeedback(boolean isSelected) {
	}
	
	protected def void doActivate()
	
	def boolean isSelectable() {
		isActive
	}
	
	def select(MouseEvent it) {
		selected = true
	}

	def toggleSelect(MouseEvent it) {
		if (shortcutDown) {
			selected = !selected
		}
	}
	
	def getSnapBounds() {
		boundsInLocal
	}
	
	
	def MoveBehavior<? extends XShape> getMoveBehavior()
}