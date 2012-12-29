package de.itemis.javafx.diagram.tools

import javafx.scene.input.MouseEvent
import de.itemis.javafx.diagram.XRootDiagram
import de.itemis.javafx.diagram.XRapidButton
import static extension de.itemis.javafx.diagram.Extensions.*

class SelectionTool {
	
	XRootDiagram diagram 
	
	new(XRootDiagram diagram) {
		this.diagram = diagram
		diagram.addEventFilter(MouseEvent::MOUSE_PRESSED) [
			if(!(target instanceof XRapidButton)) { 
				val targetShape = it.targetShape
				if(targetShape?.selectionBehavior != null) {
					if(!targetShape.selectionBehavior.selected && !shortcutDown)
						selection.forEach[selectionBehavior.selected = false]		
					for(shape: selection) {
						shape?.moveBehavior?.mousePressed(it)				
					}
					targetShape.moveBehavior?.mousePressed(it)				
					targetShape.selectionBehavior.mousePressed(it)
					it.consume
				}
			}
		]
		diagram.addEventFilter(MouseEvent::MOUSE_DRAGGED) [
			for(shape: selection) {
				shape?.moveBehavior?.mouseDragged(it)				
			}
			it.consume
		]
	}
	
	def getSelection() {
		diagram.nodes.filter[selectionBehavior?.selected]
	}
}