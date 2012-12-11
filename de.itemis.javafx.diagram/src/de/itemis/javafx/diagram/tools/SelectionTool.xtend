package de.itemis.javafx.diagram.tools

import de.itemis.javafx.diagram.XDiagram
import javafx.scene.input.MouseEvent

import static de.itemis.javafx.diagram.Extensions.*

class SelectionTool {
	
	XDiagram diagram 
	
	new(XDiagram diagram) {
		this.diagram = diagram
		diagram.rootPane.addEventFilter(MouseEvent::MOUSE_PRESSED) [
			val targetShape = getTargetShape(it)
			if(targetShape?.selectionBehavior != null) {
				if(!targetShape.selectionBehavior.selected && !shortcutDown)
					selection.forEach[selectionBehavior.setSelected(false)]		
				for(shape: selection) {
					shape.moveBehavior?.mousePressed(it)				
				}
				targetShape.selectionBehavior.mousePressed(it)
			}
		]
		diagram.rootPane.addEventFilter(MouseEvent::MOUSE_DRAGGED) [
			for(shape: selection) {
				shape?.moveBehavior?.mouseDragged(it)				
			}
		]
	}
	
	def getSelection() {
		diagram.shapes.filter[selectionBehavior?.selected]
	}
}