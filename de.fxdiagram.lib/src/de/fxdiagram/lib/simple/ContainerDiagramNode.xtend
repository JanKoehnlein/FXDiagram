package de.fxdiagram.lib.simple

import com.google.common.annotations.Beta
import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane2
import javafx.geometry.Insets
import javafx.scene.Group

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * An {@link XNode} containing another diagram.
 *  
 * The node's border automatically scales around its contents.
 */
@Beta
@Logging
@ModelNode('innerDiagram')
class ContainerDiagramNode extends XNode implements XDiagramContainer {

	RectangleBorderPane2 pane = new RectangleBorderPane2

	@FxProperty XDiagram innerDiagram = new XDiagram

	val padding = new Insets(10,10,10,10)
	
	Group group
	
	new(String name) {
		super(name)
	}

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}

	protected override createNode() {
		pane => [
			it.padding = this.padding
			children += group = new Group => [
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
		// move container node when the inner diagram grows to the upper/left
		innerDiagram.boundsInLocalProperty.addListener [ p, o, n |
			if(!layoutXProperty.bound && !layoutYProperty.bound) {
				val dx = o.minX - n.minX
				val dy = o.minY - n.minY
				layoutX = layoutX - dx
				layoutY = layoutY - dy
				group.layoutX = group.layoutX + dx  
				group.layoutY = group.layoutY + dy  
			}
		]		
	}
	
	override protected layoutChildren() {
		super.layoutChildren()
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
