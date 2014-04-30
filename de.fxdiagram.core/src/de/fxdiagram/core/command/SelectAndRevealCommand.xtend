package de.fxdiagram.core.command

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.viewport.ViewportTransition
import java.util.Set

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class SelectAndRevealCommand extends ViewportCommand {

	Set<XShape> originalSelection = newHashSet()
	
	(XShape)=>boolean selectionPredicate
	
	new(XRoot root, (XShape)=>boolean selectionPredicate) {
		this.selectionPredicate = selectionPredicate
	}
	
	override createViewportTransiton(CommandContext context) {
		val root = context.root
		originalSelection += root.currentSelection
		var selection = (root.diagram.nodes + root.diagram.connections).map[ 
			if(selectionPredicate.apply(it)) {
				selected = true
				return it 
			} else {
				return null
			}
		].filterNull
		selection = if(selection.empty) 
		  	root.diagram.nodes + root.diagram.connections
		else
		  	selection
		val selectionBounds = selection.map[localToRootDiagram(snapBounds)].reduce[a,b|a+b]
		if(selectionBounds != null && selectionBounds.width > EPSILON && selectionBounds.height > EPSILON) {
			val targetScale = min(1, 
					min(root.scene.width / selectionBounds.width, 
						root.scene.height / selectionBounds.height))
			return new ViewportTransition(root, selectionBounds.center, targetScale)
		} else {
			return null
		}
	}
	
	override getUndoAnimation(CommandContext context) {
		(context.root.diagram.nodes + context.root.diagram.connections).forEach[
			selected = originalSelection.contains(it) 	
		] 
		super.getUndoAnimation(context)
	}
	
	override getRedoAnimation(CommandContext context) {
		(context.root.diagram.nodes + context.root.diagram.connections).forEach[
			selected = selectionPredicate.apply(it) 	
		] 
		super.getRedoAnimation(context)
	}
}