package de.fxdiagram.lib.animations

import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
import java.util.Map
import javafx.animation.FadeTransition
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.ParallelTransition
import javafx.animation.SequentialTransition
import javafx.animation.Timeline
import javafx.animation.Transition
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import org.eclipse.xtend.lib.annotations.Accessors

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class Inflator {

	XNode host

	Pane container

	Map<VBox, Rectangle> inflatable2spacer = newLinkedHashMap

	Dimension2D deflatedUnpadded

	@Accessors(PUBLIC_GETTER) boolean isInflated = false

	new(XNode host, Pane container) {
		this.host = host
		this.container = container
	}

	def addInflatable(VBox inflatable, int index) {
		val spacer = new Rectangle(0, 0, 0, 0) => [
			opacity = 0
		]
		if (isInflated) {
			container.children.add(index, inflatable)
		} else {
			container.children.add(index, spacer)
		}
		inflatable2spacer.put(inflatable, spacer)
	}

	def removeInflatable(VBox inflatable) {
		val spacer = inflatable2spacer.get(inflatable)
		if (isInflated) {
			container.children -= inflatable
		} else {
			container.children -= spacer
		}
		inflatable2spacer.remove(inflatable, spacer)
	}

	def getInflateAnimation() {
		if (inflatable2spacer.empty || isInflated)
			return new EmptyTransition
		new SequentialTransition => [
			delay = 200.millis
			children += inflate
			children += appear
			onFinished = [
				isInflated = true
			]
		]
	}

	def getDeflateAnimation() {
		if (inflatable2spacer.empty || !isInflated)
			return new EmptyTransition
		new SequentialTransition => [
			children += disappear
			children += deflate
			onFinished = [
				isInflated = false
			]
		]
	}

	def getInflateCommand() {
		return new AbstractAnimationCommand() {

			override createExecuteAnimation(CommandContext context) {
				inflateAnimation
			}

			override createUndoAnimation(CommandContext context) {
				deflateAnimation
			}

			override createRedoAnimation(CommandContext context) {
				inflateAnimation
			}

		}
	}
	
	def getDeflateCommand() {
		return new AbstractAnimationCommand() {

			override createExecuteAnimation(CommandContext context) {
				deflateAnimation
			}

			override createUndoAnimation(CommandContext context) {
				inflateAnimation
			}

			override createRedoAnimation(CommandContext context) {
				deflateAnimation
			}
		}
	}
	
	def getInflatedSize() {
		calculateDeflatedUnpadded
		val unpadded = inflatedUnpadded
		val padding = container.padding
		new Dimension2D(unpadded.width + padding.left + padding.right, unpadded.height + padding.top + padding.bottom + deflatedUnpadded.height)
	}

	protected def getInflatedUnpadded() {
		var inflatedWidth = deflatedUnpadded.width
		var inflatedHeight = 0.0
		for (it : inflatable2spacer.entrySet) {
			val inflatable = key
			val size = calculateSize(inflatable)
			inflatedWidth = max(inflatedWidth, size.width)
			inflatedHeight += size.height
		}
		new Dimension2D(inflatedWidth, inflatedHeight)
	}
	
	protected def calculateDeflatedUnpadded() {
		val containerSize = calculateSize(container)
		val padding = container.padding
		deflatedUnpadded = new Dimension2D(containerSize.width - padding.left - padding.right,
			containerSize.height - padding.top - padding.bottom)
	}

	protected def inflate() {
		calculateDeflatedUnpadded
		new ParallelTransition => [ pt |
			for (it : inflatable2spacer.entrySet) {
				val inflatable = key
				val spacer = value
				val size = calculateSize(inflatable)
				pt.children += new Timeline => [
					cycleCount = 1
					autoReverse = false
					keyFrames += new KeyFrame(
						0.millis,
						new KeyValue(spacer.widthProperty, deflatedUnpadded.width),
						new KeyValue(spacer.heightProperty, 0)
					)
					keyFrames += new KeyFrame(
						200.millis,
						new KeyValue(spacer.widthProperty, size.width),
						new KeyValue(spacer.heightProperty, size.height)
					)
				]
			}
			val inflatedHostPos = getInflatedHostPosition
			pt.children += new Timeline => [
				autoReverse = false
				keyFrames += new KeyFrame(
					200.millis,
					new KeyValue(host.placementGroup.layoutXProperty, inflatedHostPos.x),
					new KeyValue(host.placementGroup.layoutYProperty, inflatedHostPos.y)
				)
			]
			pt.onFinished = [
				for (it : inflatable2spacer.entrySet) {
					val inflatable = key
					val spacer = value
					inflatable.children.forEach [
						opacity = 0
					]
					val siblings = container.children
					siblings.set(siblings.indexOf(spacer), inflatable)
				}
			]
		]
	}

	protected def deflate() {
		new ParallelTransition => [ pt |
			for (spacer : inflatable2spacer.values) {
				pt.children += new Timeline => [
					cycleCount = 1
					autoReverse = false
					keyFrames += new KeyFrame(
						200.millis,
						new KeyValue(spacer.widthProperty, deflatedUnpadded.width),
						new KeyValue(spacer.heightProperty, 0)
					)
				]
			}
			val deflatedHostPos = getDeflatedHostPosition()
			pt.children += new Timeline => [
				autoReverse = false
				keyFrames += new KeyFrame(
					200.millis,
					new KeyValue(host.placementGroup.layoutXProperty, deflatedHostPos.x),
					new KeyValue(host.placementGroup.layoutYProperty, deflatedHostPos.y)
				)
			]
		]
	}

	protected def appear() {
		val contents = getContents()
		if (contents.empty) {
			new EmptyTransition
		} else {
			new SequentialTransition => [
				children += (contents).map [ child |
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

	protected def getContents() {
		inflatable2spacer.keySet.map[children].flatten
	}

	protected def disappear() {
		val contents = getContents()
		if (contents.empty) {
			new Transition() {
				override protected interpolate(double frac) {
				}
			}
		} else {
			new ParallelTransition => [
				children += contents.map [ child |
					new FadeTransition => [
						node = child
						duration = 30.millis
						fromValue = 1
						toValue = 0
					]
				]
				onFinished = [
					inflatable2spacer.entrySet.forEach [
						val index = container.children.indexOf(key)
						value.width = key.width
						value.height = key.height
						container.children.set(index, value)
					]
				]
			]
		}
	}

	protected def getInflatedHostPosition() {
		val inflatedUnpadded = getInflatedUnpadded
		new Point2D(host.placementGroup.layoutX - switch host.placementHint {
			case LEFT:
				inflatedUnpadded.width - deflatedUnpadded.width
			case TOP,
			case BOTTOM:
				0.5 * (inflatedUnpadded.width - deflatedUnpadded.width)
			default:
				0
		}, host.placementGroup.layoutY - switch host.placementHint {
			case TOP:
				inflatedUnpadded.height
			case BOTTOM:
				0
			case LEFT,
			case RIGHT:
				0.5 * inflatedUnpadded.height
			default:
				0
		})
	}

	protected def getDeflatedHostPosition() {
		val inflatedUnpadded = getInflatedUnpadded
		new Point2D(host.placementGroup.layoutX + switch host.placementHint {
			case LEFT:
				inflatedUnpadded.width - deflatedUnpadded.width
			case RIGHT:
				0
			case TOP,
			case BOTTOM:
				0.5 * (inflatedUnpadded.width - deflatedUnpadded.width)
			default:
				0
		}, host.placementGroup.layoutY + switch host.placementHint {
			case TOP:
				inflatedUnpadded.height
			case BOTTOM:
				0
			case LEFT,
			case RIGHT:
				0.5 * inflatedUnpadded.height
			default:
				0
		})
	}

	protected def calculateSize(Pane node) {
		node.autosize
		new Dimension2D(node.width, node.height)
	}

}