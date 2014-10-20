package de.fxdiagram.xtext.xbase

import com.sun.javafx.tk.Toolkit
import de.fxdiagram.core.XNode
import java.util.List
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.SequentialTransition
import javafx.animation.Timeline
import javafx.geometry.Dimension2D
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class CompartmentInflator {
	
	static def inflate(XNode parent, List<Text> labels, Pane contentArea, double minWidth) {
		val maxLabelSize = (labels
			.map[dimension] + #[new Dimension2D(minWidth, 0)])
			.reduce[new Dimension2D(max($0.width, $1.width), max($0.height, $1.height))]
		val spacer = new Rectangle(0, 0, minWidth, 0) => [
			opacity = 0
		]
		contentArea.children += spacer
		val allChildrenHeight = maxLabelSize.height * labels.size
		val endX = parent.layoutX - switch parent.placementHint {
			case TOP,
			case BOTTOM:
				0.5 * (maxLabelSize.width - minWidth)
			case LEFT:
				maxLabelSize.width - minWidth
			default:
				0	
		} 
		val endY = parent.layoutY - switch parent.placementHint {
			case LEFT,
			case RIGHT:
				0.5 * allChildrenHeight
			case TOP:
				allChildrenHeight
			default:
				0	
		} 
		new SequentialTransition => [
			children += new Timeline => [
				delay = 300.millis
				cycleCount = 1
				autoReverse = false
				keyFrames += new KeyFrame(
					300.millis,
					new KeyValue(parent.layoutXProperty, endX),
					new KeyValue(parent.layoutYProperty, endY),
					new KeyValue(spacer.widthProperty, maxLabelSize.width),
					new KeyValue(spacer.heightProperty, allChildrenHeight)
				) 
				onFinished = [
					contentArea => [
						children -= spacer
						children += new VBox => [
							children += labels
						]
					] 
				]
			]
			children += labels.map [ label |
				new FadeTransition => [
					node = label
					duration = 30.millis
					fromValue = 0
					toValue = 1
				]
			]
			play
		]
	}
	
	private static def getFontLoader() {
		Toolkit.toolkit.fontLoader
	}
	
	static def Dimension2D getDimension(Text it) {
		new Dimension2D(
			fontLoader.computeStringWidth(text, font),
			fontLoader.getFontMetrics(font).lineHeight)
	}
}