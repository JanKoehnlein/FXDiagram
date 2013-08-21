package de.fxdiagram.core

import de.fxdiagram.core.behavior.MoveBehavior
import java.util.List
import javafx.fxml.FXMLLoader
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.transform.Affine

import static de.fxdiagram.core.XControlPointType.*
import static de.fxdiagram.core.extensions.Point2DExtensions.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import static extension java.lang.Math.*

class XControlPoint extends XShape {

	MoveBehavior<XControlPoint> moveBehavior

	XControlPointType type

	new() {
		setType(CONTROL_POINT)
	}

	def getType() {
		type
	}
	
	def setType(XControlPointType type) {
		this.type = type
		switch type {
			case ANCHOR: {
				node = new Circle => [
					radius = 3
					stroke = Color.BLUE
					fill = Color.WHITE
				]
			}
			case CONTROL_POINT: {
				node = newMagnet
			}
			case INTERPOLATED: {
				node = new Circle => [
					radius = 5
					stroke = Color.RED
					fill = Color.WHITE
				]
			}
		}
		if (type != ANCHOR)
			moveBehavior = new MoveBehavior(this)
	}

	override protected doActivate() {
		moveBehavior?.activate
	}

	override selectionFeedback(boolean isSelected) {
		if (isSelected)
			switch type {
				case CONTROL_POINT:
					node.effect = new DropShadow
				case INTERPOLATED:
					(node as Circle).fill = Color.RED
			}
		else
			switch type {
				case CONTROL_POINT:
					node.effect = null
				case INTERPOLATED:
					(node as Circle).fill = Color.WHITE
			}
	}

	override isSelectable() {
		type != XControlPointType.ANCHOR && super.isSelectable()
	}

	override getMoveBehavior() {
		moveBehavior
	}

	override toString() {
		'''XControlPoint at («layoutX»,«layoutY»)'''
	}

	def update(List<XControlPoint> siblings) {
		if (type == CONTROL_POINT) {
			val index = siblings.indexOf(this)
			if (index > 0 && index < siblings.size - 1) {
				val predecessor = siblings.get(index - 1)
				val successor = siblings.get(index + 1)
				val dx = successor.layoutX - predecessor.layoutX
				val dy = successor.layoutY - predecessor.layoutY
				var angle = atan2(dy, dx).toDegrees
				if (isClockwise(layoutX, layoutY, 
					successor.layoutX, successor.layoutY,
					predecessor.layoutX, predecessor.layoutY)) 
					angle = angle + 180
				val trafo = new Affine
				trafo.translate(-0.5 * node.layoutBounds.width, -node.layoutBounds.height - 5)
				trafo.rotate(angle)
				transforms.setAll(trafo)
			}
		}
	}

	protected def Node newMagnet() {
		new Group => [
			children += FXMLLoader.load(class.getResource('/icons/Magnet.fxml'))
		]
	}
}

enum XControlPointType {
	ANCHOR,
	INTERPOLATED,
	CONTROL_POINT
}
