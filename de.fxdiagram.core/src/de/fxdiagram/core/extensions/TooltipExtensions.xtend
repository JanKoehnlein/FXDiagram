package de.fxdiagram.core.extensions

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.util.Duration

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension javafx.scene.layout.StackPane.*
import javafx.beans.property.StringProperty
import de.fxdiagram.annotations.properties.FxProperty
import javafx.scene.Parent

class TooltipExtensions {
	static def setTooltip(Node host, String text) {
		new SoftTooltip(host, text).install(host)
	}  
}

/**
 * JavaFX's {@link Tooltip} affectes gesture events in an unpredictable way. 
 * This tooltip is a lightweight version not using a pop-up.
 */
class SoftTooltip {
	
	@FxProperty Duration delay = 200.millis
	StringProperty textProperty
	
	Node host
	Node tooltip 
	TooltipTimer timer
	
	boolean canceled = false
	boolean isShowing
	
	new(Node host, String text) {
		this.host = host
		tooltip = new StackPane() => [
			style = '''
				-fx-border-color: black;
				-fx-border-width: 1;
				-fx-background-color: #ffffbb;
			'''
			children += new Text => [
				it.text = text
				this.textProperty = it.textProperty
				margin = new Insets(2,2,2,2)
			]
			mouseTransparent = true
		]
	}

	protected def void install(Node currentHost) {
		currentHost.addEventHandler(MouseEvent.ANY, [
			switch eventType {
				case MouseEvent.MOUSE_ENTERED: {
					setReferencePosition(sceneX, sceneY)
					timer = new TooltipTimer(this)
				}
				case MouseEvent.MOUSE_MOVED: {
					setReferencePosition(sceneX, sceneY)
					timer?.restart
				}
				default: {
					timer?.stop
					timer = null
					hide
					canceled = eventType != MouseEvent.MOUSE_EXITED
				}
			}			
		])
		switch currentHost {
			Parent: currentHost.childrenUnmodifiable.forEach[install] 
		}
	}
	
	def getText() {
		textProperty.get
	}
	
	def setText(String text) {
		textProperty.set(text)
	}
	
	def isShowing() {
		isShowing
	}
	
	def setReferencePosition(double positionX, double positionY) {
		setPosition(positionX + 16, positionY - 32)
	}

	def setPosition(double positionX, double positionY) {
		tooltip => [
			layoutX = positionX
			layoutY = positionY
		]
	}
	
	def show(double positionX, double positionY) {
		setReferencePosition(positionX, positionY)
		show()
	}

	def show() {
		if(host != null && !isShowing)
			host.root.headsUpDisplay.children += tooltip
		isShowing = true
	}

	def hide() {
		if(host != null && isShowing)
			host.root.getHeadsUpDisplay.children -= tooltip
		isShowing = false
	}
}

class TooltipTimer implements Runnable {
	SoftTooltip tooltip
	boolean isRunning
	long endTime
	
	new(SoftTooltip behavior) {
		this.tooltip = behavior
		new Thread(this).start
		isRunning = true
		restart 
	}
	
	def stop() {
		isRunning = false
	}
	
	def restart() {
		endTime = System.currentTimeMillis + (tooltip.delay.toMillis as long) 
	}
	
	override run() {
		var long delay
		do {
			Thread.sleep(endTime - System.currentTimeMillis)
			if(!isRunning) 
				return;
			delay = endTime - System.currentTimeMillis
		} while(delay > 0)
		if(isRunning)
			Platform.runLater[| tooltip.show]	
	}
}

