package de.fxdiagram.lib.nodes

import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractOpenBehavior
import de.fxdiagram.core.model.DomainObjectHandle
import javafx.animation.RotateTransition
import javafx.animation.SequentialTransition
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension java.lang.Math.*
import static extension javafx.util.Duration.*
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode
class FlipNode extends XNode {

	Node front

	Node back

	Group pane = new Group

	boolean isCurrentFront = true

	EventHandler<MouseEvent> clickHandler

	new(String name) {
		super(name)
	}

	new(DomainObjectHandle domainObject) {
		super(domainObject)
	}
	
	override doActivatePreview() {
		super.doActivatePreview()
		node = pane
	}
	
	override doActivate() {
		super.doActivate()
		if(front == null)
			throw new IllegalStateException('FlipNode.front not set')
		if(back == null)
			throw new IllegalStateException('FlipNode.back not set')
		cursor = Cursor.HAND
		flipOnDoubleClick = true
		val AbstractOpenBehavior openBehavior = [| flip(true) ]
		addBehavior(openBehavior)
	}

	def setFlipOnDoubleClick(boolean isFlipOnDoubleClick) {
		clickHandler = if(isFlipOnDoubleClick) 
				[ event |
					if (event.clickCount == 2) {
						if (front != null && back != null) 
							flip(isHorizontal(event))
					}
				]
			else 
				null
		onMouseClicked = clickHandler
	}

	def void flip(boolean isHorizontal) {
		back.layoutX = front.layoutX - (back.layoutBounds.width - front.layoutBounds.width) / 2 
			- back.layoutBounds.minX
		back.layoutY = front.layoutY - (back.layoutBounds.height - front.layoutBounds.height) / 2 
			- back.layoutBounds.minY
		val turnAxis = if (isHorizontal)
				new Point3D(1, 0, 0)
			else 
				new Point3D(0, 1, 0)
		new SequentialTransition => [
			children += new RotateTransition => [
				node = getCurrentVisible
				duration = 250.millis
				axis = turnAxis
				fromAngle = 0
				toAngle = 90
				onFinished = [
					getCurrentVisible.visible = false
					isCurrentFront = ! isCurrentFront
					getCurrentVisible.visible = true
				]
			]
			children += new RotateTransition => [
				node = getCurrentInvisible
				duration = 250.millis
				axis = turnAxis
				fromAngle = 90
				toAngle = 0
			]
			play
		]
	}
	
	def protected isHorizontal(MouseEvent event) {
		val clickInScene = new Point2D(event.sceneX, event.sceneY)
		val clickInLocal = getCurrentVisible.sceneToLocal(clickInScene)
		val center = boundsInLocal.center
		val direction = new Point3D(clickInLocal.x - center.x, clickInLocal.y - center.y, 0)
		return (direction.x * direction.x + direction.y * direction.y < EPSILON)
				|| (direction.x.abs < direction.y.abs)
	}

	def setFront(Node front) {
		this.front = front
		pane.children += front
		front.visible = isCurrentFront
	}
	
	def getFront() {
		front
	}

	def setBack(Node back) {
		this.back = back
		pane.children += back
		back.visible = !isCurrentFront
	}
	
	def getBack() {
		back
	}

	def getCurrentVisible() {
		if(isCurrentFront) front else back
	}

	def getCurrentInvisible() {
		if(isCurrentFront) back else front
	}

}
