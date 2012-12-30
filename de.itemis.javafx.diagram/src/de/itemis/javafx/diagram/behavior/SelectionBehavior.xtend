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
			offsetX = 4.0
			offsetY = 4.0
		]
	}
	
	override doActivate() {
		host.onMousePressed = [
			mousePressed
		]
		host.onMouseReleased = [
			if(shortcutDown)
				isSelected.set(!wasSelected)
		]
		val ChangeListener<Boolean> selectionListener = [
			observable, oldValue, newValue |
			if(newValue) {
				host.effect = selectionEffect
				host.scaleX = 1.05
				host.scaleY = 1.05
			} else {
				host.effect = null
				host.scaleX = 1.0
				host.scaleY = 1.0
			}
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

