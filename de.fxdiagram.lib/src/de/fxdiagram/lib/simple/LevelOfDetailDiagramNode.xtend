package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@Logging
@ModelNode
class LevelOfDetailDiagramNode extends XNode {

	RectangleBorderPane pane = new RectangleBorderPane

	Text label

	Group innerDiagramGroup
	
	XDiagram innerDiagram

	DiagramScaler diagramScaler

	new(String name) {
		super(name)	
	}

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}
	
	protected override createNode() {
		pane => [
			children += label = new Text => [
				textOrigin = VPos.TOP
				text = name
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
		]
	}
	
	def setInnerDiagram(XDiagram innerDiagram) {
		this.innerDiagram = innerDiagram
		pane.children += innerDiagramGroup = new Group => [
			children.setAll(innerDiagram) 
		]
		diagramScaler = new DiagramScaler(innerDiagram)
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	override doActivate() {
		super.doActivate()
		label => [
			text = name
			tooltip = "Zoom to reveal content"
		]	
		if(innerDiagram == null) {
			LOG.severe('No inner diagram set on node ' + name + '. LOD behavior deactivated')
		} else {
			diagram.viewportTransform.scaleProperty.addListener [ prop, oldVal, newVal |
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
		}
	}
}
