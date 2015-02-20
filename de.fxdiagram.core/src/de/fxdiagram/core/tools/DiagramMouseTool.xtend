package de.fxdiagram.core.tools

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.services.ImageCache
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.ImageCursor
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import org.eclipse.xtend.lib.annotations.Accessors

import static de.fxdiagram.core.extensions.Point2DExtensions.*

class DiagramMouseTool implements XDiagramTool {
	XRoot root

	DragContext dragContext

	EventHandler<MouseEvent> pressedHandler
	EventHandler<MouseEvent> dragHandler
	EventHandler<MouseEvent> releasedHandler

	static val ZOOM_SENSITIVITY = 30
	
	boolean hasDragged = false

	static val zoomInCursor = new ImageCursor(ImageCache.get.getImage(DiagramMouseTool, '../images/zoom_in.png'))
	static val zoomOutCursor = new ImageCursor(ImageCache.get.getImage(DiagramMouseTool, '../images/zoom_out.png'))
	
	new(XRoot root) {
		this.root = root
		pressedHandler = [ event |
			dragContext = new DragContext => [
				sceneX = root.viewportTransform.translateX - event.sceneX
				sceneY = root.viewportTransform.translateY - event.sceneY
				screenX = event.screenX
				screenY = event.screenY
				pivotInDiagram = root.diagram.sceneToLocal(event.sceneX, event.sceneY)
			]
			if(event.button == MouseButton.PRIMARY) 
				root.scene.cursor = Cursor.OPEN_HAND
			else if(event.shortcutDown) 
				root.scene.cursor = zoomOutCursor
			else 
				root.scene.cursor = zoomInCursor
			hasDragged = false
			event.consume
		]
		dragHandler = [
			if(dragContext != null) {
				hasDragged = true
				if(button == MouseButton.PRIMARY) {
					root.viewportTransform.translateX = dragContext.sceneX + sceneX 
					root.viewportTransform.translateY = dragContext.sceneY + sceneY 
				} else {
					var totalZoomFactor = 1 + norm(screenX - dragContext.screenX,
						screenY - dragContext.screenY) / ZOOM_SENSITIVITY
					if(shortcutDown) 
						totalZoomFactor = 1 / totalZoomFactor	
					val scale = totalZoomFactor / dragContext.previousScale
					root.viewportTransform.scaleRelative(scale)
					val pivotInScene = root.diagram.localToScene(dragContext.pivotInDiagram)
					root.viewportTransform.translateRelative(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
					dragContext.previousScale = totalZoomFactor
				}
				consume
			}
		]
		releasedHandler = [
			if(hasDragged) {
				hasDragged = false
				dragContext = null
				consume
			}
			root.scene.cursor = Cursor.DEFAULT
		]
	}

	override activate() {
		val scene = root.scene
		scene.addEventHandler(MouseEvent.MOUSE_PRESSED, pressedHandler)
		scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler)
		root.addEventHandler(MouseEvent.MOUSE_RELEASED, releasedHandler)
		true
	}

	override deactivate() {
		val scene = root.scene
		scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressedHandler)
		scene.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler)
		root.removeEventHandler(MouseEvent.MOUSE_RELEASED, releasedHandler)
		true
	}
	
	static class DragContext {
		@Accessors double sceneX
		@Accessors double sceneY
		@Accessors double screenX
		@Accessors double screenY
		@Accessors double previousScale = 1
		@Accessors Point2D pivotInDiagram
	}
}