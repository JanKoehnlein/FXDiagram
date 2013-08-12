package de.fxdiagram.core.tools

import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRootDiagram
import de.fxdiagram.core.XShape
import java.util.Collection
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

import static extension de.fxdiagram.core.Extensions.*

class SelectionTool implements XDiagramTool {

	XRootDiagram rootDiagram

	EventHandler<MouseEvent> mousePressedHandler
	EventHandler<MouseEvent> mouseDraggedHandler
	EventHandler<MouseEvent> mouseReleasedHandler

	new(XRootDiagram rootDiagram) {
		this.rootDiagram = rootDiagram
		this.mousePressedHandler = [ event |
			val selection = rootDiagram.currentSelection.toSet
			if(event.target instanceof Scene && event.button == MouseButton.PRIMARY) {
				selection.deselect[true]
			} else if (!(event.targetButton instanceof XRapidButton)) {
				val targetShape = event.targetShape
				if (targetShape?.isSelectable) {
					if (!targetShape.selected && !event.shortcutDown) {
						val skip = switch targetShape {
							XControlPoint: targetShape.parent.containerShape
							default: null
						}
						selection.deselect[it != skip]
					} 
					selection.deselect[it.diagram != targetShape.diagram]
					if(event.shortcutDown)
						targetShape.toggleSelect(event)
					else 
						targetShape.select(event)
						
					selection.forEach [
						moveBehavior?.mousePressed(event)
					]
					targetShape.moveBehavior?.mousePressed(event)
				}
			}
		]
		this.mouseDraggedHandler = [
			val selection = rootDiagram.currentSelection
			for (shape : selection) 
				shape?.moveBehavior?.mouseDragged(it)
			rootDiagram.auxiliaryLinesSupport?.show(selection)				
			consume
		]
		this.mouseReleasedHandler = [
			rootDiagram.auxiliaryLinesSupport?.hide()				
		]
	}
	
	protected def deselect(Collection<XShape> selection, (XShape)=>boolean filter) {
		val i = selection.iterator()
		while(i.hasNext) {
			val element = i.next
			if(filter.apply(element)) {
				element.selected = false				
				i.remove
			}
		}
	}

	override activate() {
		rootDiagram.scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler)
		rootDiagram.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler)
		rootDiagram.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler)
		true
	}

	override deactivate() {
		rootDiagram.scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler)
		rootDiagram.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler)
		rootDiagram.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler)
		true
	}
}
