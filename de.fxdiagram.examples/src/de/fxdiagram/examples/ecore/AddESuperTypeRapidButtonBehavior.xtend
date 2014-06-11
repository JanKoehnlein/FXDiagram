package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.lib.model.AbstractConnectionRapidButtonBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.Set
import org.eclipse.emf.ecore.EClass

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.XRapidButtonAction

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
		new EClassNode(key.withDomainObject[
			domainObjectProvider.createEClassDescriptor(it.superType)
		])
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(EcoreDomainObjectProvider)
	}
	
	override protected createChooser(XRapidButton button, Set<ESuperTypeDescriptor> availableChoiceKeys, Set<ESuperTypeDescriptor> unavailableChoiceKeys) {
		val chooser = new CoverFlowChooser(host, button.chooserPosition)
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
	
	override protected createButtons(XRapidButtonAction addConnectionAction) {
		#[	new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, 'Discover supertypes'), addConnectionAction),
			new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, 'Discover supertypes'), addConnectionAction)
		]
	}
}

