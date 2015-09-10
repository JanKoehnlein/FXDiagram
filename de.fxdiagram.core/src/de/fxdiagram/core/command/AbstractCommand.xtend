package de.fxdiagram.core.command

/**
 * A command without animations
 */
abstract class AbstractCommand implements AnimationCommand {
	
	override getExecuteAnimation(CommandContext context) {
		execute(context)
		return null
	}
	
	def void execute(CommandContext context)
	
	override getUndoAnimation(CommandContext context) {
		undo(context)
		null
	}
	
	def void undo(CommandContext context)
	
	override getRedoAnimation(CommandContext context) {
		redo(context)
		null
	}
	
	def void redo(CommandContext context)
	
	override clearRedoStackOnExecute() {
		false
	}
	
	override skipViewportRestore() {
	}
	
}