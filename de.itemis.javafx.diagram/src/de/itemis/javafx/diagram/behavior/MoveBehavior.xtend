package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.XNode
import javafx.scene.input.MouseEvent

class MoveBehavior extends AbstractBehavior {
	
	DragContext dragContext
	
	new(XNode host) {
		super(host)
	}
	
	override activate() {
		host.node.onMousePressed = [
			mousePressed  
		]
		host.node.onMouseDragged = [
			mouseDragged	
		]
	}
	
	def mousePressed(MouseEvent it) {
		dragContext = new DragContext(screenX, screenY, host.layoutX, host.layoutY)
	}
	
	def mouseDragged(MouseEvent it) {
		host.relocate(
			dragContext.initialX - dragContext.mouseAnchorX + screenX,
			dragContext.initialY - dragContext.mouseAnchorY + screenY)
	}
}

@Data 
class DragContext {
	double mouseAnchorX 
	double mouseAnchorY
	double initialX
	double initialY
}