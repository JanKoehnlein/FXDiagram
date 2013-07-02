package de.fxdiagram.core.tools

import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRootDiagram
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import static extension de.fxdiagram.core.Extensions.*

class SelectionTool implements XDiagramTool {

	XRootDiagram rootDiagram

	EventHandler<MouseEvent> mousePressedHandler
	EventHandler<MouseEvent> mouseDraggedHandler

	new(XRootDiagram rootDiagram) {
		this.rootDiagram = rootDiagram
		this.mousePressedHandler = [ event |
			if (!(event.targetButton instanceof XRapidButton)) {
				val targetShape = event.targetNode
				if (targetShape?.selectionBehavior != null) {
					if (!targetShape.selectionBehavior.selected && !event.shortcutDown) {
						selection.forEach[selectionBehavior.selected = false]
					}
					selection.filter[it.diagram != targetShape.diagram].forEach [
						selectionBehavior.selected = false
					]
					selection.forEach [
						moveBehavior?.mousePressed(event)
					]
					targetShape.moveBehavior?.mousePressed(event)
					targetShape.selectionBehavior.mousePressed(event)
					event.consume
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
		rootDiagram.nodes.filter[selectionBehavior?.selected]
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
