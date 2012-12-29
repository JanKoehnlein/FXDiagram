package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XConnection
import de.itemis.javafx.diagram.XNode
import java.util.List
import de.itemis.javafx.diagram.XRapidButton
import de.itemis.javafx.diagram.behavior.AbstractBehavior
import static extension de.itemis.javafx.diagram.Extensions.*

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<XRapidButton> rapidButtons
	
	new(XNode host) {
		super(host)
	}	
	
	override activate() {
		val addAction = [ XRapidButton button | 
			val target = new MyNode("new")
			val source = button.host
			val connection = new XConnection(source, target)
			host.diagram.addNode(target)
			host.diagram.addConnection(connection)
			target.layoutX = 200 * (button.placer.XPos - 0.5) + source.layoutX 
			target.layoutY = 150 * (button.placer.YPos - 0.5) + source.layoutY
			return
		]
		rapidButtons = newArrayList(
			new XRapidButton(host, 0.5, 0, 'icons/add_16.png', addAction),
			new XRapidButton(host, 0.5, 1, 'icons/add_16.png', addAction),
			new XRapidButton(host, 0, 0.5, 'icons/add_16.png', addAction),
			new XRapidButton(host, 1, 0.5, 'icons/add_16.png', addAction))
		rapidButtons.forEach[host.diagram.addButton(it)]
		host.node.onMouseEntered = [
			rapidButtons.forEach[show]
		]
		host.node.onMouseExited = [
			rapidButtons.forEach[fade]
		]
	}
}