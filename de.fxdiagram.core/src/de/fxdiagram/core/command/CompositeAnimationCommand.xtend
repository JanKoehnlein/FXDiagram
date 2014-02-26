package de.fxdiagram.core.command

import java.util.List
import javafx.animation.ParallelTransition

class CompositeAnimationCommand extends AbstractAnimationCommand {

	List<AbstractAnimationCommand> commands = newArrayList

	def operator_add(AbstractAnimationCommand command) {
		this.commands += command
	} 

	override createExecuteAnimation(CommandContext context) {
		new ParallelTransition => [
			children += commands.map[createExecuteAnimation(context)].filterNull
		]
	}

	override createUndoAnimation(CommandContext context) {
		new ParallelTransition => [
			children += commands.map[createUndoAnimation(context)].filterNull
		]
	}
	
	override createRedoAnimation(CommandContext context) {
		new ParallelTransition => [
			children += commands.map[createRedoAnimation(context)].filterNull
		]
	}
	
	override canUndo() {
		commands.forall[canUndo]
	}
	
	override canRedo() {
		commands.forall[canRedo]
	}
	
}