package de.fxdiagram.lib.animations

import de.fxdiagram.core.XNode
import java.util.Map
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.ParallelTransition
import javafx.animation.SequentialTransition
import javafx.animation.Timeline
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.animation.Transition

class Inflator {
	
	XNode host
	
	Pane container
	
	Map<VBox, Rectangle> inflatable2spacer = newLinkedHashMap
	 
	new(XNode host, Pane container) {
		this.host = host
		this.container = container
		
	}
	
	def addInflatable(VBox inflatable, int index) {
		val spacer = new Rectangle(0, 0, 0, 0) => [
			opacity = 0
		]
		container.children.add(index, spacer)
		inflatable2spacer.put(inflatable, spacer)
	}


	def getInflateAnimation() {
		if(inflatable2spacer.empty)
			return null
		val containerSize = calculateSize(container)
		val padding = container.padding
		val deflatedSize = new Dimension2D(
			containerSize.width - padding.left - padding.right,
			containerSize.height - padding.top - padding.bottom)
		new SequentialTransition => [
			delay = 300.millis
			children += inflate(deflatedSize)
			children += appear	
		]
	}
	
	protected def inflate(Dimension2D deflatedSize) {
		new ParallelTransition => [ pt |
			var inflatedWidth = deflatedSize.width
			var inflatedHeight = 0.0
			for(it: inflatable2spacer.entrySet) {
				val inflatable = key
				val spacer = value
				val size = calculateSize(inflatable)
				inflatedWidth = max(inflatedWidth, size.width)
				inflatedHeight += size.height
				pt.children += new Timeline => [
					cycleCount = 1
					autoReverse = false
					keyFrames += new KeyFrame(
						0.millis,
						new KeyValue(spacer.widthProperty, deflatedSize.width),
						new KeyValue(spacer.heightProperty, 0)
					)	
					keyFrames += new KeyFrame(
						500.millis,
						new KeyValue(spacer.widthProperty, size.width),
						new KeyValue(spacer.heightProperty, size.height)
					)	
				]
			}
			val inflatedHostPos = getInflatedHostPosition(deflatedSize, inflatedWidth, inflatedHeight)
			pt.children += new Timeline => [
				autoReverse = false
				keyFrames += new KeyFrame(
					500.millis,
					new KeyValue(host.layoutXProperty, inflatedHostPos.x),
					new KeyValue(host.layoutYProperty, inflatedHostPos.y)
				)
			] 
			pt.onFinished = [
				for(it: inflatable2spacer.entrySet) {
					val inflatable = key
					val spacer = value
					inflatable.children.forEach[
						opacity = 0
					]
					val siblings = container.children
					siblings.set(siblings.indexOf(spacer), inflatable)
				}
			]
		]
	}	
	
	protected def appear() {
		val contents = inflatable2spacer.keySet.map[children].flatten
		if(contents.empty) {
			new Transition() {
				override protected interpolate(double frac) {
				}
			}
		} else {
			new SequentialTransition => [ 
				children += (contents).map[ child |
					new FadeTransition => [
						node = child
						duration = 30.millis
						fromValue = 0
						toValue = 1
					] 
				]
			]
		} 
	}
	
	protected def getInflatedHostPosition(Dimension2D deflatedSize, double inflatedWidth, double inflatedHeight) {
		new Point2D(
			host.layoutX - switch host.placementHint {
				case LEFT:
					inflatedWidth - deflatedSize.width
				case RIGHT:
					0
				default:
					0.5 * (inflatedWidth - deflatedSize.width)
			},
			host.layoutY - switch host.placementHint {
				case TOP:
					inflatedHeight
				case BOTTOM:
					0
				default:
					0.5 * inflatedHeight
			})
	}
	
	
	protected def calculateSize(Pane node) {
		node.autosize
		new Dimension2D(node.width, node.height)
	}
	
}