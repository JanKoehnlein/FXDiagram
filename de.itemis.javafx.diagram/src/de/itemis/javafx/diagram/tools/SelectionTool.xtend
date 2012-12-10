package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.Diagram
import javafx.scene.input.MouseEvent

import static de.itemis.javafx.diagram.GraphUtil.*

class SelectionTool {
	
	Diagram diagram 
	
	new(Diagram diagram) {
		this.diagram = diagram
		diagram.rootPane.addEventFilter(MouseEvent::MOUSE_PRESSED) [
			val targetShape = getTargetShape(it)
			if(targetShape == null || (!targetShape.selected && !shortcutDown))
				selection.forEach[selected = false]				
			for(shape: selection) {
				shape.selectionBehavior.mousePressed(it)				
			}
		]
		diagram.rootPane.addEventFilter(MouseEvent::MOUSE_DRAGGED) [
			for(shape: selection) {
				shape.selectionBehavior.mouseDragged(it)				
			}
		]
	}
	
	def getSelection() {
		diagram.shapes.filter[selected]
	}
}