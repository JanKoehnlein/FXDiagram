package de.fxdiagram.core.command

import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransition
import javafx.animation.Animation
import javafx.animation.SequentialTransition
import javafx.util.Duration

abstract class AbstractAnimationCommand implements AnimationCommand {

	ViewportMemento fromMemento
	ViewportMemento toMemento
	Duration executeDuration
	
	boolean isRestoreViewport = true
	
	override skipViewportRestore() {
		isRestoreViewport = false
	}

	override getExecuteAnimation(CommandContext context) {
		if(isRestoreViewport)
			fromMemento = context.root.viewportTransform.createMemento
		val animation = createExecuteAnimation(context)
		if(animation != null) {
			return new SequentialTransition => [
				children += animation
				onFinished = [
					if(isRestoreViewport)
						toMemento = context.root.viewportTransform.createMemento
				]
			]
		} else {
			if(isRestoreViewport)
				toMemento = fromMemento
			return null
		}	
	}
	
	def Animation createExecuteAnimation(CommandContext context)
	
	override getUndoAnimation(CommandContext context) {
		val undoAnimation = createUndoAnimation(context)
		if(toMemento != null || undoAnimation != null) {
			new SequentialTransition => [
				if(toMemento != null)
					children += new ViewportTransition(context.root, toMemento, context.defaultUndoDuration)
				children += undoAnimation
			]
		} else {
			null
		}
	}
	
	def Animation createUndoAnimation(CommandContext context)
	
	override getRedoAnimation(CommandContext context) {
		val redoAnimation = createRedoAnimation(context)
		if(fromMemento != null || redoAnimation != null) {
			new SequentialTransition => [
				if(fromMemento != null)
					children += new ViewportTransition(context.root, fromMemento, context.defaultUndoDuration)
				children += redoAnimation
			]
		} else {
			null
		}
	}

	def Animation createRedoAnimation(CommandContext context)
	
	override clearRedoStackOnExecute() {
		true
	}
	
	def setExecuteDuration(Duration executeDuration) {
		this.executeDuration = executeDuration
	}
	
	def getExecuteDuration(CommandContext context) {
		executeDuration ?: context.defaultExecuteDuration
	}
} 