package de.fxdiagram.lib.nodes

import de.fxdiagram.core.XNode
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.SequentialTransition
import javafx.animation.Timeline
import javafx.geometry.Dimension2D
import javafx.scene.Parent
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.TextExtensions.*

class InflatableCompartment extends Parent {
	
	XNode containerNode
	double deflatedWidth
	VBox labelContainer = new VBox
	
	new(XNode containerNode, double deflatedWidth) {
		this.containerNode = containerNode
		this.deflatedWidth = deflatedWidth
		managed = true
	}
	
	def add(Text label) {
		labelContainer.children += label
	}
	
	protected def getLabels() {
		labelContainer.children.filter(Text)
	}
	
	def inflate() {
		val maxLabelSize = (labels
			.map[offlineDimension] + #[new Dimension2D(deflatedWidth, 0)])
			.reduce[new Dimension2D(max($0.width, $1.width), max($0.height, $1.height))]
		val spacer = new Rectangle(0, 0, deflatedWidth, 0) => [
			opacity = 0
		]
		children += spacer
		val allChildrenHeight = maxLabelSize.height * labels.size
		val endX = containerNode.layoutX - switch containerNode.placementHint {
			case LEFT:
				maxLabelSize.width - deflatedWidth
			case RIGHT:
				0	
			default:
				0.5 * (maxLabelSize.width - deflatedWidth)
		} 
		val endY = containerNode.layoutY - switch containerNode.placementHint {
			case TOP:
				allChildrenHeight
			case BOTTOM:
				0
			default:
				0.5 * allChildrenHeight
		} 
		new SequentialTransition => [
			children += new Timeline => [
				delay = 300.millis
				cycleCount = 1
				autoReverse = false
				keyFrames += new KeyFrame(
					300.millis,
					new KeyValue(containerNode.layoutXProperty, endX),
					new KeyValue(containerNode.layoutYProperty, endY),
					new KeyValue(spacer.widthProperty, maxLabelSize.width),
					new KeyValue(spacer.heightProperty, allChildrenHeight)
				) 
				onFinished = [
					labelContainer.children.forEach[ opacity = 0 ]
					populate
					parent.requestLayout
					parent.layout
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
	
	def populate() {
		if(labelContainer.parent == this)
			return;
		children.clear
		children += labelContainer
	}
}