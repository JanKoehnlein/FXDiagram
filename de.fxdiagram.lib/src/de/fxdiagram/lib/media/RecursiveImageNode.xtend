package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractOpenBehavior
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
import javafx.scene.shape.StrokeType

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import static extension javafx.util.Duration.*

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height', 'image', 'x', 'y', 'scale'])
class RecursiveImageNode extends XNode implements SvgExportable {

	@FxProperty Image image
	@FxProperty double x
	@FxProperty double y
	@FxProperty double scale

	DoubleProperty actualWidthProperty = new SimpleDoubleProperty
	DoubleProperty actualHeightProperty = new SimpleDoubleProperty

	FirstRecursiveImageNode pivot
	
	boolean isZoomedIn 

	new() {
		super('Recursive image')
	}
	
	protected override createNode() {
		pivot = new FirstRecursiveImageNode(this)
		return createPane => [
			children += pivot
		]
	}

	override doActivate() {
		super.doActivate
		tooltip = 'Double-click to zoom in'
		onMouseClicked = [
			if (clickCount == 2) {
				if(isZoomedIn)
					zoomOut
				else
					zoomIn
			}
		]
		val AbstractOpenBehavior openBehavior = [| if(isZoomedIn) zoomOut else zoomIn ] 
		addBehavior(openBehavior)
		pivot.activate
	}
	
	protected def void zoomIn() {
		if(isZoomedIn)
			return
		val centerInDiagram = localToRootDiagram(
			actualWidthProperty.get * 0.5 * (1 - scale + 2 * this.x / actualWidthProperty.get) *
				(1 / (1 - scale)),
			actualHeightProperty.get * 0.5 * (1 - scale + 2 * this.y / actualHeightProperty.get) *
				(1 / (1 - scale))
		)
		new ScrollToAndScaleTransition(root, centerInDiagram, 500) => [
			duration = 5.seconds
			interpolator = [
				exp(log(10000) * it) / 10000
			]
			play
		]
		isZoomedIn = true
	}

	protected def void zoomOut() {
		if(!isZoomedIn)
			return
		val centerInDiagram = localToRootDiagram(boundsInLocal.center)
		new ScrollToAndScaleTransition(root, centerInDiagram, 1) => [
			duration = 5.seconds
			interpolator = [
				log(it * 10000)/log(10000)
			]
			play
		]
		isZoomedIn = false
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
				strokeType = StrokeType.INSIDE
			]
			updateActualDimension(imageView.boundsInLocal)
			imageView.boundsInLocalProperty.addListener [ property, oldValue, newValue |
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
		super(parent.name + '_')
		this.recursiveImageNode = parent
	}

	override protected createNode() {
		val pane = recursiveImageNode.createPane
		panes.push(pane)
		pane
	}
	
	override doActivate() {
		super.doActivate()
		layoutXProperty.bindBidirectional(recursiveImageNode.xProperty)
		layoutYProperty.bindBidirectional(recursiveImageNode.yProperty)
		scaleXProperty.bind(recursiveImageNode.scaleProperty)
		scaleYProperty.bind(recursiveImageNode.scaleProperty)
		updateChildPanes
		diagram.canvasTransform.scaleProperty.addListener [ prop, oldVal, newVal |
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
