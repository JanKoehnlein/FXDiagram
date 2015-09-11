package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import java.util.Set
import javafx.geometry.Side
import org.eclipse.emf.ecore.EClass

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddESuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EClass, ESuperTypeDescriptor> {
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.EClass.ESuperTypes
	}
	
	override protected getChoiceKey(EClass superType) {
		domainObjectProvider.createESuperClassDescriptor(
			new ESuperTypeHandle(host.EClass, superType))
	}
	
	override protected createNode(ESuperTypeDescriptor key) {
		new EClassNode(domainObjectProvider.createEClassDescriptor(key.domainObject.superType))
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(EcoreDomainObjectProvider)
	}
	
	override protected createChooser(RapidButton button, Set<ESuperTypeDescriptor> availableChoiceKeys, Set<ESuperTypeDescriptor> unavailableChoiceKeys) {
		val chooser = new ConnectedNodeChooser(host, button.position, new CoverFlowChoice)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [
			host, choice, descriptor |
			new XConnection(host, choice, descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					null, host.diagram.backgroundPaint, false)
			]
		]
		chooser
	}
	
	override protected createButtons(RapidButtonAction addConnectionAction) {
		#[	new RapidButton(host, Side.TOP, getTriangleButton(TOP, 'Discover supertypes'), addConnectionAction),
			new RapidButton(host, Side.BOTTOM, getTriangleButton(BOTTOM, 'Discover supertypes'), addConnectionAction)
		]
	}
}

