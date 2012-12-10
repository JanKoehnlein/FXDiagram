package de.itemis.javafx.diagram.behavior

import java.util.List
import de.itemis.javafx.diagram.ShapeContainer
import de.itemis.javafx.diagram.MyNode
import de.itemis.javafx.diagram.Connection
import de.itemis.javafx.diagram.Diagram

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<RapidButton> rapidButtons
	
	new(ShapeContainer host) {
		super(host)
	}	
	
	override activate(Diagram diagram) {
		val addAction = [ ShapeContainer source | 
			val target = new MyNode("new")
			val connection = new Connection(source, target)
			diagram.addShape(target)
			diagram.addConnection(connection)
			return
		]
		rapidButtons = newArrayList(
			new RapidButton(host, 0.5, 0, 'icons/add_16.png', addAction),
			new RapidButton(host, 0.5, 1, 'icons/add_16.png', addAction),
			new RapidButton(host, 0, 0.5, 'icons/add_16.png', addAction),
			new RapidButton(host, 1, 0.5, 'icons/add_16.png', addAction))
		rapidButtons.forEach[diagram.addButton(it)]
		host.node.onMouseEntered = [
			rapidButtons.forEach[show]
		]
		host.node.onMouseExited = [
			rapidButtons.forEach[fade]
		]
	}
}