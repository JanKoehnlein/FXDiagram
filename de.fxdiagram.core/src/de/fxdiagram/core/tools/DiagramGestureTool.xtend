package de.fxdiagram.core.tools

import de.fxdiagram.core.XRoot
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.RotateEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*

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
			val scale = max(totalZoomFactor / zoomContext.previousScale, XRoot.MIN_SCALE)
			val newScale = scale * root.diagramScale 
			root.diagramScale = newScale
			root.diagramTransform.scale(scale, scale)
			val pivotInScene = root.diagram.localToScene(zoomContext.pivotInDiagram)
			root.diagramTransform.translate(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
			zoomContext.previousScale = totalZoomFactor
		]
		scrollHandler = [
			root.diagramTransform.translate(deltaX, deltaY)
		]
		rotateHandler = [
			if (shortcutDown)
				root.diagramTransform.rotate(angle, sceneX, sceneY)
		]
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
}

class ZoomContext {
	@Property double previousScale = 1
	@Property Point2D pivotInDiagram

	new(Point2D pivotInDiagram) {
		this.pivotInDiagram = pivotInDiagram
	}
}
