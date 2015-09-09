package de.fxdiagram.core.command

import javafx.animation.Animation
import javafx.animation.Timeline

/**
 * Base interface for commands. 
 * 
 * FXDiagram extends the command pattern by the idea that everything should be animated 
 * in order to give a better user experience. So instead of changing some diagram model,
 * we apply {@link Animations} to the scenegraph directly. 
 * 
 * Consider overrding {@link AbstractAnimationCommand} instead.
 */
interface AnimationCommand {
	def Animation getExecuteAnimation(CommandContext context)
	def Animation getUndoAnimation(CommandContext context)
	def Animation getRedoAnimation(CommandContext context)
	def boolean clearRedoStackOnExecute()
	
	/**
	 * Consider package private. Clients should not override this.
	 */
	def void skipViewportRestore()
	
	AnimationCommand NOOP = new AnimationCommand() {
		
		override getExecuteAnimation(CommandContext context) {
			null
		}
		
		override getUndoAnimation(CommandContext context) {
			null
		}
		
		override getRedoAnimation(CommandContext context) {
			null
		}
		
		override clearRedoStackOnExecute() {
			false
		}
		
		override skipViewportRestore() {
		}
	}
}
