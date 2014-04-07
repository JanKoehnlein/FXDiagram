package de.fxdiagram.core.viewport

import de.fxdiagram.core.XRoot
import javafx.animation.Transition
import javafx.geometry.Point2D
import javafx.util.Duration

import static java.lang.Math.*

import static extension javafx.util.Duration.*

class ViewportTransition extends Transition {

	XRoot root

	ViewportMemento from
	ViewportMemento to
	
	new(XRoot root, ViewportMemento toMemento, Duration duration) {
		this.root = root
		this.from = root.diagramTransform.createMemento
		this.to = toMemento
		cycleDuration = new Duration(min(duration.toMillis, from.dist(to)))
	}

	new(XRoot root, Point2D targetCenterInDiagram, double targetScale) {
		this(root, targetCenterInDiagram, targetScale, 0)
	}
	
	new(XRoot root, Point2D targetCenterInDiagram, double targetScale, double targetAngle) {
		this.root = root
		this.from = root.diagramTransform.createMemento
		this.to = calculateTargetMemento(targetCenterInDiagram, targetScale, targetAngle)
		cycleDuration = 500.millis
	}
	
	def setDuration(Duration duration) {
		cycleDuration = duration		
	}
	
	def getFrom() {
		from
	}
	
	def getTo() {
		to
	}
	
	override protected interpolate(double frac) {
		root.diagramTransform => [
			rotate = (1-frac) * from.rotate + frac * to.rotate
			scale = (1-frac) * from.scale + frac * to.scale
			translateX = (1-frac) * from.translateX + frac * to.translateX
			translateY = (1-frac) * from.translateY + frac * to.translateY
		]
	}
	
	def calculateTargetMemento(Point2D targetCenterInDiagram, double targetScale, double targetAngle) {
		val toScale = max(ViewportTransform.MIN_SCALE, targetScale)
		root.diagramTransform => [
			scaleRelative(toScale/from.scale)
			rotate = targetAngle
		]
		val centerInScene = root.diagram.localToScene(targetCenterInDiagram)
		val toTranslation = new Point2D(
					0.5 * root.scene.width - centerInScene.x + root.diagramTransform.translateX,
					0.5 * root.scene.height - centerInScene.y + root.diagramTransform.translateY)
		root.diagramTransform.applyMemento(from)
		return new ViewportMemento(toTranslation.x, toTranslation.y, toScale, targetAngle)
	}
	
}