package de.fxdiagram.core.command

import javafx.animation.Animation

interface AnimationCommand {
	def Animation getExecuteAnimation(CommandContext context)
	def Animation getUndoAnimation(CommandContext context)
	def Animation getRedoAnimation(CommandContext context)
	def boolean clearRedoStackOnExecute()
}
