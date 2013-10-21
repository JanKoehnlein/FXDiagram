package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.Set

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddSuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, Class<?>, SuperTypeKey> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.javaTypeModel.superTypes
	}
	
	override protected getChoiceKey(Class<?> superType) {
		new SuperTypeKey((host as JavaTypeNode).javaType, superType)
	}
	
	override protected createNode(SuperTypeKey key) {
		new JavaTypeNode(key.superType)
	}
	
	override protected createChooser(XRapidButton button, Set<SuperTypeKey> availableChoiceKeys, Set<SuperTypeKey> unavailableChoiceKeys) {
		val chooser = new CoverFlowChooser(host, button.chooserPosition)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, key |
			new XConnection(host, choice, key) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					it.strokeProperty, host.diagram.backgroundPaintProperty, false)
			]
		]
		chooser
	}
	
	override protected createButtons((XRapidButton)=>void addConnectionAction) {
		#[	new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, 'Discover supertypes'), addConnectionAction),
			new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, 'Discover supertypes'), addConnectionAction)
		]
	}
}

@Data
class SuperTypeKey {
	Class<?> subType
	Class<?> superType
}