package de.fxdiagram.core.command

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import javafx.animation.FadeTransition
import javafx.animation.SequentialTransition
import javafx.util.Duration
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class ChangeDiagramCommand extends AbstractAnimationCommand {
	
	XDiagram newDiagram
	XDiagram oldDiagram
	
	new(XDiagram newDiagram) {
		this.newDiagram = newDiagram
	}
	
	override createExecuteAnimation(CommandContext context) {
		oldDiagram = context.root.diagram
		swap(context.root, newDiagram, context.defaultUndoDuration)
	}
	
	override createUndoAnimation(CommandContext context) {
		swap(context.root, oldDiagram, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		swap(context.root, newDiagram, context.defaultUndoDuration)
	}

	protected def swap(XRoot root, XDiagram appear, Duration duration) {
		new SequentialTransition => [
			children += new FadeTransition => [
				node = root.diagram
				fromValue = 1
				toValue = 0
				cycleCount = 1
				it.duration = if(root.diagram.nodes.empty) 0.millis else duration/2
			] => [ 
				onFinished = [
					root.diagram = appear
					appear.opacity = 0
				]
			]
			children += new FadeTransition => [
				node = appear
				fromValue = 0
				toValue = 1
				cycleCount = 1
				it.duration = if(appear.nodes.empty) 0.millis else duration/2
			] 
		]
	}
}