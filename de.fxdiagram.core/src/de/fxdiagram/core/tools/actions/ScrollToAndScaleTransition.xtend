package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRootDiagram
import javafx.animation.Transition
import javafx.geometry.Point2D

import static java.lang.Math.*

import static extension de.fxdiagram.core.geometry.TransformExtensions.*
import static extension javafx.util.Duration.*
import javafx.util.Duration

class ScrollToAndScaleTransition extends Transition {

	XRootDiagram diagram

	double fromScale
	double toScale
	
	Point2D fromTranslation
	Point2D toTranslation

	new(XRootDiagram diagram, Point2D targetCenterInDiagram, double targetScale) {
		this.diagram = diagram
		fromScale = diagram.scale
		toScale = max(XRootDiagram.MIN_SCALE, targetScale)
		fromTranslation = new Point2D(diagram.canvasTransform.tx, diagram.canvasTransform.ty)
		val rescale = toScale / fromScale
		diagram.canvasTransform.scale(rescale, rescale)
		val centerInScene = diagram.localToScene(targetCenterInDiagram)
		toTranslation = new Point2D(
					0.5 * diagram.scene.width - centerInScene.x + diagram.canvasTransform.tx,
					0.5 * diagram.scene.height - centerInScene.y + diagram.canvasTransform.ty)
		diagram.canvasTransform.scale(1/rescale, 1/rescale)
		cycleDuration = 500.millis
	}
	
	def setDuration(Duration duration) {
		cycleDuration = duration		
	}
	
	override protected interpolate(double frac) {
		val scaleNow = (1-frac) * fromScale + frac * toScale
		val txNow =  (1-frac) * fromTranslation.x + frac * toTranslation.x
		val tyNow =  (1-frac) * fromTranslation.y + frac * toTranslation.y
		val rescale = scaleNow / diagram.scale
		diagram.scale = scaleNow
		diagram.canvasTransform => [
			scale(rescale, rescale)
			tx = txNow
			ty = tyNow
		]
	}
}