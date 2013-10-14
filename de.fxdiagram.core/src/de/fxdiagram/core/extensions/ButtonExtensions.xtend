package de.fxdiagram.core.extensions

import javafx.geometry.Side
import javafx.scene.paint.Color
import javafx.scene.shape.SVGPath
import javafx.scene.shape.StrokeLineCap

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

class ButtonExtensions {
	
	def static getTriangleButton(Side side, String tooltip) {
		new SVGPath => [
			content = switch side {
				case Side.TOP: "m 0,11 9,-11 9,11 z"
				case Side.BOTTOM: "m 0,0 9,11 9,-11 z"
				case Side.LEFT: "m 11,0 -11,9 11,9 z"
				case Side.RIGHT: "m 0,0 11,9 -11,9 z"
			}
			fill = Color.WHITE
			stroke = Color.DARKGREEN
			strokeWidth = 3.5
			it.tooltip = tooltip
		]
	}
	
	def static getFilledTriangle(Side side, String tooltip) {
		new SVGPath => [
			content = switch side {
				case Side.TOP: "m 0,7 6,-7 6,7 z"
				case Side.BOTTOM: "m 0,0 6,7 6,-7 z"
				case Side.LEFT: "m 7,0 -7,6 7,6 z"
				case Side.RIGHT: "m 0,0 7,6 -7,6 z"
			}
			fill = Color.DARKGREEN
			stroke = Color.DARKGREEN
			strokeWidth = 3.5
			it.tooltip = tooltip
		]
	}
	
	def static getArrowButton(Side side, String tooltip) {
		new SVGPath => [
			content = switch side {
				case Side.TOP: "m 0,9 7,-9 7,9"
				case Side.BOTTOM: "m 0,0 7,9 7,-9"
				case Side.LEFT: "m 9,0 -9,7 9,7"
				case Side.RIGHT: "m 0,0 9,7 -9,7"
			}
			stroke = Color.DARKGREEN
			fill = Color.TRANSPARENT
			strokeWidth = 3.5
			strokeLineCap = StrokeLineCap.ROUND
			it.tooltip = tooltip
		]
	}
}