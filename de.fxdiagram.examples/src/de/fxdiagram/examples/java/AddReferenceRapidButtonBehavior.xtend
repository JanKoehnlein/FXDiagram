package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.CarusselChoice
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import java.util.Set

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, JavaProperty, JavaPropertyDescriptor> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.javaTypeModel.references
	}
	
	override protected getChoiceKey(JavaProperty property) {
		domainObjectProvider.createJavaPropertyDescriptor(property)
	}
	
	override protected createNode(JavaPropertyDescriptor key) {
		new JavaTypeNode(domainObjectProvider.createJavaTypeDescriptor(key.domainObject.type))
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(JavaModelProvider)
	}
	
	override protected createChooser(RapidButton button, Set<JavaPropertyDescriptor> availableChoiceKeys, Set<JavaPropertyDescriptor> unavailableChoiceKeys) {
		val chooser = new ConnectedNodeChooser(host, button.position, new CarusselChoice)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as JavaPropertyDescriptor
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [
					text.text = reference.domainObject.name
				]
			]
		]
		chooser
	}
	
	override protected createButtons(RapidButtonAction addConnectionAction) {
		#[	new RapidButton(host, LEFT, getArrowButton(LEFT, 'Discover properties'), addConnectionAction),
			new RapidButton(host, RIGHT, getArrowButton(RIGHT, 'Discover properties'), addConnectionAction) ]
	}
}