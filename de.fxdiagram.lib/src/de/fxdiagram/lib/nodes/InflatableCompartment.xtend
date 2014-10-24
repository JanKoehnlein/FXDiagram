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
import org.eclipse.xtend.lib.annotations.Data

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.TextExtensions.*
import javafx.geometry.Insets

class InflatableCompartment extends Parent {

	XNode host
	VBox labelContainer 
	boolean isInflated = false
	Insets padding

	new(XNode host, Insets padding) {
		this.host = host
		this.padding = padding
		labelContainer = new VBox => [
			it.padding = padding
		]
		managed = true
	}

	def add(Text label) {
		labelContainer.children += label
	}

	protected def getLabels() {
		labelContainer.children.filter(Text)
	}

	def getInflateAnimation(double deflatedWidth) {
		getInflateAnimation(new InflationData(this, deflatedWidth))
	}

	def populate() {
		if(isInflated)
			return;
		children.setAll(labelContainer)
	}

	protected def getInflateAnimation(InflationData data) {
		val spacer = new Rectangle(0, 0, data.deflatedWidth, 0) => [
			opacity = 0
		]
		children.setAll(spacer)
		return new SequentialTransition => [
			children += new Timeline => [
				cycleCount = 1
				autoReverse = false
				keyFrames += new KeyFrame(
					200.millis,
					new KeyValue(host.layoutXProperty, data.endX),
					new KeyValue(host.layoutYProperty, data.endY),
					new KeyValue(spacer.widthProperty, data.inflatedWidth),
					new KeyValue(spacer.heightProperty, data.inflatedHeight)
				)
				onFinished = [
					labels.forEach[opacity = 0]
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
			onFinished = [
				isInflated = true
			]
		]
	}

	@Data 
	static class InflationData {

		val double deflatedWidth
		val double inflatedWidth
		val double inflatedHeight
		val double startX
		val double startY
		val double endX
		val double endY

		new(InflatableCompartment compartment, double deflatedWidth) {
			this.deflatedWidth = deflatedWidth
			val labels = compartment.labels
			val host = compartment.host
			val maxLabelSize = (labels.map[offlineDimension] + #[new Dimension2D(deflatedWidth, 0)]).reduce[
				new Dimension2D(max($0.width, $1.width), max($0.height, $1.height))]
			inflatedWidth = maxLabelSize.width + compartment.padding.left + compartment.padding.right
			inflatedHeight = maxLabelSize.height * labels.size + compartment.padding.top + compartment.padding.bottom
			startX = host.layoutX
			endX = host.layoutX - switch host.placementHint {
				case LEFT:
					inflatedWidth - deflatedWidth
				case RIGHT:
					0
				default:
					0.5 * (inflatedWidth - deflatedWidth)
			}
			startY = host.layoutY
			endY = host.layoutY - switch host.placementHint {
				case TOP:
					inflatedHeight
				case BOTTOM:
					0
				default:
					0.5 * inflatedHeight
			}
		}
	}
}
