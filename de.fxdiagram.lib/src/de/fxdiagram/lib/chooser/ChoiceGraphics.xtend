package de.fxdiagram.lib.chooser

import de.fxdiagram.core.XActivatable
import de.fxdiagram.core.XNode
import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Accessors

interface ChoiceGraphics extends XActivatable {
	
	def void setInterpolatedPosition(double interpolatedPosition) 
	
	def void nodeChosen(XNode choice)
	
	def void relocateButtons(Node minusButton, Node plusButton)
	
	def boolean hasButtons()
	
	def void setChooser(AbstractBaseChooser chooser)
}

abstract class AbstractChoiceGraphics implements ChoiceGraphics {
	
	@Accessors
	AbstractBaseChooser chooser
	
	protected def getChoiceNodes() {
		chooser.nodes
	}
	
	protected def getChoiceGroup() {
		chooser.group
	}
	
	override activate() {
	}
	
	override nodeChosen(XNode choice) {
	}
	
	override relocateButtons(Node minusButton, Node plusButton) {
	}
	
	override hasButtons() {
		true
	}
}