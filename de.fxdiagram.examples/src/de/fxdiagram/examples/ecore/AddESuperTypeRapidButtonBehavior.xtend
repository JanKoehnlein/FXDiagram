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

class AddESuperTypeRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EClass, ESuperTypeHandle> {
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected getInitialModelChoices() {
		host.EClass.ESuperTypes
	}
	
	override protected getChoiceKey(EClass superType) {
		domainObjectProvider.createESuperClassHandle(new ESuperType(host.EClass, superType)) 
	}
	
	override protected createNode(ESuperTypeHandle key) {
		new EClassNode => [
			domainObject = domainObjectProvider.createDomainObjectHandle(key.domainObject.superType)
		]
	}
	
	protected def getDomainObjectProvider() {
		host.root.getDomainObjectProvider(EcoreDomainObjectProvider)
	}
	
	override protected createChooser(XRapidButton button, Set<ESuperTypeHandle> availableChoiceKeys, Set<ESuperTypeHandle> unavailableChoiceKeys) {
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

