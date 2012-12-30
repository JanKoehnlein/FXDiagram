package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.XRootDiagram
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent

class ZoomTool {
	
	ZoomContext zoomContext
	
	new(XRootDiagram diagram) {
		val scene = diagram.scene
		scene.onZoomStarted = [
			zoomContext = new ZoomContext(diagram.scaleX, diagram.sceneToLocal(sceneX, sceneY))
		]
		val EventHandler<ZoomEvent> zoomHandler = [ 
			val scale = totalZoomFactor * zoomContext.initialScale
			if(scale > 0) {
				diagram.scaleX = scale
				diagram.scaleY = scale
				val pivotInScene = diagram.localToScene(zoomContext.initialDiagramPos)
				diagram.translateX = diagram.translateX + sceneX - pivotInScene.x
				diagram.translateY = diagram.translateY + sceneY - pivotInScene.y
			}
		]
		scene.onZoom = zoomHandler 
		scene.onZoomFinished = zoomHandler
		
		val EventHandler<ScrollEvent> scrollHandler = [
			diagram.translateX = diagram.translateX + deltaX		
			diagram.translateY = diagram.translateY + deltaY		
		] 
		scene.onScrollStarted = scrollHandler 
		scene.onScroll = scrollHandler
		scene.onScrollFinished = scrollHandler
	}
}

@Data
class ZoomContext {
	double initialScale
	Point2D initialDiagramPos
}