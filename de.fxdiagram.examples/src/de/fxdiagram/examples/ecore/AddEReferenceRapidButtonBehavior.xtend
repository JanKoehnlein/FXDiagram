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

class AddEReferenceRapidButtonBehavior extends AbstractConnectionRapidButtonBehavior<EClassNode, EReference, EReferenceKey> {
	
	new(EClassNode host) {
		super(host)
	}
	
	override getBehaviorKey() {
		AddEReferenceRapidButtonBehavior
	}

	override protected getInitialModelChoices() {
		host.EClass.EReferences
	}
	
	override protected getChoiceKey(EReference model) {
		new EReferenceKey(model)
	}
	
	override protected createNode(EReferenceKey key) {
		new EClassNode(key.left.EReferenceType)
	}

	override protected createChooser(XRapidButton button, Set<EReferenceKey> availableChoiceKeys, Set<EReferenceKey> unavailableChoiceKeys) {
		val chooser = new CarusselChooser(host, button.chooserPosition)
		availableChoiceKeys.forEach[
			chooser.addChoice(it.createNode, it)
		]
		chooser.connectionProvider = [	host, choice, key |
			val reference = (key as EReferenceKey).left
			new XConnection(host, choice, key) => [
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

class EReferenceKey {
	EReference left
	EReference right
	
	new(EReference left) {
		this.left = left
		this.right = left.EOpposite ?: left
	}

	def getLeft() {
		left
	}

	def getRight() {
		right
	}
	
	override hashCode() {
		left.hashCode + right.hashCode
	}
	
	override equals(Object other) {
		switch other {
			EReferenceKey:
				return (other.left == this.left && other.right == this.right)
					|| (other.left == this.right && other.right == this.left)
			default: 
				return false
		}
	}		
	
	override toString() {
		'''EReferenceKey «left»«IF right!=left» / «right»«ENDIF»'''
	}
	
}