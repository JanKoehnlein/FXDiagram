package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.behavior.AbstractCloseBehavior
import de.fxdiagram.core.behavior.AbstractOpenBehavior
import de.fxdiagram.core.extensions.AccumulativeTransform2D
import de.fxdiagram.core.model.ModelElement
import de.fxdiagram.core.tools.actions.ScrollToAndScaleTransition
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import eu.hansolo.enzo.radialmenu.Symbol
import eu.hansolo.enzo.radialmenu.SymbolCanvas
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.geometry.BoundingBox
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.util.Duration

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@Logging
class OpenableDiagramNode extends XNode {
	
	@FxProperty XDiagram innerDiagram
	
	XDiagram parentDiagram 
	
	XRoot root
	
	RectangleBorderPane pane
	
	Text textNode
	
	DiagramScaler diagramScaler

	@Property Duration transitionDuration = 1000.millis
	@Property Duration transitionDelay = 100.millis
	
	@Property boolean isOpen
	
	Point2D nodeCenterInDiagram
	
	new() {
		node = pane = new RectangleBorderPane => [
			children += textNode = new Text => [
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
			tooltip = "Double-click to open"
		]
		cursor = Cursor.HAND
	}
	
	
	override createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	override doActivate() {
		super.doActivate()
		textNode.text = domainObject?.key
		this.root = getRoot
		if(innerDiagram == null) {
			LOG.severe('Nested diagram not set. Deactivating open behavior')
		} else {
			node.onMouseClicked = [
				if(clickCount == 2) {
					openDiagram
				}
			]
		}
		val AbstractOpenBehavior openBehavior = [| openDiagram ]
		addBehavior(openBehavior)
	}
	
	def void openDiagram() {
		if(isOpen) {
			LOG.severe('Attempt to close a closed editor')
			return
		}
		isOpen = true
		val nodeBounds = layoutBounds - new Insets(5,5,5,5)
		nodeCenterInDiagram = localToRootDiagram(nodeBounds.center)
		innerDiagram.opacity = 0
		pane.children.add(new Group => [
			children += innerDiagram
		])
		innerDiagram.activate
		val AbstractCloseBehavior closeBehavior = [| closeDiagram ]
		innerDiagram.addBehavior(closeBehavior)
		innerDiagram.layout
		diagramScaler = new DiagramScaler(innerDiagram) => [
			width = nodeBounds.width
			height = nodeBounds.height
			activate
		]
		val initialScale = innerDiagram.localToScene(new BoundingBox(0,0,1,0)).width
		val diagramBoundsInLocal = innerDiagram.boundsInLocal
		val targetScale = max(AccumulativeTransform2D.MIN_SCALE, 
			min(1, 
				min(scene.width / diagramBoundsInLocal.width, 
					scene.height / diagramBoundsInLocal.height)) / initialScale) * root.diagramTransform.scale
		new ParallelTransition => [
			children += new ScrollToAndScaleTransition(root, nodeCenterInDiagram, targetScale) => [
				duration = transitionDuration
				onFinished = [
					diagramScaler.deactivate
					parentDiagram = root.diagram
					pane.children.setAll(textNode)
					val toParentButton = SymbolCanvas.getSymbol(Symbol.Type.ZOOM_OUT, 32, Color.GRAY) => [
						onMouseClicked = [
							root.headsUpDisplay.children -= target as Node
							closeDiagram
						]
						tooltip = "Parent diagram"
						
					]
					innerDiagram.fixedButtons.put(toParentButton, Pos.TOP_RIGHT)
					root.diagram = innerDiagram				
				]
			]
			children += new FadeTransition => [
				duration = transitionDuration - transitionDelay
				fromValue = 1
				toValue = 0
				node = textNode
			]
			children += new FadeTransition => [
				delay = transitionDelay
				duration = transitionDuration - transitionDelay
				fromValue = 0
				toValue = 1
				node = innerDiagram
			]
			play
		]
	}
	
	def void closeDiagram() {
		if(!isOpen) {
			LOG.severe('Attempt to close a closed editor')
			return
		}
		isOpen = false
		root.diagram = parentDiagram
		pane.children += new Group => [
			children += innerDiagram
		]
		innerDiagram.activate
		innerDiagram.layout
		diagramScaler.activate
		new ParallelTransition => [
			children += new ScrollToAndScaleTransition(root, nodeCenterInDiagram, 1) => [
				duration = transitionDuration
				onFinished = [
					diagramScaler.deactivate
					parentDiagram = root.diagram
					pane.children.setAll(textNode)
				]
			]
			children += new FadeTransition => [
				delay = transitionDelay
				duration = transitionDuration - transitionDelay
				fromValue = 0
				toValue = 1
				node = textNode
			]
			children += new FadeTransition => [
				duration = transitionDuration - transitionDelay
				fromValue = 1
				toValue = 0
				node = innerDiagram
			]
			play
		]
	}
	
	override populate(ModelElement it) {
		super.populate(it)
//		addChildProperty(innerDiagramProperty, XDiagram)
	}
	
}

