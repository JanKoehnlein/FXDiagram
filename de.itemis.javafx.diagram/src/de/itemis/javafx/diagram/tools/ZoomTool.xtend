package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.XDiagram
import javafx.event.EventHandler
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent

class ZoomTool {
	
	double initialScale
	
	new(XDiagram diagram) {
		val scene = diagram.rootPane.scene
		val rootPane = diagram.rootPane
		
		scene.onZoomStarted = [
			initialScale = rootPane.scaleX
		]
		val EventHandler<ZoomEvent> zoomHandler = [
			rootPane.scaleX = totalZoomFactor * initialScale
			rootPane.scaleY = totalZoomFactor * initialScale
		]
		scene.onZoom = zoomHandler 
		scene.onZoomFinished = zoomHandler
		
		val EventHandler<ScrollEvent> scrollHandler = [
			rootPane.translateX = rootPane.translateX + deltaX		
			rootPane.translateY = rootPane.translateY + deltaY		
		] 
		scene.onScrollStarted = scrollHandler 
		scene.onScroll = scrollHandler
		scene.onScrollFinished = scrollHandler
	}
}