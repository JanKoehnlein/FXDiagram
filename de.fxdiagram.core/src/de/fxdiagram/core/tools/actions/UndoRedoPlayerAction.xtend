package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.AnimationQueueListener
import javafx.animation.FadeTransition
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.HBox

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle

class UndoRedoPlayerAction implements DiagramAction {
	
    XRoot root

	AnimationQueueListener animationQueueListener
    
	Node controlPanel
	
	FadeTransition fadeTransition
	
	override perform(XRoot root) {
		this.root = root
		this.controlPanel = createControlPanel
		root.headsUpDisplay.add(controlPanel, Pos.BOTTOM_CENTER);
	}
	
	protected def createControlPanel() {
		new StackPane => [
			alignment = Pos.CENTER
			children += new Rectangle => [
				// ths rectangle is needed as buttons slightly change their size when selected
				// causing stack overflow in the headsUpDisplay's layout  
				width = 400
				height = 60
				opacity = 0
			]
			children += new HBox => [
				alignment = Pos.CENTER
				children += new Button => [
					text = "rewind"
					onAction = [
						startFastMode(true)
					]
				]
				children += new Button => [
					text = "undo"
					onAction = [
						stopFastMode
						root.commandStack.undo
					]
				]
				children += new Button => [
					text = "pause"
					onAction = [
						stopFastMode
					]
				]
				children += new Button => [
					text = "redo"
					onAction = [
						stopFastMode
						root.commandStack.redo
					]
				]
				children += new Button => [
					text = "forward"
					onAction = [
						startFastMode(false)
					]
				]
				onMouseExited = [
					fade
				]
				onMouseEntered = [
					show
				]
			]
		]
	}
	
	protected def show() {
		if(fadeTransition != null)
			fadeTransition.stop
		controlPanel.opacity = 1
	}	

	protected def fade() {
		stopFastMode
		fadeTransition = new FadeTransition => [
			node = controlPanel
			duration = 1.seconds
			fromValue = 1
			toValue = 0
			onFinished = [
				root.headsUpDisplay.children -= controlPanel;
			]
			play
		]
	}	
	
	protected def stopFastMode() {
		if(animationQueueListener != null)
			root.commandStack.context.animationQueue.removeListener(animationQueueListener)
	}

	protected def startFastMode(boolean isUndo) {
		stopFastMode
		val commandStack = root.commandStack
		animationQueueListener = [|
			if(isUndo && commandStack.canUndo)
				commandStack.undo
			else if(!isUndo && commandStack.canRedo)
				commandStack.redo
			else
				stopFastMode
		]
		root.commandStack.context.animationQueue.addListener(animationQueueListener)
		if(isUndo) 
			commandStack.undo
		else 
			commandStack.redo
	}
	
}