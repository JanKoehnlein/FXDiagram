package de.fxdiagram.core.tools

import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRootDiagram
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import static extension de.fxdiagram.core.Extensions.*
import de.fxdiagram.core.XControlPoint

class SelectionTool implements XDiagramTool {

	XRootDiagram rootDiagram

	EventHandler<MouseEvent> mousePressedHandler
	EventHandler<MouseEvent> mouseDraggedHandler

	new(XRootDiagram rootDiagram) {
		this.rootDiagram = rootDiagram
		this.mousePressedHandler = [ event |
			if (!(event.targetButton instanceof XRapidButton)) {
				val targetShape = event.targetShape
				if (targetShape?.isSelectable) {
					if (!targetShape.selected && !event.shortcutDown) {
						val skip = switch targetShape {
							XControlPoint: targetShape.parent.containerShape
							default: null
						}
						selection.filter[it != skip].forEach[selected = false]						
					} 
					selection.filter[it.diagram != targetShape.diagram].forEach [
						selected = false
					]
					if(event.shortcutDown)
						targetShape.toggleSelect(event)
					else 
						targetShape.select(event)
						
					selection.forEach [
						moveBehavior?.mousePressed(event)
					]
					targetShape.moveBehavior?.mousePressed(event)
					//event.consume
				}
			}
		]
		this.mouseDraggedHandler = [
			for (shape : selection) {
				shape?.moveBehavior?.mouseDragged(it)
			}
			consume
		]
	}

	def getSelection() {
		(rootDiagram.nodes + rootDiagram.connections + rootDiagram.connections.map[controlPoints].flatten)
			.filter[isSelectable && selected]
	}

	override activate() {
		rootDiagram.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler)
		rootDiagram.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler)
		true
	}

	override deactivate() {
		rootDiagram.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler)
		rootDiagram.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler)
		true
	}
}
