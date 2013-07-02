package de.fxdiagram.lib.shapes

import javafx.animation.RotateTransition
import javafx.animation.SequentialTransition
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane

import static extension de.fxdiagram.core.transform.BoundsExtensions.*
import static extension java.lang.Math.*
import static extension javafx.util.Duration.*

class FlipNode extends StackPane {

	Node front

	Node back

	boolean isCurrentFront = true

	EventHandler<MouseEvent> clickHandler

	new() {
		clickHandler = [ event |
			if (front != null && back != null) {
				val clickInScene = new Point2D(event.sceneX, event.sceneY)
				val clickInLocal = currentVisible.sceneToLocal(clickInScene)
				val center = boundsInLocal.center
				val direction = new Point3D(clickInLocal.x - center.x, clickInLocal.y - center.y, 0)
				val turnAxis = if (direction.x * direction.x + direction.y * direction.y < 1E-6)
						new Point3D(1, 0, 0)
					else if (direction.x.abs > direction.y.abs)
						new Point3D(0, direction.y, 0)
					else
						new Point3D(direction.x, 0, 0)
				new SequentialTransition => [
					children += new RotateTransition => [
						node = currentVisible
						duration = 250.millis
						axis = turnAxis
						fromAngle = 0
						toAngle = 90
						onFinished = [
							currentVisible.visible = false
							isCurrentFront = ! isCurrentFront
							currentVisible.visible = true
						]
					]
					children += new RotateTransition => [
						node = currentInvisible
						duration = 250.millis
						axis = turnAxis
						fromAngle = 90
						toAngle = 0
					]
					play
				]
			}
		]
		onMousePressed = clickHandler
	}

	def setFront(Node front) {
		this.front = front
		front.onMousePressed = clickHandler
		children.add(front)
		front.visible = isCurrentFront
	}

	def setBack(Node back) {
		this.back = back
		back.onMousePressed = clickHandler
		children.add(back)
		back.visible = !isCurrentFront
	}

	def getCurrentVisible() {
		if(isCurrentFront) front else back
	}

	def getCurrentInvisible() {
		if(isCurrentFront) back else front
	}

}
