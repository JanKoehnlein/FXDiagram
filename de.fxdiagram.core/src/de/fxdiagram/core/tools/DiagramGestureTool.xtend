package de.fxdiagram.core.tools

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import javafx.event.EventHandler
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import javafx.scene.input.RotateEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent
import javafx.scene.transform.Rotate
import org.eclipse.xtend.lib.annotations.Accessors

class DiagramGestureTool implements XDiagramTool {

	XRoot root

	ZoomContext zoomContext

	EventHandler<ZoomEvent> zoomStartHandler

	EventHandler<ZoomEvent> zoomHandler

	EventHandler<ScrollEvent> scrollHandler

	EventHandler<RotateEvent> rotateHandler
	
	new(XRoot root) {
		this.root = root
		zoomStartHandler = [
			zoomContext = new ZoomContext(root.diagram.sceneToLocal(sceneX, sceneY))
		]
		zoomHandler = [
			val scale = totalZoomFactor / zoomContext.previousScale
			root.viewportTransform.scaleRelative(scale)
			val pivotInScene = root.diagram.localToScene(zoomContext.pivotInDiagram)
			root.viewportTransform.translateRelative(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
			zoomContext.previousScale = totalZoomFactor
		]
		scrollHandler = [
			// mimic Visio behavior: CTRL-MouseWheel to zoom, SHIFT-MouseWheel to scroll horizontally
			if(isShortcutDown) {
				val pivotInLocal = root.diagram.sceneToLocal(sceneX, sceneY)
				val scale = root.viewportTransform.scale * (1+deltaY/400)
				root.viewportTransform.scale = scale
				val pivotInScene = root.diagram.localToScene(pivotInLocal)
				root.viewportTransform.translateRelative(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
			} else if(isShiftDown) {
				root.viewportTransform.translateRelative(deltaY, 0)
			} else {
				root.viewportTransform.translateRelative(deltaX, deltaY)				
			}
		]
		rotateHandler = [
			var selection = root.currentSelection
			val rotateSet = (
				if(selection.empty) {
					root.diagram.nodes + root.diagram.connections.map[nonAnchorPoints].flatten
				} else {
					val nodes = selection.filter(XNode).toSet
					val connections = selection.filter(XConnection) 
						+ nodes.map[
							outgoingConnections.filter[nodes.contains(target)]
							+ incomingConnections.filter[nodes.contains(source)]
						].flatten
					nodes + connections.map[nonAnchorPoints].flatten + selection.filter(XControlPoint)
				}
			).toSet
			val pivot = root.diagram.sceneToLocal(sceneX, sceneY)
			val rotate = new Rotate(angle, pivot.x, pivot.y)
			rotateSet.forEach[ 
				val offset = rotateOffset 
				val newPosition = rotate.transform(layoutX + offset.width, layoutY + offset.height)
				layoutX = newPosition.x - offset.width
				layoutY = newPosition.y - offset.height
			]
		]
	}
	
	protected def getNonAnchorPoints(XConnection it) {
		controlPoints.filter[type != XControlPoint.Type.ANCHOR]
	}
	
	protected def getRotateOffset(XShape it) {
		if (it instanceof XControlPoint) 
			new Dimension2D(0, 0)
		else
			new Dimension2D(0.5 * layoutBounds.width, 0.5 * layoutBounds.height)	
	}

	override activate() {
		val scene = root.scene
		scene.addEventHandler(ZoomEvent.ZOOM_STARTED, zoomStartHandler)
		scene.addEventHandler(ZoomEvent.ZOOM, zoomHandler)
		scene.addEventHandler(ZoomEvent.ZOOM_FINISHED, zoomHandler)
		scene.addEventHandler(ScrollEvent.SCROLL, scrollHandler)
		scene.addEventHandler(ScrollEvent.SCROLL_FINISHED, scrollHandler)
		scene.addEventHandler(RotateEvent.ROTATION_STARTED, rotateHandler)
		scene.addEventHandler(RotateEvent.ROTATE, rotateHandler)
		scene.addEventHandler(RotateEvent.ROTATION_FINISHED, rotateHandler)
		true
	}

	override deactivate() {
		val scene = root.scene
		scene.removeEventHandler(ZoomEvent.ZOOM_STARTED, zoomStartHandler)
		scene.removeEventHandler(ZoomEvent.ZOOM, zoomHandler)
		scene.removeEventHandler(ZoomEvent.ZOOM_FINISHED, zoomHandler)
		scene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler)
		scene.removeEventHandler(ScrollEvent.SCROLL_FINISHED, scrollHandler)
		scene.removeEventHandler(RotateEvent.ROTATION_STARTED, rotateHandler)
		scene.removeEventHandler(RotateEvent.ROTATE, rotateHandler)
		scene.removeEventHandler(RotateEvent.ROTATION_FINISHED, rotateHandler)
		true
	}
	
	static class ZoomContext {
		@Accessors double previousScale = 1
		@Accessors Point2D pivotInDiagram
	
		new(Point2D pivotInDiagram) {
			this.pivotInDiagram = pivotInDiagram
		}
	}
}

