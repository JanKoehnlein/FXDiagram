package de.fxdiagram.core.tools

import de.fxdiagram.core.XRootDiagram
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.RotateEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent
import javafx.scene.transform.Affine

import static extension de.fxdiagram.core.transform.TransformExtensions.*

class DiagramGestureTool implements XDiagramTool {
	
	XRootDiagram diagram
	
	ZoomContext zoomContext
	
	Affine diagramTransform
	
	EventHandler<ZoomEvent> zoomStartHandler

	EventHandler<ZoomEvent> zoomHandler
	
	EventHandler<ScrollEvent> scrollHandler
	
	EventHandler<RotateEvent> rotateHandler
	
	new(XRootDiagram diagram) {
		this.diagram = diagram
		diagramTransform = new Affine
		diagram.transforms.clear
		diagram.transforms += diagramTransform
		zoomStartHandler = [
 			zoomContext = new ZoomContext(diagram.sceneToLocal(sceneX, sceneY))
		]
		zoomHandler = [
			val scale = totalZoomFactor / zoomContext.previousScale
			diagram.scaleProperty.set(scale * diagram.scaleProperty.get)
			diagramTransform.scale(scale, scale)
			val pivotInScene = diagram.localToScene(zoomContext.pivotInDiagram)
			diagramTransform.translate(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
			zoomContext.previousScale = totalZoomFactor
		]
		scrollHandler = [
			diagramTransform.translate(deltaX, deltaY)
		] 
		rotateHandler = [
			if(shortcutDown)
				diagramTransform.rotate(angle, sceneX, sceneY)
		] 
	}
	
	override activate() {
		val scene = diagram.scene
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
		val scene = diagram.scene
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
