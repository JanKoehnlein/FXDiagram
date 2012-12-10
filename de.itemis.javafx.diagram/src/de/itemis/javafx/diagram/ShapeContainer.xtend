package de.itemis.javafx.diagram

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.input.MouseEvent
import org.eclipse.xtend.lib.Data

class ShapeContainer extends Group {
	
	Node node
	
	BooleanProperty isSelected = new SimpleBooleanProperty
	
	Effect selectionEffect 
	
	AnchorPoints anchorPoints
	
	DragContext dragContext
	
	RapidButton rapidButton = new RapidButton('R')
	
	def setNode(Node node) {
		this.node = node
		this.children += node
		this.children += rapidButton
		val ChangeListener<Boolean> selectionListener = [
			observable, oldValue, newValue |
			if(newValue) 
				effect = getSelectionEffect
			else
				effect = null
		]
		isSelected.addListener(selectionListener)
		node.onMousePressed = [
			mousePressed
			isSelected.set(true)
		]
		node.onMouseReleased = [
			if(dragContext.mouseAnchorX == screenX
					&& dragContext.mouseAnchorY == screenY 
					&& shortcutDown)
				isSelected.set(!dragContext.wasSeleceted)
		]
		node.onMouseDragged = [
			mouseDragged	
		]
		node.onMouseEntered = [
			rapidButton.show
		]
		node.onMouseExited = [
			rapidButton.fade
		]
		anchorPoints = new AnchorPoints(this)
	}
	
	def getSelectedProperty() {
		isSelected
	}
	
	def isSelected() {
		isSelected.get
	}

	def setSelected(boolean isSelected) {
		this.isSelected.set(isSelected)
	}
	
	def protected getSelectionEffect() {
		if(selectionEffect == null)
			selectionEffect = new DropShadow() => [
				offsetX = 5.0
				offsetY = 5.0
			]
		selectionEffect
	}
	
	def mousePressed(MouseEvent it) {
		dragContext = new DragContext(screenX, screenY, translateX, translateY, isSelected.get)
	}
	
	def mouseDragged(MouseEvent it) {
		translateX = dragContext.initialX - dragContext.mouseAnchorX + screenX
		translateY = dragContext.initialY - dragContext.mouseAnchorY + screenY
	}
	
	def getAnchorPoints() {
		anchorPoints
	}	
	
	def getNode() {
		node
	}
}

@Data 
class DragContext {
	double mouseAnchorX 
	double mouseAnchorY
	double initialX
	double initialY
	boolean wasSeleceted 
}