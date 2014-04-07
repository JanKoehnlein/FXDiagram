package de.fxdiagram.core.command

import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransition

class ViewportCommand implements Command {
	
	ViewportMemento fromMemento
	ViewportMemento toMemento
	
	()=>ViewportTransition transitionFactory

	new(()=>ViewportTransition transitionFactory) {
		this.transitionFactory = transitionFactory
	}
	
	override execute(CommandContext context) {
		val transition = transitionFactory.apply()
		if(transition != null) {
			fromMemento =  transition.from
			toMemento =  transition.to
			context.animationQueue.enqueue[|
				transition
			]
		}
	}
	
	override canUndo() {
		fromMemento != null
	}
	
	override undo(CommandContext context) {
		context.animationQueue.enqueue[| 
			new ViewportTransition(context.root, fromMemento, context.defaultUndoDuration)
		]
	}
	
	override canRedo() {
		toMemento != null
	}
	
	override redo(CommandContext context) {
		context.animationQueue.enqueue[|
			new ViewportTransition(context.root, toMemento, context.defaultUndoDuration)
		]
	}
}