package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.DiamondArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import de.fxdiagram.lib.tools.CarusselChooser
import java.util.Set
import org.eclipse.emf.ecore.EReference

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddEReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EReference, EReferenceHandle> {
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.EClass.EReferences
	}
	
	override protected getChoiceKey(EReference model) {
		domainObjectProvider.createEReferenceHandle(model)
	}
	
	override protected createNode(EReferenceHandle handle) {
		new EClassNode => [
			domainObject = domainObjectProvider.createDomainObjectHandle(handle.domainObject.EReferenceType)
		]
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(EcoreDomainObjectProvider)
	}

	override protected createChooser(XRapidButton button, Set<EReferenceHandle> availableChoiceKeys, Set<EReferenceHandle> unavailableChoiceKeys) {
		val chooser = new CarusselChooser(host, button.chooserPosition)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [ host, choice, handle |
			val reference = handle.domainObject as EReference
			new XConnection(host, choice, handle) => [
				targetArrowHead = if (reference.container)
						new DiamondArrowHead(it, false)
					else 
						new LineArrowHead(it, false)
				sourceArrowHead = if (reference.containment) 
						new DiamondArrowHead(it, true)
					else if(!reference.container && reference.EOpposite != null) 
						new LineArrowHead(it, true)

				new XConnectionLabel(it) => [
					text.text = reference.name
					position = 0.8
				]
				if(reference.EOpposite != null) {
					new XConnectionLabel(it) => [
						text.text = reference.EOpposite.name
						position = 0.2
					]
				}
			]
		]
		chooser
	}
	
	override protected createButtons((XRapidButton)=>void addConnectionAction) {
		#[	new XRapidButton(host, 0, 0.5, getArrowButton(LEFT, 'Discover references'), addConnectionAction),
			new XRapidButton(host, 1, 0.5, getArrowButton(RIGHT, 'Discover references'), addConnectionAction) ]
	}
}

