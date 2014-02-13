package de.fxdiagram.lib.model

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.tools.AbstractChooser
import java.util.List
import java.util.Set
import javafx.collections.ListChangeListener

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

abstract class AbstractConnectionRapidButtonBehavior<HOST extends XNode, MODEL, KEY extends DomainObjectDescriptor> extends AbstractHostBehavior<HOST> {
	
	Set<KEY> availableChoiceKeys = newLinkedHashSet	
	Set<KEY> unavailableChoiceKeys = newHashSet
	
	List<XRapidButton> buttons = newArrayList
	
	new(HOST host) {
		super(host)
	}
	
	override getBehaviorKey() {
		class
	}
	
	override protected doActivate() {
		availableChoiceKeys += initialModelChoices.map[choiceKey]
		if(!availableChoiceKeys.empty) {
			val addConnectionAction = [
				XRapidButton button |
				val chooser = createChooser(button, availableChoiceKeys, unavailableChoiceKeys)
				host.root.currentTool = chooser
			]
			buttons += createButtons(addConnectionAction)
			host.diagram.buttons += buttons
			host.diagram.connections.forEach [
				if(availableChoiceKeys.remove(domainObject))						
					unavailableChoiceKeys.add(domainObject as KEY)
			]
			host.diagram.connections.addListener [
				ListChangeListener.Change<? extends XConnection> change |
				val hadChoices = !availableChoiceKeys.empty
				while(change.next) {
					if(change.wasAdded) 
						change.addedSubList.forEach[ 
							if(availableChoiceKeys.remove(domainObject)) {
								unavailableChoiceKeys.add(domainObject as KEY)
							}
						]
					if(change.wasRemoved) 
						change.removed.forEach[
							if(unavailableChoiceKeys.remove(domainObject)) {
								availableChoiceKeys.add(domainObject as KEY)
							}
						]
				}
				if(availableChoiceKeys.empty) 
					host.diagram.buttons -= buttons
				else if(!hadChoices) 
					host.diagram.buttons += buttons
			]  			
		}
	}	
	
	protected def Iterable<MODEL> getInitialModelChoices() 
	
	protected def KEY getChoiceKey(MODEL model)
	
	protected def XNode createNode(KEY key)

	protected def Iterable<XRapidButton> createButtons((XRapidButton)=>void addConnectionAction) 
		
	protected def AbstractChooser createChooser(XRapidButton button, Set<KEY> availableChoiceKeys, Set<KEY> unavailableChoiceKeys) 
	
}

