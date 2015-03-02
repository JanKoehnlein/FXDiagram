package de.fxdiagram.core.tools

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.services.ImageCache
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.ImageCursor
import javafx.scene.input.MouseEvent
import org.eclipse.xtend.lib.annotations.Accessors

import static de.fxdiagram.core.extensions.Point2DExtensions.*
import static de.fxdiagram.core.tools.DiagramMouseTool.State.*

class DiagramMouseTool implements XDiagramTool {
	XRoot root

	DragContext dragContext

	EventHandler<MouseEvent> pressedHandler
	EventHandler<MouseEvent> dragHandler
	EventHandler<MouseEvent> releasedHandler

	static val ZOOM_SENSITIVITY = 30
	
	boolean hasDragged = false

	State currentState

	static val zoomInCursor = new ImageCursor(ImageCache.get.getImage(DiagramMouseTool, 'zoom_in.png'))
	static val zoomOutCursor = new ImageCursor(ImageCache.get.getImage(DiagramMouseTool, 'zoom_out.png'))
	
	new(XRoot root) {
		this.root = root
		pressedHandler = [ event |
			hasDragged = false
			event.applyToState
			event.consume
		]
		dragHandler = [
			if(dragContext != null) {
				hasDragged = true
				if(applyToState) 
					return;
				if(currentState == SCROLL) {
					root.viewportTransform.translateX = dragContext.sceneX + sceneX 
					root.viewportTransform.translateY = dragContext.sceneY + sceneY 
				} else {
					var totalZoomFactor = 1 + norm(screenX - dragContext.screenX,
						screenY - dragContext.screenY) / ZOOM_SENSITIVITY
					if(currentState == ZOOM_OUT) 
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
			currentState = null
			root.scene.cursor = Cursor.DEFAULT
		]
	}
	
	protected def applyToState(MouseEvent event) {
		val newState = if(!event.isShortcutDown)
				SCROLL
			else if(event.isShiftDown) 
				ZOOM_OUT
			else
				ZOOM_IN
		if(currentState != newState) {
			currentState = newState
			event.startDragContext
			root.scene.cursor = switch newState {
				case SCROLL: Cursor.OPEN_HAND
				case ZOOM_IN: zoomInCursor
				case ZOOM_OUT: zoomOutCursor 
			}	
			return true
		} else {
			return false
		}
	}
	
	protected def startDragContext(MouseEvent event) {
		dragContext = new DragContext => [
			sceneX = root.viewportTransform.translateX - event.sceneX
			sceneY = root.viewportTransform.translateY - event.sceneY
			screenX = event.screenX
			screenY = event.screenY
			pivotInDiagram = root.diagram.sceneToLocal(event.sceneX, event.sceneY)
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
	
	enum State {
		SCROLL, ZOOM_IN, ZOOM_OUT
	}
}