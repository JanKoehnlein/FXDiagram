package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.DiamondArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.CarusselChoice
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import java.util.Set
import org.eclipse.emf.ecore.EReference

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddEReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EReference, EReferenceDescriptor> {
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.EClass.EReferences
	}
	
	override protected getChoiceKey(EReference model) {
		domainObjectProvider.createEReferenceDescriptor(model)
	}
	
	override protected createNode(EReferenceDescriptor handle) {
		new EClassNode(domainObjectProvider.createEClassDescriptor(handle.domainObject.EReferenceType))
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(EcoreDomainObjectProvider)
	}

	override protected createChooser(RapidButton button, Set<EReferenceDescriptor> availableChoiceKeys, Set<EReferenceDescriptor> unavailableChoiceKeys) {
		val chooser = new ConnectedNodeChooser(host, button.position, new CarusselChoice)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [ host, choice, descriptor |
			val reference = (descriptor as EReferenceDescriptor).domainObject
			new XConnection(host, choice, descriptor) => [
				targetArrowHead = if (reference.container)
						new DiamondArrowHead(it, false)
					else 
						new LineArrowHead(it, false)
				sourceArrowHead = if (reference.containment) 
						new DiamondArrowHead(it, true)
					else if(!reference.container && reference.EOpposite !== null) 
						new LineArrowHead(it, true)

				new XConnectionLabel(it) => [
					text.text = reference.name
					position = 0.8
				]
				if(reference.EOpposite !== null) {
					new XConnectionLabel(it) => [
						text.text = reference.EOpposite.name
						position = 0.2
					]
				}
			]
		]
		chooser
	}
	
	override protected createButtons(RapidButtonAction addConnectionAction) {
		#[	new RapidButton(host, LEFT, getArrowButton(LEFT, 'Discover references'), addConnectionAction),
			new RapidButton(host, RIGHT, getArrowButton(RIGHT, 'Discover references'), addConnectionAction) ]
	}
}

