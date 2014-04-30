package de.fxdiagram.core.behavior

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.viewport.ViewportTransition
import de.fxdiagram.core.command.ViewportCommand

interface NavigationBehavior extends Behavior {
	
	def boolean next()
	
	def boolean previous()
} 

class DiagramNavigationBehavior extends AbstractHostBehavior<XDiagram> implements NavigationBehavior {
	
	new(XDiagram host) {
		super(host)
	}
	
	override protected doActivate() {
	}

	override getBehaviorKey() {
		NavigationBehavior
	}
	
	override next() {
		val nodes = host.nodes
		val current = nodes.findFirst[selected] 
		val XNode next =  
			if(current != null)
				nodes.get((nodes.indexOf(current) + 1) % nodes.size)
			else 
				nodes.head
		next?.reveal
		return next != null
	}
	
	override previous() {
		val nodes = host.nodes
		val current = nodes.findLast[selected] 
		val XNode previous =  
			if(current != null)
				nodes.get((nodes.indexOf(current) + nodes.size - 1) % nodes.size)
			else 
				nodes.last
		previous?.reveal
		return previous != null
	}

	protected def reveal(XShape node) {
		val ViewportCommand command = [
			new ViewportTransition(host.root, node.localToDiagram(node.boundsInLocal.center), 1) => [
				onFinished = [
					host.root.currentSelection.forEach[selected = false]
					node.selected = true
				]
			]
		]
		node.root.commandStack.execute(command)
	}
}