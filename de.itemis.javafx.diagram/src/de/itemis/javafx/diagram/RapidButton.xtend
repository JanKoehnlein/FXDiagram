package de.itemis.javafx.diagram

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.control.Button
import javafx.util.Duration

class RapidButton extends Button {
	
	Timeline timeline
	
	new(String text) {
		super(text)
		visible = false
		onMouseEntered = [ show ]
		onMouseExited = [ fade ]
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
				keyFrames += new KeyFrame(Duration::millis(1500), 
					new KeyValue(this.opacityProperty, 0.0))
				keyFrames += new KeyFrame(Duration::millis(1500), 
					new KeyValue(this.visibleProperty, false)
				)
			]
		timeline
	}
}

