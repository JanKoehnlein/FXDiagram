package de.fxdiagram.core.command

/**
 * Postpones the delegate command creation until it is executed.
 */
abstract class LazyCommand implements AnimationCommand {
	
	AnimationCommand delegate
	
	protected abstract def AnimationCommand createDelegate()

	override clearRedoStackOnExecute() {
		delegate?.clearRedoStackOnExecute
	}
	
	override skipViewportRestore() {
		delegate?.skipViewportRestore
	}
	
	override getExecuteAnimation(CommandContext context) {
		delegate = createDelegate
		delegate.getExecuteAnimation(context)
	}
	
	override getUndoAnimation(CommandContext context) {
		delegate.getUndoAnimation(context)
	}
	
	override getRedoAnimation(CommandContext context) {
		delegate.getRedoAnimation(context)
	}
	
}