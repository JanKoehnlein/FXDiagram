package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.XRootDiagram
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.ScrollEvent
import javafx.scene.input.ZoomEvent

class ZoomTool {
	
	ZoomContext zoomContext
	
	def foo() {
		val x=newArrayList()
		println('''«x.size»''')
	}
	new(XRootDiagram diagram) {
		val scene = diagram.scene
		scene.onZoomStarted = [
			zoomContext = new ZoomContext(diagram.scaleX, diagram.sceneToLocal(sceneX, sceneY))
		]
		val EventHandler<ZoomEvent> zoomHandler = [ 
			println(diagram.localToParentTransform)
			val scale = totalZoomFactor * zoomContext.initialScale
			diagram.scaleX = scale
			diagram.scaleY = scale
			val pivotInScene = diagram.localToScene(zoomContext.initialDiagramPos)
			diagram.translateX = diagram.translateX + (sceneX - pivotInScene.x) / scale
			diagram.translateY = diagram.translateX + (sceneY - pivotInScene.y) / scale
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