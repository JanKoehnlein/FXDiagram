package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import de.fxdiagram.core.tools.actions.ScrollToAndScaleTransition
import java.util.Deque
import java.util.LinkedList
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Bounds
import javafx.scene.Group
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Rectangle

import static extension de.fxdiagram.core.Extensions.*
import static extension javafx.util.Duration.*
import javafx.animation.Interpolator

class RecursiveImageNode extends XNode implements SvgExportable {

	@FxProperty Image image
	@FxProperty double x
	@FxProperty double y
	@FxProperty double scale

	DoubleProperty actualWidthProperty = new SimpleDoubleProperty  
	DoubleProperty actualHeightProperty = new SimpleDoubleProperty
	  
	FirstRecursiveImageNode pivot
	
	new(Image image, double x, double y, double scale) {
		this.image = image
		this.x = x
		this.y = y
		this.scale = scale
		pivot = new FirstRecursiveImageNode(this)
		node = createPane => [
			children += pivot
		]
	}

	override doActivate() {
		super.doActivate
		pivot.activate
		onMouseClicked = [
			if(clickCount == 2) {
				val centerInDiagram = localToRootDiagram(
					actualWidthProperty.get * 0.5 * (1-scale+2*this.x/actualWidthProperty.get) * (1/(1-scale)), 
					actualHeightProperty.get * 0.5 * (1-scale+2*this.y/actualHeightProperty.get) * (1/(1-scale))
				)
				new ScrollToAndScaleTransition(rootDiagram, centerInDiagram, 10000) => [
					duration = 5.seconds
					interpolator = Interpolator.SPLINE(0.5, 0, 1, 0.5)
					play
				]
			}
		]
	}

	protected def Group createPane() {
		val pane = new Group => [
			val imageView = new ImageView
			children += imageView => [
				it.imageProperty.bind(imageProperty)
				preserveRatio = true
				fitWidthProperty.bind(this.widthProperty)
				fitHeightProperty.bind(this.heightProperty)
			]
			clip = new Rectangle => [
				x = 0
				y = 0
				widthProperty.bind(actualWidthProperty)
				heightProperty.bind(actualHeightProperty)
			]
			updateActualDimension(imageView.boundsInLocal)
			imageView.boundsInLocalProperty.addListener [
				property, oldValue, newValue |
				updateActualDimension(newValue)
			]
		]
		pane
	}
	
	protected def updateActualDimension(Bounds newValue) {
		actualWidthProperty.set(newValue.width)
		actualHeightProperty.set(newValue.height)
	}

	override toSvgElement(extension SvgExporter exporter) {
		exporter.snapshotToSvgElement(this.node)
	}
}

class FirstRecursiveImageNode extends XNode {

	RecursiveImageNode recursiveImageNode

	Deque<Group> panes = new LinkedList<Group>

	new(RecursiveImageNode parent) {
		this.recursiveImageNode = parent
		val group = parent.createPane
		node = group
		panes.push(group)
	}

	override doActivate() {
		super.doActivate()
		layoutXProperty.bindBidirectional(recursiveImageNode.xProperty)
		layoutYProperty.bindBidirectional(recursiveImageNode.yProperty)
		scaleXProperty.bind(recursiveImageNode.scaleProperty)
		scaleYProperty.bind(recursiveImageNode.scaleProperty)
		updateChildPanes
		rootDiagram.scaleProperty.addListener [ prop, oldVal, newVal |
			updateChildPanes
		]
	}
	
	override selectionFeedback(boolean isSelected) {
	}
	
	def void updateChildPanes() {
		while (!panes.empty) {
			val child = panes.pop
			val parent = panes.peek
			val bounds = child.localToScene(child.boundsInLocal)
			val area = bounds.width * bounds.height
			if (area <= 10) {
				if (parent != null) {
					parent.children -= child
				} else {
					// never remove the first image
					panes.push(child)
					return
				}
			} else if (area > 500) {
				val grandChild = createScaledPane
				child.children += grandChild
				panes.push(child)
				panes.push(grandChild)
				
			} else {
				panes.push(child)
				return
			}
		}
	}
	
	def createScaledPane() {
		recursiveImageNode.createPane() => [
			scaleXProperty.bind(recursiveImageNode.scaleProperty)
			scaleYProperty.bind(recursiveImageNode.scaleProperty)
			layoutXProperty.bind(recursiveImageNode.xProperty)
			layoutYProperty.bind(recursiveImageNode.yProperty)
		]
	}
}
