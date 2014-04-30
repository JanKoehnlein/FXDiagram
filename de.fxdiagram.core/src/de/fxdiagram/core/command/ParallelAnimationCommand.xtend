package de.fxdiagram.core.command

import java.util.List
import javafx.animation.ParallelTransition

class ParallelAnimationCommand extends AbstractAnimationCommand {

	List<AnimationCommand> commands = newArrayList

	def operator_add(AnimationCommand command) {
		this.commands += command
	} 

	override createExecuteAnimation(CommandContext context) {
		new ParallelTransition => [
			children += commands.map[getExecuteAnimation(context)].filterNull
		]
	}

	override createUndoAnimation(CommandContext context) {
		new ParallelTransition => [
			children += commands.map[getUndoAnimation(context)].filterNull
		]
	}
	
	override createRedoAnimation(CommandContext context) {
		new ParallelTransition => [
			children += commands.map[getRedoAnimation(context)].filterNull
		]
	}
}