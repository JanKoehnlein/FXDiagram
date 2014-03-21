package de.fxdiagram.core.command

import java.util.LinkedList
import de.fxdiagram.core.XRoot

class CommandStack {
	
	LinkedList<Command> undoStack = newLinkedList
	LinkedList<Command> redoStack = newLinkedList

	CommandContext context 
	
	new(XRoot root) {
		context = new CommandContext(root)
	}
	
	def boolean canUndo() {
		return !undoStack.empty && undoStack.last?.canUndo
	}
	
	def boolean canRedo() {
		return !redoStack.empty && redoStack.last?.canRedo
	}
	
	def void undo() {
		if(canUndo) {
			val command = undoStack.pop
			command.undo(context)
			redoStack.push(command)
		}
	}
	
	def void redo() {
		if(canRedo) {
			val command = redoStack.pop
			command.redo(context)
			undoStack.push(command)
		}
	}
	
	def execute(Command command) {
		command.execute(context)
		undoStack.push(command)
	}
	
	def getContext() {
		context
	}
}