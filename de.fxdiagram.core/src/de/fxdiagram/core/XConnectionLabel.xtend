package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import java.util.List
import javafx.beans.property.StringProperty
import javafx.scene.Parent
import javafx.scene.text.Text
import javafx.scene.transform.Affine

import static java.lang.Math.*

import static extension de.fxdiagram.core.geometry.TransformExtensions.*

class XConnectionLabel extends Parent implements XActivatable {

	@FxProperty XConnection connection

	@FxProperty @ReadOnly boolean isActive

	Text text

	new(XConnection connection) {
		this.connection = connection
		connection.label = this
		text = new Text
		children += text
	}

	def getText() {
		text.text
	}

	def setText(String text) {
		this.text.text = text
	}

	def StringProperty textProperty() {
		text.textProperty
	}

	override activate() {
		if (!isActive)
			doActivate
		isActiveProperty.set(true)
	}

	def doActivate() {
		place(connection.points)
		connection.points.addListener [
			place(list)
		]
		boundsInLocalProperty.addListener [ element, oldVlaue, newValue |
			place(connection.points)
		]
	}

	def protected place(List<? extends Double> list) {
		if (list.size >= 4) {
			val dx = list.get(list.size - 2) - list.get(0)
			val dy = list.get(list.size - 1) - list.get(1)
			var angle = atan2(dy, dx)
			val labelDx = -boundsInLocal.width / 2
			var labelDy = - 4
			if (abs(angle) > PI / 2) {
				if (angle < 0)
					angle = angle + PI
				else if (angle > 0)
					angle = angle - PI
				labelDy = - 4
			}
			val transform = new Affine
			transform.translate(labelDx, labelDy)
			transform.rotate(angle * 180 / PI)

			val xPos = (list.get(0) + list.get(list.size - 2)) / 2
			val yPos = (list.get(1) + list.get(list.size - 1)) / 2
			transform.translate(xPos, yPos)

			transforms.clear
			transforms += transform
		}
	}
}
