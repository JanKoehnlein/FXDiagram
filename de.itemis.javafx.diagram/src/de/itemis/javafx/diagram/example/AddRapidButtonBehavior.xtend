package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XConnection
import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.XRapidButton
import de.itemis.javafx.diagram.behavior.AbstractBehavior
import de.itemis.javafx.diagram.tools.chooser.CarusselChooser
import de.itemis.javafx.diagram.tools.chooser.CoverFlowChooser
import de.itemis.javafx.diagram.tools.chooser.CubeChooser
import java.util.List
import javafx.geometry.Point2D

import static extension de.itemis.javafx.diagram.Extensions.*
import de.itemis.javafx.diagram.tools.chooser.AbstractXNodeChooser

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<XRapidButton> rapidButtons
	
	new(XNode host) {
		super(host)
	}	
	
	override doActivate() {
		val addAction = [ XRapidButton button | 
			val target = new MyContainerNode("new")
			val source = button.host
			val connection = new XConnection(source, target)
			host.diagram.addNode(target)
			host.diagram.addConnection(connection)
			target.layoutX = 200 * (button.placer.XPos - 0.5) + source.layoutX 
			target.layoutY = 150 * (button.placer.YPos - 0.5) + source.layoutY
		]
		val chooseAction = [ XRapidButton button | 
			val chooser = new CarusselChooser(host, button.chooserPosition)
			chooser.addChoices
			host.rootDiagram.currentTool = chooser
		]
		val cubeChooseAction = [ XRapidButton button | 
			val chooser = new CubeChooser(host, button.chooserPosition)
			chooser.addChoices
			host.rootDiagram.currentTool = chooser
		]
		val coverFlowChooseAction = [ XRapidButton button | 
			val chooser = new CoverFlowChooser(host, button.chooserPosition)
			chooser.addChoices
			host.rootDiagram.currentTool = chooser
		]
		rapidButtons = #[
			new XRapidButton(host, 0.5, 0, 'icons/add_16.png', cubeChooseAction),
			new XRapidButton(host, 0.5, 1, 'icons/add_16.png', coverFlowChooseAction),
			new XRapidButton(host, 0, 0.5, 'icons/add_16.png', chooseAction),
			new XRapidButton(host, 1, 0.5, 'icons/add_16.png', addAction)]
		rapidButtons.forEach[host.diagram.addButton(it)]
		host.node.onMouseEntered = [
			rapidButtons.forEach[show]
		]
		host.node.onMouseExited = [
			rapidButtons.forEach[fade]
		]
	}
	
	protected def getChooserPosition(XRapidButton button) {
		val x = 200 * (button.placer.XPos - 0.5) + button.host.layoutX + 0.5 * button.host.layoutBounds.width 
		val y = 150 * (button.placer.YPos - 0.5) + button.host.layoutY + 0.5 * button.host.layoutBounds.height
		new Point2D(x, y) 
	}
	
	protected def addChoices(AbstractXNodeChooser chooser) {
		for(i:0..<20)
			chooser += new MyNode("node " +  i)
	} 
}