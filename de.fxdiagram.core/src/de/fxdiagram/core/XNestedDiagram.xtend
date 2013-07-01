package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import javafx.beans.value.ChangeListener
import javafx.geometry.Bounds
import javafx.scene.Group

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.transform.BoundsExtensions.*
import javafx.scene.shape.Rectangle

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

	new() {
		children += nodeLayer
		children += buttonLayer
		visibleProperty.addListener [ property, oldVal, newVal |
			connections.forEach[visible = newVal]
		]
		boundsInLocalListener = [ prop, oldVal, newVal | scaleToFit ]
		layoutListener = [ prop, oldVal, newVal | scaleToFit ]
		widthProperty.addListener [ prop, oldVal, newVal | scaleToFit ]
		heightProperty.addListener [ prop, oldVal, newVal | scaleToFit ]
	}

	def setScale(double scale) {
		scaleX = scale
		scaleY = scale
	}

	def scaleToFit() {
		if(nodes.empty) {
			scale = 1
			clip = null
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
				x = myBounds.minX
				width = myBounds.width
				if(newScaleX == 1) {
					val delta = width / newScale - myBounds.width
					x = x - 0.5 * delta
					width = width + delta
				} 
				y = myBounds.minY
				height = myBounds.height
				if(newScaleY == 1) {
					val delta = height / newScale - myBounds.height
					y = y - 0.5 * delta
					height = height + delta
				}
			]
		}
	}

	def setContentsInitializer((XNestedDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}

	override doActivate() {
		super.doActivate()
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

	override internalAddNode(XNode node) {
		super.internalAddNode(node)
		node.boundsInLocalProperty.addListener(boundsInLocalListener)
		node.layoutXProperty.addListener(layoutListener)
		node.layoutYProperty.addListener(layoutListener)
		parentDiagram.internalAddNode(node)
	}

	override internalAddButton(XRapidButton button) {
		super.internalAddButton(button)
		parentDiagram.internalAddButton(button)
	}

	override internalRemoveNode(XNode node) {
		super.internalRemoveNode(node)
		node.boundsInLocalProperty.removeListener(boundsInLocalListener)
		node.layoutXProperty.removeListener(layoutListener)
		node.layoutYProperty.removeListener(layoutListener)
		parentDiagram.internalRemoveNode(node)
	}

	override internalRemoveButton(XRapidButton button) {
		super.internalRemoveButton(button)
		parentDiagram.internalRemoveButton(button)
	}

	def protected getParentDiagram() {
		parent?.diagram
	}
}
