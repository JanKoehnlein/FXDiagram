package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import de.fxdiagram.lib.tools.CarusselChooser
import java.util.Set

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

class AddReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, Property, Property> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.javaTypeModel.references
	}
	
	override protected getChoiceKey(Property property) {
		property
	}
	
	override protected createNode(Property key) {
		new JavaTypeNode(key.type)
	}
	
	
	override protected createChooser(XRapidButton button, Set<Property> availableChoiceKeys, Set<Property> unavailableChoiceKeys) {
		val chooser = new CarusselChooser(host, button.chooserPosition)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as Property 
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [
					text.text = reference.name
				]
			]
		]
		chooser
	}
	
	override protected createButtons((XRapidButton)=>void addConnectionAction) {
		#[	new XRapidButton(host, 0, 0.5, getArrowButton(LEFT, 'Discover properties'), addConnectionAction),
			new XRapidButton(host, 1, 0.5, getArrowButton(RIGHT, 'Discover properties'), addConnectionAction) ]
	}
}