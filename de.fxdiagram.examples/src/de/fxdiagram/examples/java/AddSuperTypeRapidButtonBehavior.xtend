package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import java.util.Set

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddSuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<JavaTypeNode, Class<?>, JavaSuperTypeDescriptor> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.javaTypeModel.superTypes
	}
	
	override protected getChoiceKey(Class<?> superType) {
		domainObjectProvider.createJavaSuperClassDescriptor(new JavaSuperTypeHandle(host.javaType, superType))
	}
	
	override protected createNode(JavaSuperTypeDescriptor key) {
		new JavaTypeNode(domainObjectProvider.createJavaTypeDescriptor(key.domainObject.superType))
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(JavaModelProvider)
	}
	
	override protected createChooser(RapidButton button, Set<JavaSuperTypeDescriptor> availableChoiceKeys, Set<JavaSuperTypeDescriptor> unavailableChoiceKeys) {
		val chooser = new ConnectedNodeChooser(host, button.position, new CoverFlowChoice)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, key |
			new XConnection(host, choice, key) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					null, host.diagram.backgroundPaint, false)
			]
		]
		chooser
	}
	
	override protected createButtons(RapidButtonAction addConnectionAction) {
		#[	new RapidButton(host, TOP, getTriangleButton(TOP, 'Discover supertypes'), addConnectionAction),
			new RapidButton(host, BOTTOM, getTriangleButton(BOTTOM, 'Discover supertypes'), addConnectionAction)
		]
	}
}

