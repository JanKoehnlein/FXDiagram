package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import javafx.animation.Transition
import javafx.geometry.Point2D
import javafx.util.Duration

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import static extension javafx.util.Duration.*

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
		fromScale = root.diagramScale
		toScale = max(XRoot.MIN_SCALE, targetScale)
		fromTranslation = new Point2D(root.diagramTransform.tx, root.diagramTransform.ty)
		fromAngle = acos(root.diagramTransform.mxx / fromScale)
		toAngle = targetAngle
		val rescale = toScale / fromScale
		root.diagramTransform => [
			scale(rescale, rescale)
			mxx = toScale * cos(toAngle)
			mxy = toScale * sin(toAngle)
			myx = toScale * -sin(toAngle)
			myy = toScale * cos(toAngle)
		]
		val centerInScene = root.diagram.localToScene(targetCenterInDiagram)
		toTranslation = new Point2D(
					0.5 * root.scene.width - centerInScene.x + root.diagramTransform.tx,
					0.5 * root.scene.height - centerInScene.y + root.diagramTransform.ty)
		root.diagramTransform => [
			mxx = fromScale * cos(fromAngle)
			mxy = fromScale * sin(fromAngle)
			myx = fromScale * -sin(fromAngle)
			myy = fromScale * cos(fromAngle)
			tx = fromTranslation.x
			tx = fromTranslation.y
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
		root.diagramScale = scaleNow
		root.diagramTransform => [
			mxx = scaleNow * cos(angleNow)
			mxy = scaleNow * sin(angleNow)
			myx = scaleNow * -sin(angleNow)
			myy = scaleNow * cos(angleNow)
			tx = txNow
			ty = tyNow
		]
	}
}