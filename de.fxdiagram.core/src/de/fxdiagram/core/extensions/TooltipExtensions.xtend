package de.fxdiagram.core.extensions

import de.fxdiagram.annotations.properties.FxProperty
import javafx.application.Platform
import javafx.beans.property.StringProperty
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Tooltip
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.util.Duration

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension javafx.scene.layout.StackPane.*

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
	
	boolean isHideOnTrigger = false
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
		this.timer = new TooltipTimer(this)
	}

	protected def void install(Node host) {
		host.addEventHandler(MouseEvent.ANY, [
			switch eventType {
				case MouseEvent.MOUSE_ENTERED_TARGET: {
					isHideOnTrigger = false
					setReferencePosition(sceneX, sceneY)
					timer?.restart
				}
				case MouseEvent.MOUSE_EXITED_TARGET: {
				}
				case MouseEvent.MOUSE_ENTERED: {
					isHideOnTrigger = false
					setReferencePosition(sceneX, sceneY)
					timer?.restart
				}
				case MouseEvent.MOUSE_MOVED: {
					setReferencePosition(sceneX, sceneY)
					timer?.restart
				}
				default: {
					isHideOnTrigger = true
					timer?.restart
				}
			}			
		])
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

	def trigger() {
		if(isHideOnTrigger)
			hide
		else 
			show
	}

	def show() {
		if(!isShowing)
			host?.root?.headsUpDisplay?.children?.add(tooltip)
		isShowing = true
	}
	
	def hide() {
		if(isShowing)
			host?.root?.headsUpDisplay?.children?.remove(tooltip)
		isShowing = false
	}
}

class TooltipTimer implements Runnable {
	SoftTooltip tooltip
	boolean isRunning
	long endTime
	
	new(SoftTooltip behavior) {
		this.tooltip = behavior
		isRunning = false
	}
	
	def stop() {
		isRunning = false
	}
	
	def restart() {
		if(!isRunning) {
			isRunning = true
			new Thread(this).start
		}
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
			Platform.runLater[| tooltip.trigger]	
		isRunning = false
	}
}

