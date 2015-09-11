package de.fxdiagram.lib.model

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.buttons.RapidButtonBehavior
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import java.util.Set

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * Commodity class to add rapid-button-based exploration behavior to your {@link XNodes}
 * by only overriding a couple of template methods.
 * 
 * See the examples for usage scenarios.
 */
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
					!availableChoiceKeys.empty
				}
			}
			createButtons(addConnectionAction).forEach[add]
			host.diagram.connections.addInitializingListener(new InitializingListListener() => [
				add = [ XConnection it |
					if(availableChoiceKeys.remove(domainObjectDescriptor)) {
						unavailableChoiceKeys.add(domainObjectDescriptor as KEY)
					}
				]
				remove = [ XConnection it |
					if(unavailableChoiceKeys.remove(domainObjectDescriptor)) {
						availableChoiceKeys.add(domainObjectDescriptor as KEY)
					} 
				]
			])
		}
	}	
	
	protected def Iterable<MODEL> getInitialModelChoices() 
	
	protected def KEY getChoiceKey(MODEL model)
	
	protected def XNode createNode(KEY key)

	protected def Iterable<RapidButton> createButtons(RapidButtonAction addConnectionAction) 
		
	protected def ConnectedNodeChooser createChooser(RapidButton button, Set<KEY> availableChoiceKeys, Set<KEY> unavailableChoiceKeys) 
	
}
