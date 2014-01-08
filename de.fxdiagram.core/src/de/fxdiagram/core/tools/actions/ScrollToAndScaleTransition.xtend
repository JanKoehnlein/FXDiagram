package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import javafx.animation.Transition
import javafx.geometry.Point2D
import javafx.util.Duration

import static java.lang.Math.*

import static extension javafx.util.Duration.*
import de.fxdiagram.core.extensions.AccumulativeTransform2D

class ScrollToAndScaleTransition extends Transition {

	XRoot root

	double fromScale
	double toScale
	
	Point2D fromTranslation
	Point2D toTranslation

	double fromAngle
	double toAngle
	
	new(XRoot root, Point2D targetCenterInDiagram, double targetScale) {
		this(root, targetCenterInDiagram, targetScale, 0)
	}
	
	new(XRoot root, Point2D targetCenterInDiagram, double targetScale, double targetAngle) {
		this.root = root
		fromScale = root.diagramTransform.scale
		toScale = max(AccumulativeTransform2D.MIN_SCALE, targetScale)
		fromTranslation = new Point2D(root.diagramTransform.translateX, root.diagramTransform.translateY)
		fromAngle = root.diagramTransform.rotate
		toAngle = targetAngle
		root.diagramTransform => [
			scaleRelative(toScale/fromScale)
			rotate = toAngle
		]
		val centerInScene = root.diagram.localToScene(targetCenterInDiagram)
		toTranslation = new Point2D(
					0.5 * root.scene.width - centerInScene.x + root.diagramTransform.translateX,
					0.5 * root.scene.height - centerInScene.y + root.diagramTransform.translateY)
		root.diagramTransform => [
			scale = fromScale
			rotate = fromAngle
			translate = fromTranslation
		]
		cycleDuration = 500.millis
	}
	
	def setDuration(Duration duration) {
		cycleDuration = duration		
	}
	
	override protected interpolate(double frac) {
		val scaleNow = (1-frac) * fromScale + frac * toScale
		val angleNow = (1-frac) * fromAngle + frac * toAngle
		val txNow =  (1-frac) * fromTranslation.x + frac * toTranslation.x
		val tyNow =  (1-frac) * fromTranslation.y + frac * toTranslation.y
		root.diagramTransform => [
			rotate = angleNow
			scale = scaleNow
			translateX = txNow
			translateY = tyNow
		]
	}
}