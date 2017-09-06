package de.fxdiagram.lib.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.AnimationQueue
import de.fxdiagram.core.tools.actions.DiagramAction
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.animation.FadeTransition
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle

import static de.fxdiagram.core.extensions.ClassLoaderExtensions.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*

/**
 * An action adding a media control to rewind/replay changes from the undo stack. 
 */
class UndoRedoPlayerAction implements DiagramAction {
	
    XRoot root

	AnimationQueue.Listener animationQueueListener
    
	Node controlPanel
	
	FadeTransition fadeTransition
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.P
	}
	
	override getSymbol() {
		SymbolType.PLAY
	}
	
	override getTooltip() {
		'Undo player'
	}

	override perform(XRoot root) {
		this.root = root
		this.controlPanel = createControlPanel
		root.getHeadsUpDisplay.add(controlPanel, Pos.BOTTOM_CENTER);
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
					id = "back-button"
					text = "Back"
					onAction = [
						startFastMode(true)
					]
				]
				children += new Button => [
					id = "reverse-button"
					text = "undo"
					onAction = [
						stopFastMode
						root.getCommandStack.undo
					]
				]
				children += new Button => [
					id = "pause-button"
					text = "pause"
					onAction = [
						stopFastMode
					]
				]
				children += new Button => [
					id = "play-button"
					text = "redo"
					onAction = [
						stopFastMode
						root.getCommandStack.redo
					]
				]
				children += new Button => [
					id = "forward-button"
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
			stylesheets += toURI(this, '../media/MovieNode.css')
		]
	}
	
	protected def show() {
		if(fadeTransition !== null)
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
				root.getHeadsUpDisplay.children -= controlPanel;
			]
			play
		]
	}	
	
	protected def stopFastMode() {
		if(animationQueueListener !== null)
			root.getCommandStack.getContext.getAnimationQueue.removeListener(animationQueueListener)
	}

	protected def startFastMode(boolean isUndo) {
		stopFastMode
		val commandStack = root.getCommandStack
		animationQueueListener = [|
			if(isUndo && commandStack.canUndo)
				commandStack.undo
			else if(!isUndo && commandStack.canRedo)
				commandStack.redo
			else
				stopFastMode
		]
		root.getCommandStack.getContext.getAnimationQueue.addListener(animationQueueListener)
		if(isUndo) 
			commandStack.undo
		else 
			commandStack.redo
	}
	
}