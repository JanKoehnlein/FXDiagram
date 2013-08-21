package de.fxdiagram.lib.simple

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

import static extension de.fxdiagram.core.Extensions.*

class LevelOfDetailDiagramNode extends XNode {

	String name

	RectangleBorderPane pane

	Node label

	Group innerDiagramGroup
	
	XDiagram innerDiagram

	DiagramScaler diagramScaler

	new(String name, XDiagram innerDiagram) {
		this.name = name
		this.innerDiagram = innerDiagram
		pane = new RectangleBorderPane
		node = pane => [
			children += label = new Text => [
				text = name
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
			children += innerDiagramGroup = new Group => [
				children += innerDiagram 
				diagramScaler = new DiagramScaler(innerDiagram)
			]
		]
		key = name
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	override doActivate() {
		super.doActivate()
		root.diagramScaleProperty.addListener [ prop, oldVal, newVal |
			val bounds = localToScene(boundsInLocal)
			val area = bounds.width * bounds.height
			if (area <= 100000) {
				label.visible = true
				innerDiagramGroup.visible = false
				pane.backgroundPaint = RectangleBorderPane.DEFAULT_BACKGROUND
			} else {
				label.visible = false
				innerDiagramGroup.visible = true
				innerDiagram.activate
				diagramScaler => [
					width = label.layoutBounds.width + 40
					height = label.layoutBounds.height + 20
					activate
				]
				pane.backgroundPaint = innerDiagram.backgroundPaint
			}
		]
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate
	}
}
