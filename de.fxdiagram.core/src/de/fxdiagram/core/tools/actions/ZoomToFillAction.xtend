package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRootDiagram

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.geometry.BoundsExtensions.*

class ZoomToFillAction implements DiagramAction {
	
	override perform(XRootDiagram diagram) {
		val selectionBounds = diagram.selection.map[localToRootDiagram(snapBounds)].reduce[a,b|a+b]
		if(selectionBounds != null) {
			val targetScale =  
					max(diagram.scene.width / selectionBounds.width, 
						diagram.scene.height / selectionBounds.height)
			new ScrollToAndScaleTransition(diagram, selectionBounds.center, targetScale).play
		}
	}
	
	protected def getSelection(XRootDiagram diagram) {
		diagram.allShapes.filter[isSelectable && selected]
	}
}