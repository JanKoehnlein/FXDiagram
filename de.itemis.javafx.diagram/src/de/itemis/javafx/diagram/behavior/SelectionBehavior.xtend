package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.XNode
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.input.MouseEvent

class SelectionBehavior extends AbstractBehavior {
	
	BooleanProperty isSelected = new SimpleBooleanProperty
	
	Effect selectionEffect
	
	boolean wasSelected
	
	new(XNode host) {
		super(host)
		selectionEffect = new DropShadow() => [
			offsetX = 5.0
			offsetY = 5.0
		]
	}
	
	override activate() {
		host.node.onMousePressed = [
			mousePressed
		]
		host.node.onMouseReleased = [
			if(shortcutDown)
				isSelected.set(!wasSelected)
		]
		val ChangeListener<Boolean> selectionListener = [
			observable, oldValue, newValue |
			if(newValue) 
				host.effect = selectionEffect
			else
				host.effect = null
		]
		selectedProperty.addListener(selectionListener)
	}	
	
	def mousePressed(MouseEvent it) {
		wasSelected = isSelected.get
		isSelected.set(true)
	}
	
	def getSelectedProperty() {
		isSelected
	}
	
	def isSelected() {
		selectedProperty.get
	}

	def setSelected(boolean isSelected) {
		selectedProperty.set(isSelected)
	}
}

