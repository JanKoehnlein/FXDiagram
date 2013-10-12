package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.DiamondArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.lib.tools.CarusselChooser
import java.util.List
import java.util.Set
import javafx.collections.ListChangeListener
import org.eclipse.emf.ecore.EReference

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddEReferenceRapidButtonBehavior extends AbstractBehavior<EClassNode> {
	
	List<XRapidButton> buttons
	
	Set<EReference> availableReferences = newHashSet
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected doActivate() {
		availableReferences += host.EClass.EReferences
		if(!availableReferences.empty) {
			val addReferenceAction = [
				XRapidButton button |
				createChooser(button)
			]
			buttons = createButtons(addReferenceAction)
			host.diagram.buttons += buttons
			host.diagram.connections.addListener [
				ListChangeListener.Change<? extends XConnection> change |
				while(change.next) {
					if(change.wasAdded) 
						change.addedSubList.forEach[ availableReferences.remove(key) ]
				}
				if(availableReferences.empty)
					host.diagram.buttons -= buttons
			]  			
		}
	}
	
	protected def createButtons((XRapidButton)=>void addReferencesAction) {
		#[
			new XRapidButton(host, 0, 0.5, getArrowButton(LEFT, 'Discover properties'), addReferencesAction),
			new XRapidButton(host, 1, 0.5, getArrowButton(RIGHT, 'Discover properties'), addReferencesAction)
		]
	}
	
	protected def createChooser(XRapidButton button) {
		val chooser = new CarusselChooser(host, button.getChooserPosition)
		availableReferences.forEach [
			chooser.addChoice(new EClassNode(it.EReferenceType), it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as EReference 
			new XConnection(host, choice, reference) => [
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
		host.root.currentTool = chooser
	}	
}