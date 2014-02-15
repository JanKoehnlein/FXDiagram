package de.fxdiagram.core.command

import javafx.animation.Animation
import javafx.util.Duration

interface Command {
	def Animation execute(Duration duration)
	def boolean canUndo()
	def Animation undo(Duration duration)
	def boolean canRedo()
	def Animation redo(Duration duration)
}

