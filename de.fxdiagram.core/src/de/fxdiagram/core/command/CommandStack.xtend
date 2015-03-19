package de.fxdiagram.core.command

import de.fxdiagram.core.XRoot
import java.util.LinkedList
import de.fxdiagram.core.XActivatable

/**
 * Executes and stores {@link AnimationCommands} for undo/redo functionality.
 * 
 * The command stack is reachable via the {@link XRoot} of the application.
 */
class CommandStack implements XActivatable {
	
	LinkedList<AnimationCommand> undoStack = newLinkedList
	LinkedList<AnimationCommand> redoStack = newLinkedList

	CommandContext context 
	
	AnimationCommand lastBeforeSave = null
	
	new(XRoot root) {
		context = new CommandContext(root)
	}
	
	override activate() {
		context.root.needsSaveProperty.addListener [ p, o, n |
			if(n == false)
				lastBeforeSave = undoStack.peek
		]
	}
	
	def clear() {
		undoStack.clear
		redoStack.clear
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
			context.root.needsSave = undoStack.peek != lastBeforeSave
		}
	}
	
	def void redo() {
		if(canRedo) {
			val command = redoStack.pop
			context.animationQueue.enqueue[|
				command.getRedoAnimation(context)
			]
			undoStack.push(command)
			context.root.needsSave = undoStack.peek != lastBeforeSave
		}
	}
	
	def execute(AnimationCommand command) {
		context.animationQueue.enqueue[| 
			command.getExecuteAnimation(context)
		]
		undoStack.push(command)
		context.root.needsSave = undoStack.peek != lastBeforeSave
		if(command.clearRedoStackOnExecute)
			redoStack.clear
	}

	def getContext() {
		context
	}
}