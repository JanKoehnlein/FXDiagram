package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.BoundsExtensions.*

class CenterAction implements DiagramAction {
	
	override perform(XRoot root) {
		val selectionBounds = root.currentSelection.map[localToRootDiagram(snapBounds)].reduce[a,b|a+b]
		if(selectionBounds != null && selectionBounds.width > EPSILON && selectionBounds.height > EPSILON) {
			val targetScale = min(1, 
					min(root.scene.width / selectionBounds.width, 
						root.scene.height / selectionBounds.height))
			new ScrollToAndScaleTransition(root, selectionBounds.center, targetScale).play
		}
	}
}