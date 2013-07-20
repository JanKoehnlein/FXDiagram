package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.List
import javafx.scene.text.Text
import javafx.scene.effect.Effect
import javafx.scene.effect.DropShadow
import javafx.geometry.VPos
import javafx.geometry.Point2D
import static extension java.lang.Math.*
import javafx.scene.transform.Affine
import static extension de.fxdiagram.core.geometry.TransformExtensions.*

class XConnectionLabel extends XShape {

	@FxProperty XConnection connection
	@FxProperty Text text

	MoveBehavior moveBehavior
	Effect selectionEffect

	new(XConnection connection) {
		this.connection = connection
		connection.label = this
		text = new Text => [
			textOrigin = VPos.TOP
		]
		node = text
		selectionEffect = new DropShadow() => [
			offsetX = 4.0
			offsetY = 4.0
		]
	}

	override doActivate() {
		moveBehavior = new MoveBehavior(this)
		moveBehavior.activate()
		selectedProperty.addListener [ observable, oldValue, newValue |
			if (newValue) {
				effect = selectionEffect
				scaleX = 1.05
				scaleY = 1.05
				toFront
			} else {
				effect = null
				scaleX = 1.0
				scaleY = 1.0
			}
		]
	}

	def protected place(List<XControlPoint> list) {
		transforms.clear
		if (list.size == 2) {
			val centerX = 0.5 * (list.get(0).layoutX + list.get(1).layoutX)
			val centerY = 0.5 * (list.get(0).layoutY + list.get(1).layoutY)
			val dx = list.get(1).layoutX - list.get(0).layoutX
			val dy = list.get(1).layoutY - list.get(0).layoutY
			var angle = atan2(dy, dx)
			val labelDx = -boundsInLocal.width / 2
			var labelDy = 1
			if (abs(angle) > PI / 2) {
				if (angle < 0)
					angle = angle + PI
				else if (angle > 0)
					angle = angle - PI
			}
			val transform = new Affine
			transform.translate(labelDx, labelDy)
			transform.rotate(angle.toDegrees)
			layoutX = transform.tx + centerX
			layoutY = transform.ty + centerY
			transform.tx = 0
			transform.ty = 0
			transforms += transform
		}
	}
	
	override getMoveBehavior() {
		moveBehavior
	}
}
