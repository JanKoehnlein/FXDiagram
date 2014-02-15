package de.fxdiagram.lib.simple

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XShape
import de.fxdiagram.core.tools.AbstractChooser
import de.fxdiagram.lib.tools.CarusselChooser
import de.fxdiagram.lib.tools.CoverFlowChooser
import de.fxdiagram.lib.tools.CubeChooser
import java.util.List

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.command.AddRemoveCommand

class AddRapidButtonBehavior<T extends XShape> extends AbstractHostBehavior<T> {

	List<XRapidButton> rapidButtons

	(AbstractChooser)=>void choiceInitializer

	new(T host) {
		super(host)
	}
	
	override getBehaviorKey() {
		AddRapidButtonBehavior
	}
	
	def setChoiceInitializer((AbstractChooser)=>void choiceInitializer) {
		this.choiceInitializer = choiceInitializer
	}

	override doActivate() {
		val host = this.getHost as XNode
		val addAction = [ XRapidButton button |
			val target = new SimpleNode("New Node")
			val source = button.getHost
			val connection = new XConnection(source, target)
			target.layoutX = 200 * (button.getPlacer.getXPos - 0.5) + source.layoutX
			target.layoutY = 150 * (button.getPlacer.getYPos - 0.5) + source.layoutY
			host.root.commandStack.execute(AddRemoveCommand.newAddCommand(host.diagram, target, connection))
		]
		val chooseAction = [ XRapidButton button |
			val chooser = new CarusselChooser(host, button.getChooserPosition)
			chooser.addChoices
			host.root.currentTool = chooser
		]
		val cubeChooseAction = [ XRapidButton button |
			val chooser = new CubeChooser(host, button.getChooserPosition)
			chooser.addChoices
			host.root.currentTool = chooser
		]
		val coverFlowChooseAction = [ XRapidButton button |
			val chooser = new CoverFlowChooser(host, button.getChooserPosition)
			chooser.addChoices
			host.root.currentTool = chooser
		]
		rapidButtons = #[new XRapidButton(host, 0.5, 0, getFilledTriangle(TOP, 'Add node'), cubeChooseAction),
			new XRapidButton(host, 0.5, 1, getFilledTriangle(BOTTOM, 'Add node'), coverFlowChooseAction),
			new XRapidButton(host, 0, 0.5, getFilledTriangle(LEFT, 'Add node'), chooseAction),
			new XRapidButton(host, 1, 0.5, getFilledTriangle(RIGHT, 'Add node'), addAction)]
		host.diagram.buttons += rapidButtons
	}

	protected def addChoices(AbstractChooser chooser) {
		choiceInitializer.apply(chooser)
	}
}
