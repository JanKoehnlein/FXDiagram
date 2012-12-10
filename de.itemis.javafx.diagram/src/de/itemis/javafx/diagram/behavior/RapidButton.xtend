package de.itemis.javafx.diagram.behavior

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.util.Duration
import de.itemis.javafx.diagram.ShapeContainer
import javafx.beans.binding.ObjectBinding
import javafx.geometry.Point2D
import javafx.beans.value.ChangeListener

class RapidButton extends ImageView {
	
	Timeline timeline
	
	ShapeContainer host;
	
	Placer placer
	
	new(ShapeContainer host, double xPos, double yPos, 
		String file, (ShapeContainer)=>void action) {
		this.host = host;
		image = new Image(file)
		visible = false
		onMouseEntered = [ show ]
		onMouseExited = [ fade ]
		onMousePressed = [ action.apply(host) ]
		placer = new Placer(this, xPos, yPos)
		position = placer.value
		val ChangeListener<Point2D> listener = [
			element, oldVal, newVal | position = newVal
		] 
		placer.addListener(listener)
	}
	
	def protected setPosition(Point2D position) {
		translateX = position.x
		translateY = position.y
	}
	
	def getHost() {
		host
	}

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
}

class Placer extends ObjectBinding<Point2D>{
	RapidButton button
	double xPos
	double yPos
	
	new(RapidButton button, double xPos, double yPos) {
		this.button = button
		this.xPos = xPos
		this.yPos = yPos
		val node = button.host
		bind(node.translateXProperty, node.translateYProperty, node.boundsInLocalProperty)
	}

	override protected computeValue() {
		val node = button.host.node
		val absPosition = node.localToScene(new Point2D(node.translateX, node.translateY))
		val absBounds = node.localToScene(node.boundsInLocal)
		val totalWidth = absBounds.width + 2 * button.layoutBounds.width
		val totalHeight = absBounds.height + 2 * button.layoutBounds.height
		new Point2D(absPosition.x - 1.5 * button.layoutBounds.width + xPos * totalWidth,
				absPosition.y - 1.5 * button.layoutBounds.height + yPos * totalHeight)
	}
	
}
