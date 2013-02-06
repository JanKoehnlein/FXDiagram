package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.XRootDiagram
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.RotateEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent
import javafx.scene.transform.Affine

import static extension de.itemis.javafx.diagram.transform.TransformExtensions.*

class DiagramGestureTool {
	
	ZoomContext zoomContext
	
	Affine diagramTransform
	
	new(XRootDiagram diagram) {
		val scene = diagram.scene
		diagramTransform = new Affine
		diagram.transforms.clear
		diagram.transforms += diagramTransform
		scene.onZoomStarted = [
 			zoomContext = new ZoomContext(diagram.sceneToLocal(sceneX, sceneY))
		]
		val EventHandler<ZoomEvent> zoomHandler = [
			val scale = totalZoomFactor / zoomContext.previousScale
			diagram.scaleProperty.set(scale * diagram.scaleProperty.get)
			diagramTransform.scale(scale, scale)
			val pivotInScene = diagram.localToScene(zoomContext.pivotInDiagram)
			diagramTransform.translate(sceneX - pivotInScene.x, sceneY - pivotInScene.y)
			zoomContext.previousScale = totalZoomFactor
		]
		scene.onZoom = zoomHandler 
		scene.onZoomFinished = zoomHandler

		val EventHandler<ScrollEvent> scrollHandler = [
			diagramTransform.translate(deltaX, deltaY)
		] 
		scene.onScrollStarted = scrollHandler 
		scene.onScroll = scrollHandler
		scene.onScrollFinished = scrollHandler
		
		val EventHandler<RotateEvent> rotateHandler = [
			if(shortcutDown)
				diagramTransform.rotate(angle, sceneX, sceneY)
		] 
		scene.onRotationStarted = rotateHandler
		scene.onRotate = rotateHandler
		scene.onRotationFinished = rotateHandler
	}
}

class ZoomContext {
	@Property double previousScale = 1
	@Property Point2D pivotInDiagram

	new(Point2D pivotInDiagram) {
		this.pivotInDiagram = pivotInDiagram
	}
}
