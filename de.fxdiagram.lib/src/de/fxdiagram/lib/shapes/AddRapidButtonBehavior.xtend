	package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.core.tools.chooser.AbstractXNodeChooser
import de.fxdiagram.core.tools.chooser.CarusselChooser
import de.fxdiagram.core.tools.chooser.CoverFlowChooser
import de.fxdiagram.core.tools.chooser.CubeChooser
import java.util.List

import static extension de.fxdiagram.core.Extensions.*

class AddRapidButtonBehavior extends AbstractBehavior {
	
	List<XRapidButton> rapidButtons
	
	new(XNode host) {
		super(host)
	}	
	
	override doActivate() {
		val host = this.host as XNode
		val addAction = [ XRapidButton button | 
			val target = new NestedDiagramNode("new")
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
	}
	
	protected def addChoices(AbstractXNodeChooser chooser) {
		for (i: 0..<20)
			chooser += new SimpleNode("node " +  i)
	} 
}