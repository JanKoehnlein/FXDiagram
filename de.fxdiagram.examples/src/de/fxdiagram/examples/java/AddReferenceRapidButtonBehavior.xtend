package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.List
import java.util.Set
import javafx.collections.ListChangeListener

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddReferenceRapidButtonBehavior extends AbstractBehavior<JavaTypeNode> {
	
	List<XRapidButton> buttons
	
	Set<Property> availableReferences = newHashSet
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected doActivate() {
		val model = host.javaTypeModel
		availableReferences += model.references
		if(!availableReferences.empty) {
			val addSuperTypeAction = [
				XRapidButton button |
				createChooser(button)
			]
			buttons = createButtons(addSuperTypeAction)
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
		val chooser = new CoverFlowChooser(host, button.getChooserPosition)
		availableReferences.forEach [
			chooser.addChoice(new JavaTypeNode(it.type), it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as Property 
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, 7, 10, 
					it.strokeProperty, false)
				new XConnectionLabel(it) => [
					text.text = reference.name
				]
			]
		]
		host.root.currentTool = chooser
	}	
}