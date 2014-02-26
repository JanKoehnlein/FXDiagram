package de.fxdiagram.core.command

import javafx.animation.Animation

abstract class AbstractAnimationCommand implements Command {

	override execute(CommandContext context) {
		context.animationQueue.enqueue[|createExecuteAnimation(context)]
	}

	override canUndo() {
		true
	}
	
	override undo(CommandContext context) {
		context.animationQueue.enqueue[|createUndoAnimation(context)]
	}
	
	override canRedo() {
		true
	}
	
	override redo(CommandContext context) {
		context.animationQueue.enqueue[|createRedoAnimation(context)]
	}
	
	def Animation createExecuteAnimation(CommandContext context)
	def Animation createUndoAnimation(CommandContext context)
	def Animation createRedoAnimation(CommandContext context)
} 