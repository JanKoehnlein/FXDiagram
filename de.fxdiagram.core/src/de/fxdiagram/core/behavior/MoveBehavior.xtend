package de.fxdiagram.core.behavior

import de.fxdiagram.core.XShape
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

class MoveBehavior extends AbstractBehavior {
	
	DragContext dragContext
	
	new(XShape host) {
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
		dragContext = new DragContext(screenX, screenY, host.parent.localToScene(host.layoutX, host.layoutY))
	}
	
	def mouseDragged(MouseEvent it) {
		val newPositionInScene = new Point2D(
			dragContext.initialPosInScene.x + screenX - dragContext.mouseAnchorX,
			dragContext.initialPosInScene.y + screenY - dragContext.mouseAnchorY)
		val newPositionInDiagram = host.parent.sceneToLocal(newPositionInScene)
		if(newPositionInDiagram != null)
			host.relocate(newPositionInDiagram.x, newPositionInDiagram.y)
	}
}

@Data 
class DragContext {
	double mouseAnchorX 
	double mouseAnchorY
	Point2D initialPosInScene
}