package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.XRootDiagram
import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent

class ZoomTool {
	
	double initialScale
	
	new(XRootDiagram diagram) {
		val scene = diagram.scene
		
		scene.onZoomStarted = [
			initialScale = diagram.scaleX
		]
		val EventHandler<ZoomEvent> zoomHandler = [
			diagram.scaleX = totalZoomFactor * initialScale
			diagram.scaleY = totalZoomFactor * initialScale
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