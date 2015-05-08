package de.fxdiagram.lib.nodes

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractOpenBehavior
import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.animation.RotateTransition
import javafx.animation.SequentialTransition
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static javafx.scene.input.MouseButton.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension java.lang.Math.*
import static extension javafx.util.Duration.*

/**
 * An {@link XNode} with a front and a back side that can be flipped with a 3D effect 
 * on click. 
 */
@Logging
@ModelNode
class FlipNode extends XNode {

	Node front

	Node back

	Group pane 

	boolean isCurrentFront = true

	new() {}

	new(String name) {
		super(name)
	}

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}
	
	protected override createNode() {
		pane = new Group
	}
	
	override initializeGraphics() {
		super.initializeGraphics()
		if(front == null)
			LOG.severe('FlipNode.front not set')
		if(back == null)
			LOG.severe('FlipNode.back not set')
	}
	
	override doActivate() {
		super.doActivate()
		cursor = Cursor.HAND
		registerOnClick
		val AbstractOpenBehavior openBehavior = [| flip(true) ]
		addBehavior(openBehavior)
	}
	
	def registerOnClick() {
		onMouseClicked = [ 
			if (button == PRIMARY && clickCount == 2) {
				if (front != null && back != null) 
					flip(isHorizontal(it))
			}
		]
	}

	def void flip(boolean isHorizontal) {
		if(isCurrentFront)
			alignFaces(front, back)
		else 
			alignFaces(back, front)
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
	
	def alignFaces(Node fixed, Node toBeAligned) {
		toBeAligned.layoutX = fixed.layoutX - (toBeAligned.layoutBounds.width - fixed.layoutBounds.width) / 2 
			- toBeAligned.layoutBounds.minX
		toBeAligned.layoutY = fixed.layoutY - (toBeAligned.layoutBounds.height - fixed.layoutBounds.height) / 2 
			- toBeAligned.layoutBounds.minY
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
		if(this.front != null) 
			pane.children -= this.front
		this.front = front
		pane.children += front
		front.visible = isCurrentFront
	}
	
	def getFront() {
		front
	}

	def setBack(Node back) {
		if(this.back != null) 
			pane.children -= this.back
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
