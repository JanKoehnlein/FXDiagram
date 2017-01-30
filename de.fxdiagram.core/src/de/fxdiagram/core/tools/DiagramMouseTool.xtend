package de.fxdiagram.core.tools

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.services.ImageCache
import java.util.Set
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.ImageCursor
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.StrokeLineCap
import org.eclipse.xtend.lib.annotations.Accessors

import static de.fxdiagram.core.extensions.Point2DExtensions.*
import static de.fxdiagram.core.tools.DiagramMouseTool.State.*
import static javafx.scene.input.MouseButton.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

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

	Rectangle marquee = new Rectangle
	Set<XShape> previousSelection

	new(XRoot root) {
		this.root = root
		pressedHandler = [ event |
			hasDragged = false
			event.applyToState
			event.consume
		]
		dragHandler = [
			if (dragContext != null) {
				hasDragged = true
				if (applyToState)
					return;
				switch currentState {
					case SCROLL: {
						root.viewportTransform.translateX = dragContext.sceneX + sceneX
						root.viewportTransform.translateY = dragContext.sceneY + sceneY
					}
					case ZOOM_IN,
					case ZOOM_OUT: {
						var totalZoomFactor = 1 +
							norm(screenX - dragContext.screenX, screenY - dragContext.screenY) / ZOOM_SENSITIVITY
						if (currentState == ZOOM_OUT)
							totalZoomFactor = 1 / totalZoomFactor
						val scale = totalZoomFactor / dragContext.previousScale
						root.viewportTransform.scaleRelative(scale)
						val pivotInScene = root.diagram.localToScene(dragContext.pivotInDiagram)
						root.viewportTransform.translateRelative(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
						dragContext.previousScale = totalZoomFactor
					}
					case MARQUEE,
					case MARQUEE_XOR: {
						val position = root.diagramCanvas.screenToLocal(dragContext.screenX, dragContext.screenY)
						val newWidth = screenX - dragContext.screenX
						if (newWidth < 0) {
							marquee.x = position.x + newWidth
							marquee.width = -newWidth
						} else {
							marquee.x = position.x
							marquee.width = newWidth
						}
						val newHeight = screenY - dragContext.screenY
						if (newHeight < 0) {
							marquee.y = position.y + newHeight
							marquee.height = -newHeight
						} else {
							marquee.y = position.y
							marquee.height = newHeight
						}
						val marqueeBounds = root.diagram.sceneToLocal(marquee.localToScene(marquee.boundsInLocal))
						root.diagram.nodes.forEach [
							selected = isMarqueeSelected(marqueeBounds)
						] 
						for (connection : root.diagram.connections) {
							if (connection.selectable) {
								val selectedControlPoints = connection.controlPoints.filter[
									isMarqueeSelected(marqueeBounds)
								].toSet
								connection.selected = !selectedControlPoints.empty
								if(connection.selected)
									connection.controlPoints.forEach [
										selected = selectedControlPoints.contains(it)
									]
								else
									connection.controlPoints.forEach[selected = false]
							}
						}
					}
				}
				consume
			}
		]
		releasedHandler = [
			if (hasDragged) {
				hasDragged = false
				dragContext = null
				consume
			}
			root.diagramCanvas.children.safeDelete(marquee)
			currentState = null
			root.scene.cursor = Cursor.DEFAULT
		]
	}
	
	protected def isMarqueeSelected(XShape shape, Bounds marqueeBounds) {
		if(!shape.selectable)
			return false
		if(marqueeBounds.contains(shape.localToDiagram(shape.boundsInLocal.center))) {
			if (currentState == MARQUEE_XOR)
				return !previousSelection.contains(shape)
			else
				return true	
		} else {
			if (currentState == MARQUEE_XOR)
				return previousSelection.contains(shape)
			else
				return false	
		}
	}

	protected def applyToState(MouseEvent event) {
		val newState = if (event.isShortcutDown)
				if (event.isShiftDown)
					ZOOM_OUT
				else
					ZOOM_IN
			else if (event.button == PRIMARY || event.primaryButtonDown)
				if (event.isShiftDown)
					MARQUEE_XOR
				else
					MARQUEE
			else
				SCROLL
		if (currentState != newState) {
			currentState = newState
			event.startDragContext
			root.scene.cursor = switch newState {
				case SCROLL: Cursor.OPEN_HAND
				case MARQUEE: Cursor.CROSSHAIR
				case MARQUEE_XOR: Cursor.CROSSHAIR
				case ZOOM_IN: zoomInCursor
				case ZOOM_OUT: zoomOutCursor
			}
			if (newState == MARQUEE || newState == MARQUEE_XOR) {
				val position = root.diagramCanvas.screenToLocal(dragContext.screenX, dragContext.screenY)
				marquee => [
					x = position.x
					y = position.y
					width = 0
					height = 0
					arcWidth = 5
					arcHeight = 5
					fill = null
					stroke = Color.DARKGRAY
					strokeWidth = 2
					strokeLineCap = StrokeLineCap.ROUND
					strokeDashArray.setAll(10.0, 5.0, 5.0, 5.0)
					new Timeline => [
						keyFrames += new KeyFrame(0.millis, new KeyValue(marquee.strokeDashOffsetProperty, 0))
						keyFrames +=
							new KeyFrame(300.millis,
								new KeyValue(marquee.strokeDashOffsetProperty, marquee.strokeDashArray.reduce [
									$0 + $1
								] as Double))
						cycleCount = Animation.INDEFINITE
						play
					]
				]
				root.diagramCanvas.children.safeAdd(marquee)
				previousSelection = newHashSet
				if(newState == MARQUEE_XOR)
					previousSelection += root.currentSelection
			} else {
				root.diagramCanvas.children.safeDelete(marquee)
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
		SCROLL,
		ZOOM_IN,
		ZOOM_OUT,
		MARQUEE,
		MARQUEE_XOR
	}
}
