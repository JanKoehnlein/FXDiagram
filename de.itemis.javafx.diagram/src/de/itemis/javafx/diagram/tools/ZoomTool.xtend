package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.Diagram

class ZoomTool {
	
	double initialScale
	
	new(Diagram diagram) {
		val scene = diagram.rootPane.scene
		val rootPane = diagram.rootPane
		scene.onZoomStarted = [
			initialScale = rootPane.scaleX
		]
		scene.onZoom = [
			rootPane.scaleX = totalZoomFactor * initialScale
			rootPane.scaleY = totalZoomFactor * initialScale
		]
		scene.onZoomFinished = [
			rootPane.scaleX = totalZoomFactor * initialScale
			rootPane.scaleY = totalZoomFactor * initialScale
		]
	}
}