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

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, JavaProperty, JavaPropertyHandle> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.javaTypeModel.references
	}
	
	override protected getChoiceKey(JavaProperty property) {
		domainObjectProvider.createJavaPropertyHandle(property)
	}
	
	override protected createNode(JavaPropertyHandle key) {
		new JavaTypeNode => [
			domainObject = domainObjectProvider.createJavaTypeHandle(key.domainObject.type)
		]
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(JavaModelProvider)
	}
	
	override protected createChooser(XRapidButton button, Set<JavaPropertyHandle> availableChoiceKeys, Set<JavaPropertyHandle> unavailableChoiceKeys) {
		val chooser = new CarusselChooser(host, button.chooserPosition)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as JavaPropertyHandle
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [
					text.text = reference.domainObject.name
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