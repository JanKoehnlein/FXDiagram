package de.fxdiagram.core.command

import java.util.List

import static de.fxdiagram.core.command.ChainedAnimationUtil.*

class SequentialAnimationCommand extends AbstractAnimationCommand {

	List<AnimationCommand> commands = newArrayList

	def operator_add(AnimationCommand command) {
		this.commands += command
	} 

	override createExecuteAnimation(CommandContext context) {
		createChainedAnimation(commands, [getExecuteAnimation(context)])
	}

	override createUndoAnimation(CommandContext context) {
		createChainedAnimation(commands.reverseView, [getUndoAnimation(context)])
	}
	
	override createRedoAnimation(CommandContext context) {
		createChainedAnimation(commands, [getRedoAnimation(context)])
	}
	
}
