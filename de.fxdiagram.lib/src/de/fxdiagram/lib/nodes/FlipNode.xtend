package de.fxdiagram.lib.nodes

import javafx.animation.RotateTransition
import javafx.animation.SequentialTransition
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.binding.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.geometry.BoundsExtensions.*
import static extension java.lang.Math.*
import static extension javafx.util.Duration.*

class FlipNode extends Parent {

	Node front

	Node back
	
	Group pane = new Group

	boolean isCurrentFront = true

	EventHandler<MouseEvent> clickHandler

	new() {
		children += pane
		clickHandler = [ event |
			if (front != null && back != null && event.clickCount == 2) {
				val clickInScene = new Point2D(event.sceneX, event.sceneY)
				val clickInLocal = getCurrentVisible.sceneToLocal(clickInScene)
				val center = boundsInLocal.center
				val direction = new Point3D(clickInLocal.x - center.x, clickInLocal.y - center.y, 0)
				val turnAxis = if (direction.x * direction.x + direction.y * direction.y < EPSILON)
						new Point3D(1, 0, 0)
					else if (direction.x.abs > direction.y.abs)
						new Point3D(0, direction.y, 0)
					else
						new Point3D(direction.x, 0, 0)
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
		]
		onMouseClicked = clickHandler
	}

	def setFront(Node front) {
		this.front = front
		pane.children += front
		front.visible = isCurrentFront
	}

	def setBack(Node back) {
		this.back = back
		pane.children += back
		back.visible = !isCurrentFront
	}

	override layoutChildren() {
		super.layoutChildren
		if(front != null && back != null) {
			back.layoutX = front.layoutX - (back.layoutBounds.width - front.layoutBounds.width) / 2 - back.layoutBounds.minX	
			back.layoutY = front.layoutY - (back.layoutBounds.height - front.layoutBounds.height) / 2 - back.layoutBounds.minY
		}
	}

	def getCurrentVisible() {
		if(isCurrentFront) front else back
	}

	def getCurrentInvisible() {
		if(isCurrentFront) back else front
	}

}
