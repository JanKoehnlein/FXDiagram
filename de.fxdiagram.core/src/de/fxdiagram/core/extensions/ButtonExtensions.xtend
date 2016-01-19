package de.fxdiagram.core.extensions

import de.fxdiagram.core.XButton
import javafx.geometry.Side
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.SVGPath
import javafx.scene.shape.StrokeLineCap

import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

class ButtonExtensions {
	
	def static getTriangleButton(Side side, String tooltip) {
		new SVGPath => [
			content = switch side {
				case TOP: "m 0,11 9,-11 9,11 z"
				case BOTTOM: "m 0,0 9,11 9,-11 z"
				case LEFT: "m 11,0 -11,9 11,9 z"
				case RIGHT: "m 0,0 11,9 -11,9 z"
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
				case TOP: "m 0,7 6,-7 6,7 z"
				case BOTTOM: "m 0,0 6,7 6,-7 z"
				case LEFT: "m 7,0 -7,6 7,6 z"
				case RIGHT: "m 0,0 7,6 -7,6 z"
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
				case TOP: "m 0,9 7,-9 7,9"
				case BOTTOM: "m 0,0 7,9 7,-9"
				case LEFT: "m 9,0 -9,7 9,7"
				case RIGHT: "m 0,0 9,7 -9,7"
			}
			stroke = Color.DARKGREEN
			fill = Color.TRANSPARENT
			strokeWidth = 3.5
			strokeLineCap = StrokeLineCap.ROUND
			it.tooltip = tooltip
		]
	}
	
	def static getTargetButton(MouseEvent event) {
		if (event.target instanceof Node)
			getContainerButton(event.target as Node)
		else
			null
	}
	
	def static XButton getContainerButton(Node it) {
		switch it {
			case null: null
			XButton: it
			default: getContainerButton(parent)
		}
	}
	
	def static invert(Side side) {
		switch side {
			case TOP: BOTTOM
			case BOTTOM: TOP
			case LEFT: RIGHT
			case RIGHT: LEFT
			default:
			null
		}
	}
}