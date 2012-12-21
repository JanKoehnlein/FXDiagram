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
		dragContext = new DragContext(screenX, screenY, host.translateX, host.translateY)
	}
	
	def mouseDragged(MouseEvent it) {
		host.translateX = dragContext.initialX - dragContext.mouseAnchorX + screenX
		host.translateY = dragContext.initialY - dragContext.mouseAnchorY + screenY
	}
}

@Data 
class DragContext {
	double mouseAnchorX 
	double mouseAnchorY
	double initialX
	double initialY
}