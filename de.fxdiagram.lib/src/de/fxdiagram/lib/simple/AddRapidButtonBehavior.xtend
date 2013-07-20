package de.fxdiagram.lib.simple

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.core.tools.AbstractXNodeChooser
import de.fxdiagram.lib.tools.CarusselChooser
import de.fxdiagram.lib.tools.CoverFlowChooser
import de.fxdiagram.lib.tools.CubeChooser
import java.util.List

import static extension de.fxdiagram.core.Extensions.*

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<XRapidButton> rapidButtons
	
	new(XNode host) {
		super(host)
	}	
	
	override doActivate() {
		val host = this.getHost as XNode
		val addAction = [ XRapidButton button | 
			val target = new NestedDiagramNode("new")
			val source = button.getHost
			val connection = new XConnection(source, target)
			host.getDiagram.addNode(target)
			host.getDiagram.addConnection(connection)
			target.layoutX = 200 * (button.getPlacer.getXPos - 0.5) + source.layoutX 
			target.layoutY = 150 * (button.getPlacer.getYPos - 0.5) + source.layoutY
		]
		val chooseAction = [ XRapidButton button | 
			val chooser = new CarusselChooser(host, button.getChooserPosition)
			chooser.addChoices
			host.getRootDiagram.currentTool = chooser
		]
		val cubeChooseAction = [ XRapidButton button | 
			val chooser = new CubeChooser(host, button.getChooserPosition)
			chooser.addChoices
			host.getRootDiagram.currentTool = chooser
		]
		val coverFlowChooseAction = [ XRapidButton button | 
			val chooser = new CoverFlowChooser(host, button.getChooserPosition)
			chooser.addChoices
			host.getRootDiagram.currentTool = chooser
		]
		rapidButtons = #[
			new XRapidButton(host, 0.5, 0, 'icons/add_16.png', cubeChooseAction),
			new XRapidButton(host, 0.5, 1, 'icons/add_16.png', coverFlowChooseAction),
			new XRapidButton(host, 0, 0.5, 'icons/add_16.png', chooseAction),
			new XRapidButton(host, 1, 0.5, 'icons/add_16.png', addAction)]
		rapidButtons.forEach[host.getDiagram.addButton(it)]
	}
	
	protected def addChoices(AbstractXNodeChooser chooser) {
		for (i: 0..<20)
			chooser += new SimpleNode("node " +  i)
	} 
}