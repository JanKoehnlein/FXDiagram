package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.geometry.Bounds
import javafx.scene.Group
import javafx.scene.shape.Rectangle

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.geometry.BoundsExtensions.*

/**
 * A nested diagram needs a width and a height to be set in order to rescale itself.
 */
class XNestedDiagram extends XAbstractDiagram {

	@FxProperty double width = 80
	@FxProperty double height = 60

	(XNestedDiagram)=>void contentsInitializer

	Group nodeLayer = new Group
	Group buttonLayer = new Group

	ChangeListener<Bounds> boundsInLocalListener
	ChangeListener<Number> layoutListener
	
	Rectangle fillRectangle

	new() {
		children += nodeLayer
		children += buttonLayer
		visibleProperty.addListener [ property, oldVal, newVal |
			connections.forEach[visible = newVal]
		]
		boundsInLocalListener = [prop, oldVal, newVal|scaleToFit]
		layoutListener = [prop, oldVal, newVal|scaleToFit]
		widthProperty.addListener[prop, oldVal, newVal|scaleToFit]
		heightProperty.addListener[prop, oldVal, newVal|scaleToFit]
		fillRectangle = new Rectangle => [
			opacity = 0
			mouseTransparent = true
		]
	}

	def setScale(double scale) {
		scaleX = scale
		scaleY = scale
	}

	def scaleToFit() {
		if (nodes.empty) {
			scale = 1
			clip = null
			if(fillRectangle != null)
				children -= fillRectangle
		} else {
			val myBounds = nodes.map[layoutBounds.translate(layoutX, layoutY)].reduce[b0, b1|b0 + b1]
			val newScaleX = if (myBounds.width != 0)
					min(1, width / myBounds.width)
				else
					1
			val newScaleY = if (myBounds.height != 0)
					min(1, height / myBounds.height)
				else
					1
			val newScale = min(newScaleX, newScaleY)
			scale = newScale
			
			clip = new Rectangle => [
				fit(newScale, newScaleX, newScaleY, myBounds)
			]
			fillRectangle.fit(newScale, newScaleX, newScaleY, myBounds)
			if(fillRectangle.parent == null)
				children += fillRectangle
		}
	}
	
	protected def fit(Rectangle it, double newScale, double newScaleX, double newScaleY, Bounds allNodesBounds) {
		arcWidth = 22 / newScale
		arcHeight = 22 / newScale
		x = allNodesBounds.minX
		width = allNodesBounds.width
		if (newScaleX > newScaleY) {
			val delta = this.width / newScale - allNodesBounds.width
			x = x - 0.5 * delta
			width = width + delta
		}
		y = allNodesBounds.minY
		height = allNodesBounds.height
		if (newScaleY > newScaleX) {
			val delta = this.height / newScale - allNodesBounds.height
			y = y - 0.5 * delta
			height = height + delta
		}
	}

	def setContentsInitializer((XNestedDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}

	override doActivate() {
		super.doActivate()
		nodes.addListener [ ListChangeListener.Change<? extends XNode> change |
			while (change.next) {
				if (change.wasAdded)
					change.addedSubList.forEach [
						boundsInLocalProperty.addListener(boundsInLocalListener)
						layoutXProperty.addListener(layoutListener)
						layoutYProperty.addListener(layoutListener)
					]
				if (change.wasRemoved)
					change.removed.forEach [
						boundsInLocalProperty.removeListener(boundsInLocalListener)
						layoutXProperty.removeListener(layoutListener)
						layoutYProperty.removeListener(layoutListener)
					]
			}
		]
		buttonLayer.layoutBoundsProperty.addListener(boundsInLocalListener)
		contentsInitializer?.apply(this)
	}

	override getNodeLayer() {
		nodeLayer
	}

	override getConnectionLayer() {
		parentDiagram.connectionLayer
	}

	override getButtonLayer() {
		buttonLayer
	}

	def protected getParentDiagram() {
		parent?.diagram
	}
}
