package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XConnection
import de.itemis.javafx.diagram.XNode
import java.util.List
import de.itemis.javafx.diagram.behavior.RapidButton
import de.itemis.javafx.diagram.behavior.AbstractBehavior

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<RapidButton> rapidButtons
	
	new(XNode host) {
		super(host)
	}	
	
	override activate() {
		val addAction = [ RapidButton button | 
			val target = new MyNode("new")
			val source = button.host
			val connection = new XConnection(source, target)
			source.diagram.addNode(target)
			source.diagram.addConnection(connection)
			target.translateX = 200 * (button.placer.XPos - 0.5) + source.translateX 
			target.translateY = 150 * (button.placer.YPos - 0.5) + source.translateY
			return
		]
		rapidButtons = newArrayList(
			new RapidButton(host, 0.5, 0, 'icons/add_16.png', addAction),
			new RapidButton(host, 0.5, 1, 'icons/add_16.png', addAction),
			new RapidButton(host, 0, 0.5, 'icons/add_16.png', addAction),
			new RapidButton(host, 1, 0.5, 'icons/add_16.png', addAction))
		rapidButtons.forEach[host.diagram.addButton(it)]
		host.node.onMouseEntered = [
			rapidButtons.forEach[show]
		]
		host.node.onMouseExited = [
			rapidButtons.forEach[fade]
		]
	}
}