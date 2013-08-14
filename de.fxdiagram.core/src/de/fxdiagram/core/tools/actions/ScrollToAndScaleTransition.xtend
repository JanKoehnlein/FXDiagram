package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XRootDiagram
import javafx.animation.Transition
import javafx.geometry.Point2D
import javafx.util.Duration

import static java.lang.Math.*

import static extension de.fxdiagram.core.geometry.TransformExtensions.*
import static extension javafx.util.Duration.*

class ScrollToAndScaleTransition extends Transition {

	XRoot root

	double fromScale
	double toScale
	
	Point2D fromTranslation
	Point2D toTranslation

	new(XRoot root, Point2D targetCenterInDiagram, double targetScale) {
		this.root = root
		fromScale = root.diagram.scale
		toScale = max(XRootDiagram.MIN_SCALE, targetScale)
		fromTranslation = new Point2D(root.diagram.canvasTransform.tx, root.diagram.canvasTransform.ty)
		val rescale = toScale / fromScale
		root.diagram.canvasTransform.scale(rescale, rescale)
		val centerInScene = root.diagram.localToScene(targetCenterInDiagram)
		toTranslation = new Point2D(
					0.5 * root.scene.width - centerInScene.x + root.diagram.canvasTransform.tx,
					0.5 * root.scene.height - centerInScene.y + root.diagram.canvasTransform.ty)
		root.diagram.canvasTransform.scale(1/rescale, 1/rescale)
		cycleDuration = 500.millis
	}
	
	def setDuration(Duration duration) {
		cycleDuration = duration		
	}
	
	override protected interpolate(double frac) {
		val scaleNow = (1-frac) * fromScale + frac * toScale
		val txNow =  (1-frac) * fromTranslation.x + frac * toTranslation.x
		val tyNow =  (1-frac) * fromTranslation.y + frac * toTranslation.y
		val rescale = scaleNow / root.diagram.scale
		root.diagram.scale = scaleNow
		root.diagram.canvasTransform => [
			scale(rescale, rescale)
			tx = txNow
			ty = tyNow
		]
	}
}