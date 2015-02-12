package de.fxdiagram.lib.chooser

import javafx.geometry.Point2D
import de.fxdiagram.core.XDiagram
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * An {@link AbstractBaseChooser} to add unrelated nodes without connections to the diagram.
 * 
 * @see AbstractBaseChooser
 */
class NodeChooser extends AbstractBaseChooser {
	
	XDiagram diagram
	
	Point2D position
	
	new(XDiagram diagram, Point2D position, ChoiceGraphics graphics, boolean isVertical) {
		super(graphics, isVertical)
		this.diagram = diagram
		this.position = position
	}
	
	override getRoot() {
		diagram.root
	}
	
	override getDiagram() {
		diagram
	}
	
	override getPosition() {
		position
	}
}