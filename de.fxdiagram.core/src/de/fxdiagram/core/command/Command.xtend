package de.fxdiagram.core.command

interface Command {
	def void execute(CommandContext context)
	def boolean canUndo()
	def void undo(CommandContext context)
	def boolean canRedo()
	def void redo(CommandContext context)
}

