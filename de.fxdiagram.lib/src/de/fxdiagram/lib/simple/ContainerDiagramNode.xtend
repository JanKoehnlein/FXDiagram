package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.scene.Group

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.XDiagramContainer
import javafx.scene.layout.VBox

/**
 * An {@link XNode} containing another diagram.
 *  
 * The node's border automatically scales around its contents.
 */
@Logging
@ModelNode('innerDiagram')
class ContainerDiagramNode extends XNode implements XDiagramContainer {

	RectangleBorderPane pane = new RectangleBorderPane

	@FxProperty XDiagram innerDiagram = new XDiagram

	val padding = new Insets(20,20,20,20)
	
	new(String name) {
		super(name)
	}

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}

	protected override createNode() {
		pane => [
			it.padding = this.padding
		]
	}
	
	override initializeGraphics() {
		super.initializeGraphics()
		pane => [
			children += new Group => [
				children += innerDiagram => [
					isRootDiagram = false
					parentDiagram = this.diagram 			
				]
			]
		]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}

	override doActivate() {
		super.doActivate()
		innerDiagram.activate
	}
	
	override getInsets() {
		padding
	}
	
	override toFront() {
		super.toFront()
		(innerDiagram.nodes + innerDiagram.connections).forEach[toFront]
	}
	
	override isInnerDiagramActive() {
		isActive
	}
}
