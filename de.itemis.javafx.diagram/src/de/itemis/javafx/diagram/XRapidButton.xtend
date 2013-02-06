package de.itemis.javafx.diagram

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ChangeListener
import javafx.geometry.Point2D
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.util.Duration

import static extension de.itemis.javafx.diagram.Extensions.*
import javafx.animation.KeyValue

class XRapidButton extends ImageView implements XActivatable {
	
	boolean isActive
	
	XNode host
	
	Placer placer
	
	(XRapidButton)=>void action

	Timeline timeline
	
	new(XNode host, double xPos, double yPos, 
		String file, (XRapidButton)=>void action) {
		this.host = host
		this.action = action
		image = new Image(file)
		placer = new Placer(this, xPos, yPos)
	}
	
	override activate() {
		if(!isActive)
			doActivate
		isActive = true
	}
	
	def doActivate() {
		visible = false
		onMouseEntered = [ show ]
		onMouseExited = [ fade ]
		onMousePressed = [ action.apply(this) consume ]
		placer.activate
		val ChangeListener<Point2D> listener = [
			element, oldVal, newVal | relocate(newVal.x, newVal.y)
		] 
		placer.addListener(listener)
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
}

class Placer extends ObjectBinding<Point2D> {
	
	XRapidButton button
	double xPos
	double yPos
	
	new(XRapidButton button, double xPos, double yPos) {
		this.button = button
		this.xPos = xPos
		this.yPos = yPos
		activate
	}
	
	def activate() {
		val node = button.host
		bind(node.layoutXProperty, node.layoutYProperty, node.layoutBoundsProperty)
	}

	override protected computeValue() {
		val node = button.host
		val boundsInDiagram = node.localToDiagram(node.layoutBounds)
		if(boundsInDiagram != null) {
			val totalWidth = boundsInDiagram.width + 2 * button.layoutBounds.width
			val totalHeight = boundsInDiagram.height + 2 * button.layoutBounds.height
			val position = new Point2D(boundsInDiagram.minX - 1.5 * button.layoutBounds.width + xPos * totalWidth,
					boundsInDiagram.minY - 1.5 * button.layoutBounds.height + yPos * totalHeight)
			position
		} else {
			null
		}
	}
	
	def getXPos() { xPos }
	
	def getYPos() { yPos }
	
}
