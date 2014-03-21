package de.fxdiagram.core.command

import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransition
import javafx.animation.Animation
import javafx.animation.SequentialTransition

abstract class AbstractAnimationCommand implements Command {

	ViewportMemento fromMemento
	ViewportMemento toMemento
	
	override execute(CommandContext context) {
		context.animationQueue.enqueue[|
			fromMemento = context.root.diagramTransform.createMemento
			val animation = createExecuteAnimation(context)
			return if(animation != null) {
				new SequentialTransition => [
					children += animation
					onFinished = [
						toMemento = context.root.diagramTransform.createMemento
					]
				]
			} else {
				toMemento = fromMemento
				null
			}
		]
	}

	override canUndo() {
		true
	}
	
	override undo(CommandContext context) {
		context.animationQueue.enqueue[| 
			new SequentialTransition => [
				children += new ViewportTransition(context.root, toMemento, context.defaultUndoDuration)
				children += createUndoAnimation(context)
			]
		]
	}
	
	override canRedo() {
		true
	}
	
	override redo(CommandContext context) {
		context.animationQueue.enqueue[|
			new SequentialTransition => [
				children += new ViewportTransition(context.root, fromMemento, context.defaultUndoDuration)
				children += createRedoAnimation(context)
			]
		]
	}
	
	def Animation createExecuteAnimation(CommandContext context)
	def Animation createUndoAnimation(CommandContext context)
	def Animation createRedoAnimation(CommandContext context)
} 