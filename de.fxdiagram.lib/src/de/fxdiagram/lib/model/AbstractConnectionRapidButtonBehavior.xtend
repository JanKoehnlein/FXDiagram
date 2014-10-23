package de.fxdiagram.lib.model

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.tools.AbstractChooser
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.buttons.RapidButtonBehavior
import java.util.Set

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

abstract class AbstractConnectionRapidButtonBehavior<HOST extends XNode, MODEL, KEY extends DomainObjectDescriptor> extends RapidButtonBehavior<HOST> {
	
	Set<KEY> availableChoiceKeys = newLinkedHashSet	
	Set<KEY> unavailableChoiceKeys = newHashSet
	
	new(HOST host) {
		super(host)
	}
	
	override getBehaviorKey() {
		class
	}
	
	override protected doActivate() {
		super.doActivate
		availableChoiceKeys += initialModelChoices.map[choiceKey]
		if(!availableChoiceKeys.empty) {
			val RapidButtonAction addConnectionAction = new RapidButtonAction() {
				override perform(RapidButton button) {
					val chooser = createChooser(button, availableChoiceKeys, unavailableChoiceKeys)
					host.root.currentTool = chooser
				}
				
				override isEnabled(XNode host) {
					availableChoiceKeys.empty
				}
			}
			createButtons(addConnectionAction).forEach[add]
			host.diagram.connections.addInitializingListener(new InitializingListListener() => [
				add = [ XConnection it |
					if(availableChoiceKeys.remove(domainObject)) {
						unavailableChoiceKeys.add(domainObject as KEY)
					}
				]
				remove = [ XConnection it |
					if(unavailableChoiceKeys.remove(domainObject)) {
						availableChoiceKeys.add(domainObject as KEY)
					} 
				]
			])
		}
	}	
	
	protected def Iterable<MODEL> getInitialModelChoices() 
	
	protected def KEY getChoiceKey(MODEL model)
	
	protected def XNode createNode(KEY key)

	protected def Iterable<RapidButton> createButtons(RapidButtonAction addConnectionAction) 
		
	protected def AbstractChooser createChooser(RapidButton button, Set<KEY> availableChoiceKeys, Set<KEY> unavailableChoiceKeys) 
	
}
