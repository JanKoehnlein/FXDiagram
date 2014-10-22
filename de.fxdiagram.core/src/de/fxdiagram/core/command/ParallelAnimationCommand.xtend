package de.fxdiagram.core.command

import java.util.List
import javafx.animation.Animation
import javafx.animation.ParallelTransition

class ParallelAnimationCommand extends AbstractAnimationCommand {

	List<AnimationCommand> commands = newArrayList

	def operator_add(AnimationCommand command) {
		if(command != null) {
			this.commands += command
			// this already restores the viewport
			command.skipViewportRestore
		} 
	} 

	override createExecuteAnimation(CommandContext context) {
		commands.map[getExecuteAnimation(context)].parallelTransition 
	}

	override createUndoAnimation(CommandContext context) {
		commands.map[getUndoAnimation(context)].parallelTransition
	}
	
	override createRedoAnimation(CommandContext context) {
		commands.map[getRedoAnimation(context)].parallelTransition
	}
	
	protected def getParallelTransition(List<Animation> animations) {
		val validAnimations = animations.filterNull
		if(validAnimations.empty)
			return null
		else
			return new ParallelTransition => [
				children += validAnimations 	
			]		
	}
}