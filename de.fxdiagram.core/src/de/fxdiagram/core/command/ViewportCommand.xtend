package de.fxdiagram.core.command

import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransition

abstract class ViewportCommand implements AnimationCommand {
	
	ViewportMemento fromMemento
	ViewportMemento toMemento
	
	override clearRedoStackOnExecute() {
		false
	}
	
	override skipViewportRestore() {
	}
	
	abstract def ViewportTransition createViewportTransiton(CommandContext context)
		
	override getExecuteAnimation(CommandContext context) {
		val transition = createViewportTransiton(context)
		if(transition != null) {
			fromMemento =  transition.from
			toMemento =  transition.to
			transition
		} else {
			null
		}
	}
	
	override getUndoAnimation(CommandContext context) {
		new ViewportTransition(context.root, fromMemento, context.defaultUndoDuration)
	}
	
	override getRedoAnimation(CommandContext context) {
		new ViewportTransition(context.root, toMemento, context.defaultUndoDuration)
	}
}