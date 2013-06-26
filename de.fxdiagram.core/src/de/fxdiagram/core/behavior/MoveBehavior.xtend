package de.fxdiagram.core.behavior

import de.fxdiagram.core.XNode
import javafx.scene.input.MouseEvent
import javafx.geometry.Point2D
import static extension de.fxdiagram.core.Extensions.*

class MoveBehavior extends AbstractBehavior {
	
	DragContext dragContext
	
	new(XNode host) {
		super(host)
	}
	
	override doActivate() {
		host.node.onMousePressed = [
			mousePressed  
		]
		host.node.onMouseDragged = [
			mouseDragged	
		]
	}
	
	def mousePressed(MouseEvent it) {
		dragContext = new DragContext(screenX, screenY, host.diagram.localToScene(host.layoutX, host.layoutY))
	}
	
	def mouseDragged(MouseEvent it) {
		val newPositionInScene = new Point2D(
			dragContext.initialPosInScene.x + screenX - dragContext.mouseAnchorX,
			dragContext.initialPosInScene.y + screenY - dragContext.mouseAnchorY)
		val newPositionInDiagram = host.diagram.sceneToLocal(newPositionInScene)
		host.relocate(newPositionInDiagram.x, newPositionInDiagram.y)
	}
}

@Data 
class DragContext {
	double mouseAnchorX 
	double mouseAnchorY
	Point2D initialPosInScene
}