package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.lib.tools.CarusselChooser
import java.util.List
import java.util.Set
import javafx.collections.ListChangeListener

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddReferenceRapidButtonBehavior extends AbstractHostBehavior<JavaTypeNode> {
	
	List<XRapidButton> buttons
	
	Set<Property> availableKeys = newLinkedHashSet
	Set<Property> unavailableKeys = newHashSet
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override getBehaviorKey() {
		AddReferenceRapidButtonBehavior
	}
	
	override protected doActivate() {
		availableKeys += host.javaTypeModel.properties
		if(!availableKeys.empty) {
			val addConnectionAction = [
				XRapidButton button |
				createChooser(button)
			]
			buttons = createButtons(addConnectionAction)
			host.diagram.buttons += buttons
			host.diagram.connections.addListener [
				ListChangeListener.Change<? extends XConnection> change |
				while(change.next) {
					if(change.wasAdded) 
						change.addedSubList.forEach[ 
							if(availableKeys.remove(key))
								unavailableKeys.add(key as Property)
						]
					if(change.wasRemoved) 
						change.removed.forEach[
							if(unavailableKeys.remove(key))
								availableKeys.add(key as Property)
						]
				}
				if(availableKeys.empty)
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
		availableKeys.forEach [
			chooser.addChoice(new JavaTypeNode(it.type), it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as Property 
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [
					text.text = reference.name
				]
			]
		]
		host.root.currentTool = chooser
	}	
}