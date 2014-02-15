package de.fxdiagram.core.command

import java.util.LinkedList
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.util.Duration

class CommandStack {
	
	LinkedList<Command> undoStack = newLinkedList
	LinkedList<Command> redoStack = newLinkedList

	Duration duration = 100.millis
	
	def boolean canUndo() {
		return !undoStack.empty && undoStack.last?.canUndo
	}
	
	def boolean canRedo() {
		return !redoStack.empty && redoStack.last?.canRedo
	}
	
	def void undo() {
		if(canUndo) {
			val command = undoStack.pop
			command.undo(duration)?.playFromStart 				
			redoStack.push(command)
		}
	}
	def void redo() {
		if(canRedo) {
			val command = redoStack.pop
			command.redo(duration)?.playFromStart
			undoStack.push(command)
		}
	}
	
	def execute(Command command) {
		command.execute(duration)?.playFromStart
		undoStack.push(command)
	}
}