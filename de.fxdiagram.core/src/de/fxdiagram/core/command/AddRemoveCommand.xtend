package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import java.util.Map
import java.util.Set
import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.util.Duration

class AddRemoveCommand extends AbstractAnimationCommand {
	
	boolean isAdd
	
	XDiagram diagram

	Set<? extends XShape> shapes
	
	Map<XConnection, Pair<XNode, XNode>> connectedNodesMap = newHashMap()
	
	Map<XShape, Double> shapeOpacities = newHashMap
	
	static def newAddCommand(XDiagram diagram, XShape... shapes) {
		new AddRemoveCommand(true, diagram, shapes)
	}
	
	static def newRemoveCommand(XDiagram diagram, XShape... shapes) {
		new AddRemoveCommand(false, diagram, shapes)
	}
	
	protected new(boolean isAdd, XDiagram diagram, XShape... shapes) {
		this.isAdd = isAdd
		this.diagram = diagram
		this.shapes = shapes.toSet
	}
	
	override createExecuteAnimation(CommandContext context) {
		shapes.filter(XNode).forEach[
			shapeOpacities.put(it, opacity)
			if(isAdd) {
				if(!diagram.nodes.contains(it)) 
					diagram.nodes += it
			} else {
				if(diagram.nodes.contains(it))				
					diagram.nodes -= it
			}
		]
		shapes.filter(XConnection).forEach[
			shapeOpacities.put(it, opacity)
			connectedNodesMap.put(it, source -> target)					
			if(isAdd) {
				if(!diagram.connections.contains(it)) 
					diagram.connections += it
			} else {
				if(diagram.connections.contains(it))
					diagram.connections -= it
			}
		]
		return null
	}

	override createUndoAnimation(CommandContext context) {
		if(isAdd)
			remove(context)
		else 
			add(context)

	}
	
	override createRedoAnimation(CommandContext context) {
		if(isAdd)
			add(context)
		else 
			remove(context)
	}
	
	protected def remove(extension CommandContext context) {
		new ParallelTransition => [ 
			children += shapes.map[disappear(defaultUndoDuration)]
			onFinished = [
				shapes.filter(XConnection).forEach[
					if(diagram.connections.contains(it))
						diagram.connections -= it
				]
				shapes.filter(XNode).forEach[
					if(diagram.nodes.contains(it))
						diagram.nodes -= it
				]
			]
		]
	}
	
	protected def add(extension CommandContext context) {
		shapes.filter(XNode).forEach[
			if(!diagram.nodes.contains(it))
				diagram.nodes += it
		]
		shapes.filter(XConnection).forEach[
			val nodes = connectedNodesMap.get(it)
			source = nodes.key
			target = nodes.value				
			if(!diagram.nodes.contains(it))
				diagram.connections += it
		]
		new ParallelTransition => [ 
			children += shapes.map[appear(defaultUndoDuration)]
		]
	}
	
	// have to state return type due to Xtend bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=436230
	protected def Animation appear(XShape node, Duration duration) {
		new FadeTransition => [
			it.node = node
			fromValue = 0
			toValue = shapeOpacities.get(node)
			cycleCount = 1
			it.duration = duration
		]
	}
	
	// have to state return type due to Xtend bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=436230
	protected def Animation disappear(XShape node, Duration duration) {
		new FadeTransition => [
			it.node = node
			fromValue = shapeOpacities.get(node)
			toValue = 0
			cycleCount = 1
			it.duration = duration
		]
	}
}