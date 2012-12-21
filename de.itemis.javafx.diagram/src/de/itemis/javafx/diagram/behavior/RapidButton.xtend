package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.Activateable
import de.itemis.javafx.diagram.XNode
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ChangeListener
import javafx.geometry.Point2D
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.util.Duration
import de.itemis.javafx.diagram.Extensions

class RapidButton extends ImageView implements Activateable {
	
	XNode host
	
	Placer placer
	
	(RapidButton)=>void action

	Timeline timeline
	
	new(XNode host, double xPos, double yPos, 
		String file, (RapidButton)=>void action) {
		this.host = host
		this.action = action
		image = new Image(file)
		placer = new Placer(this, xPos, yPos)
	}
	
	def protected setPosition(Point2D position) {
		translateX = position.x
		translateY = position.y
	}
	
	def getHost() {	host }

	def getPlacer() { placer }
	
	def show() {
		getTimeline.stop
		visible = true
		opacity = 1.0
	}
	
	def fade() {
		getTimeline.playFromStart	
	}
	
	def protected getTimeline() {
		if(timeline == null) 
			timeline = new Timeline => [
				autoReverse = true
				keyFrames += new KeyFrame(Duration::millis(500), 
					new KeyValue(this.opacityProperty, 1.0))
				keyFrames += new KeyFrame(Duration::millis(1000), 
					new KeyValue(this.opacityProperty, 0.0))
				keyFrames += new KeyFrame(Duration::millis(1000), 
					new KeyValue(this.visibleProperty, false)
				)
			]
		timeline
	}

	override activate() {
		visible = false
		onMouseEntered = [ show ]
		onMouseExited = [ fade ]
		onMousePressed = [ action.apply(this) ]
		placer.activate()
		position = placer.value
		val ChangeListener<Point2D> listener = [
			element, oldVal, newVal | position = newVal
		] 
		placer.addListener(listener)
	}
	
}

class Placer extends ObjectBinding<Point2D> implements Activateable {
	
	extension Extensions
	
	RapidButton button
	double xPos
	double yPos
	
	new(RapidButton button, double xPos, double yPos) {
		this.button = button
		this.xPos = xPos
		this.yPos = yPos
	}
	
	override activate() {
		val node = button.host
		bind(node.translateXProperty, node.translateYProperty, node.boundsInLocalProperty)
	}

	override protected computeValue() {
		val node = button.host.node
		val absPosition = node.localToRoot(node.translateX, node.translateY)
		val absBounds = node.localToRoot(node.boundsInLocal)
		val totalWidth = absBounds.width + 2 * button.layoutBounds.width
		val totalHeight = absBounds.height + 2 * button.layoutBounds.height
		new Point2D(absPosition.x - 1.5 * button.layoutBounds.width + xPos * totalWidth,
				absPosition.y - 1.5 * button.layoutBounds.height + yPos * totalHeight)
	}
	
	def getXPos() { xPos }
	
	def getYPos() { yPos }
	
}
