package de.itemis.javafx.diagram.behavior

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import de.itemis.javafx.diagram.ShapeContainer
import javafx.scene.input.MouseEvent
import de.itemis.javafx.diagram.Diagram

class SelectionBehavior extends AbstractBehavior {
	
	BooleanProperty isSelected = new SimpleBooleanProperty
	
	DragContext dragContext
	
	new(ShapeContainer host) {
		super(host)
	}
	
	override activate(Diagram diagram) {
		host.node.onMousePressed = [
			mousePressed
			isSelected.set(true)
		]
		host.node.onMouseReleased = [
			if(dragContext.mouseAnchorX == screenX
					&& dragContext.mouseAnchorY == screenY 
					&& shortcutDown)
				isSelected.set(!dragContext.wasSeleceted)
		]
		host.node.onMouseDragged = [
			mouseDragged	
		]
	}	
	
	def mousePressed(MouseEvent it) {
		dragContext = new DragContext(screenX, screenY, host.translateX, host.translateY, isSelected.get)
	}
	
	def mouseDragged(MouseEvent it) {
		host.translateX = dragContext.initialX - dragContext.mouseAnchorX + screenX
		host.translateY = dragContext.initialY - dragContext.mouseAnchorY + screenY
	}
	
	def getSelectedProperty() {
		isSelected
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