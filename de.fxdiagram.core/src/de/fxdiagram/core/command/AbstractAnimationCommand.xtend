package de.fxdiagram.core.command

import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransition
import javafx.animation.Animation
import javafx.animation.SequentialTransition

abstract class AbstractAnimationCommand implements AnimationCommand {

	ViewportMemento fromMemento
	ViewportMemento toMemento

	override getExecuteAnimation(CommandContext context) {
		fromMemento = context.root.viewportTransform.createMemento
		val animation = createExecuteAnimation(context)
		if(animation != null) {
			return new SequentialTransition => [
				children += animation
				onFinished = [
					toMemento = context.root.viewportTransform.createMemento
				]
			]
		} else {
			toMemento = fromMemento
			return null
		}	
	}
	
	def Animation createExecuteAnimation(CommandContext context)
	
	override getUndoAnimation(CommandContext context) {
		new SequentialTransition => [
			children += new ViewportTransition(context.root, toMemento, context.defaultUndoDuration)
			children += createUndoAnimation(context)
		]
	}
	
	def Animation createUndoAnimation(CommandContext context)
	
	override getRedoAnimation(CommandContext context) {
		new SequentialTransition => [
			children += new ViewportTransition(context.root, fromMemento, context.defaultUndoDuration)
			children += createRedoAnimation(context)
		]
	}

	def Animation createRedoAnimation(CommandContext context)
	
	override clearRedoStackOnExecute() {
		true
	}
} 