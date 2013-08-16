package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XActivatable
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.geometry.Bounds
import javafx.scene.shape.Rectangle

import static java.lang.Math.*

import static extension de.fxdiagram.core.geometry.BoundsExtensions.*

class AutoScaleToFit implements XActivatable {

	@FxProperty double width = 80
	@FxProperty double height = 60

	ChangeListener<Bounds> boundsInLocalListener
	ChangeListener<Number> layoutListener
	
//	Rectangle fillRectangle

	XDiagram diagram

	new(XDiagram diagram) {
		this.diagram = diagram
		boundsInLocalListener = [prop, oldVal, newVal | scaleToFit]
		layoutListener = [prop, oldVal, newVal | scaleToFit]
		widthProperty.addListener[prop, oldVal, newVal | scaleToFit]
		heightProperty.addListener[prop, oldVal, newVal | scaleToFit]
//		fillRectangle = new Rectangle => [
//			opacity = 0
//			mouseTransparent = true
//		]
	}

	def scaleToFit() {
		if (diagram.nodes.empty) {
			diagram.scaleX = 1
			diagram.scaleY = 1

			diagram.clip = null
//			if(fillRectangle != null)
//				diagram.children -= fillRectangle
		} else {
			val myBounds = diagram.nodes
				.map[layoutBounds.translate(layoutX, layoutY)]
				.reduce[b0, b1 | b0 + b1]
			val newScaleX = if (myBounds.width != 0)
					min(1, width / myBounds.width)
				else
					1
			val newScaleY = if (myBounds.height != 0)
					min(1, height / myBounds.height)
				else
					1
			val newScale = min(newScaleX, newScaleY)
			diagram.scaleX = newScale
			diagram.scaleY = newScale
						
			diagram.clip = new Rectangle => [
				fit(newScale, newScaleX, newScaleY, myBounds)
			]
//			fillRectangle.fit(newScale, newScaleX, newScaleY, myBounds)
//			if(fillRectangle.parent == null)
//				children += fillRectangle
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

	override activate() {
		diagram.nodes.addListener [ ListChangeListener.Change<? extends XNode> change |
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
		diagram.buttonLayer.layoutBoundsProperty.addListener(boundsInLocalListener)
	}

}