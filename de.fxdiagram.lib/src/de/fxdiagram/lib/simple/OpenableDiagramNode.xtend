package de.fxdiagram.lib.simple

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.tools.actions.ScrollToAndScaleTransition
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import eu.hansolo.enzo.radialmenu.Symbol
import eu.hansolo.enzo.radialmenu.SymbolCanvas
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
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

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.geometry.BoundsExtensions.*
import static extension javafx.util.Duration.*

class OpenableDiagramNode extends XNode {
	
	XDiagram nestedDiagram
	
	XDiagram parentDiagram 
	
	XRoot root
	
	RectangleBorderPane pane
	
	Text textNode

	@Property Duration transitionDuration = 800.millis
	
	new(String name, XDiagram nestedDiagram) {
		this.nestedDiagram = nestedDiagram
		node = pane = new RectangleBorderPane => [
			children += textNode = new Text => [
				text = name
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
		]
		key = name
		cursor = Cursor.HAND
	}
	
	override createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}

	override doActivate() {
		super.doActivate()
		this.root = getRoot
		node.onMouseClicked = [
			if(clickCount == 2) {
				openDiagram
			}
		]
	}
	
	protected def openDiagram() {
		val nodeBounds = layoutBounds
		val targetInDiagram = localToRootDiagram(nodeBounds.center)
		nestedDiagram.transforms.clear
		pane.children.add(new Group => [
			children += nestedDiagram
		])
		nestedDiagram.activate
		nestedDiagram.layout
		val autoScaleToFit = new AutoScaleToFit(nestedDiagram) => [
			width = nodeBounds.width
			height = nodeBounds.height
			activate
		]
		val initialScale = nestedDiagram.localToScene(new BoundingBox(0,0,1,0)).width
		val diagramBoundsInLocal = nestedDiagram.boundsInLocal
		val scale = max(XRoot.MIN_SCALE, 
			min(1, 
				min(scene.width / diagramBoundsInLocal.width, 
					scene.height / diagramBoundsInLocal.height)) / initialScale) * root.diagramScale
		new ParallelTransition => [
			children += new ScrollToAndScaleTransition(root, targetInDiagram, scale) => [
				duration = transitionDuration
				onFinished = [
					autoScaleToFit.deactivate
					parentDiagram = root.diagram
					nestedDiagram.transforms.clear
					pane.children.setAll(textNode)
					root.diagram = nestedDiagram				
					root.headsUpDisplay.add(
						SymbolCanvas.getSymbol(Symbol.Type.ZOOM_OUT, 32, Color.GRAY) => [
							onMouseClicked = [
								root.headsUpDisplay.children -= target as Node
								closeDiagram(nodeBounds, targetInDiagram)
							]
						], Pos.TOP_RIGHT)
				]
			]
			children += new FadeTransition => [
				duration = transitionDuration
				fromValue = 1
				toValue = 0
				node = textNode
			]
			children += new FadeTransition => [
				duration = transitionDuration
				fromValue = 0
				toValue = 1
				node = nestedDiagram
			]
			play
		]
	}
	
	protected def closeDiagram(Bounds nodeBounds, Point2D targetInDiagram) {
		root.diagram = parentDiagram
		pane.children.add(new Group => [
			children += nestedDiagram
		])
		nestedDiagram.activate
		nestedDiagram.layout
		val autoScaleToFit = new AutoScaleToFit(nestedDiagram) => [
			width = nodeBounds.width
			height = nodeBounds.height
			activate
		]
		new ParallelTransition => [
			children += new ScrollToAndScaleTransition(root, targetInDiagram, 1) => [
				duration = transitionDuration
				onFinished = [
					autoScaleToFit.deactivate
					parentDiagram = root.diagram
					nestedDiagram.transforms.clear
					pane.children.setAll(textNode)
				]
			]
			children += new FadeTransition => [
				duration = transitionDuration
				fromValue = 0
				toValue = 1
				node = textNode
			]
			children += new FadeTransition => [
				duration = transitionDuration
				fromValue = 1
				toValue = 0
				node = nestedDiagram
			]
			play
		]
		textNode.opacity = 1
	}
}