package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.core.tools.chooser.CarusselChooser
import de.fxdiagram.core.tools.chooser.CoverFlowChooser
import de.fxdiagram.core.tools.chooser.CubeChooser
import java.util.List
import javafx.geometry.Point2D

import static extension de.fxdiagram.core.Extensions.*
import de.fxdiagram.core.tools.chooser.AbstractXNodeChooser

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<XRapidButton> rapidButtons
	
	new(XNode host) {
		super(host)
	}	
	
	override doActivate() {
		val addAction = [ XRapidButton button | 
			val target = new MyContainerNode("new")
			val source = button.getHost
			val connection = new XConnection(source, target)
			getHost.getDiagram.addNode(target)
			getHost.getDiagram.addConnection(connection)
			target.layoutX = 200 * (button.getPlacer.getXPos - 0.5) + source.layoutX 
			target.layoutY = 150 * (button.getPlacer.getYPos - 0.5) + source.layoutY
		]
		val chooseAction = [ XRapidButton button | 
			val chooser = new CarusselChooser(getHost, button.getChooserPosition)
			chooser.addChoices
			getHost.getRootDiagram.currentTool = chooser
		]
		val cubeChooseAction = [ XRapidButton button | 
			val chooser = new CubeChooser(getHost, button.getChooserPosition)
			chooser.addChoices
			getHost.getRootDiagram.currentTool = chooser
		]
		val coverFlowChooseAction = [ XRapidButton button | 
			val chooser = new CoverFlowChooser(getHost, button.getChooserPosition)
			chooser.addChoices
			getHost.getRootDiagram.currentTool = chooser
		]
		rapidButtons = #[
			new XRapidButton(getHost, 0.5, 0, 'icons/add_16.png', cubeChooseAction),
			new XRapidButton(getHost, 0.5, 1, 'icons/add_16.png', coverFlowChooseAction),
			new XRapidButton(getHost, 0, 0.5, 'icons/add_16.png', chooseAction),
			new XRapidButton(getHost, 1, 0.5, 'icons/add_16.png', addAction)]
		rapidButtons.forEach[getHost.getDiagram.addButton(it)]
		getHost.getNode.onMouseEntered = [
			rapidButtons.forEach[show]
		]
		getHost.getNode.onMouseExited = [
			rapidButtons.forEach[fade]
		]
	}
	
	protected def getChooserPosition(XRapidButton button) {
		val x = 200 * (button.getPlacer.getXPos - 0.5) + button.getHost.layoutX + 0.5 * button.getHost.layoutBounds.width 
		val y = 150 * (button.getPlacer.getYPos - 0.5) + button.getHost.layoutY + 0.5 * button.getHost.layoutBounds.height
		new Point2D(x, y) 
	}
	
	protected def addChoices(AbstractXNodeChooser chooser) {
		for(i:0..<20)
			chooser += new MyNode("node " +  i)
	} 
}