package de.fxdiagram.core.command

import de.fxdiagram.core.XRoot
import java.util.LinkedList

class CommandStack {
	
	LinkedList<AnimationCommand> undoStack = newLinkedList
	LinkedList<AnimationCommand> redoStack = newLinkedList

	CommandContext context 
	
	new(XRoot root) {
		context = new CommandContext(root)
	}
	
	def boolean canUndo() {
		return !undoStack.empty 
	}
	
	def boolean canRedo() {
		return !redoStack.empty 
	}
	
	def void undo() {
		if(canUndo) {
			val command = undoStack.pop
			context.animationQueue.enqueue[|
				command.getUndoAnimation(context)
			]
			redoStack.push(command)
		}
	}
	
	def void redo() {
		if(canRedo) {
			val command = redoStack.pop
			context.animationQueue.enqueue[|
				command.getRedoAnimation(context)
			]
			undoStack.push(command)
		}
	}
	
	def execute(AnimationCommand command) {
		context.animationQueue.enqueue[| 
			command.getExecuteAnimation(context)
		]
		undoStack.push(command)
		if(command.clearRedoStackOnExecute)
			redoStack.clear
	}

	def getContext() {
		context
	}
}