package de.itemis.javafx.diagram.tools

import javafx.scene.input.MouseEvent
import de.itemis.javafx.diagram.XRootDiagram
import de.itemis.javafx.diagram.XRapidButton
import static extension de.itemis.javafx.diagram.Extensions.*

class SelectionTool {
	
	XRootDiagram rootDiagram 
	
	new(XRootDiagram rootDiagram) {
		this.rootDiagram = rootDiagram
		rootDiagram.addEventFilter(MouseEvent::MOUSE_PRESSED) [ 
			event |
			if(!(event.target instanceof XRapidButton)) { 
				val targetShape = event.targetShape
				if(targetShape?.selectionBehavior != null) {
					if(!targetShape.selectionBehavior.selected && !event.shortcutDown) {
						selection.forEach[selectionBehavior.selected = false]	
					}
					selection.filter[it.diagram != targetShape.diagram].forEach[
						selectionBehavior.selected = false
					]
					selection.forEach[
						moveBehavior?.mousePressed(event)				
					]
					targetShape.moveBehavior?.mousePressed(event)				
					targetShape.selectionBehavior.mousePressed(event)
				}
				event.consume
			}
		]
		rootDiagram.addEventFilter(MouseEvent::MOUSE_DRAGGED) [
			for(shape: selection) {
				shape?.moveBehavior?.mouseDragged(it)				
			}
			consume
		]
	}
	
	def getSelection() {
		rootDiagram.nodes.filter[selectionBehavior?.selected]
	}
}