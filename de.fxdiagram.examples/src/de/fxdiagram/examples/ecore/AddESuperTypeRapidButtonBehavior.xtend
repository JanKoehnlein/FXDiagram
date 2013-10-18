package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.List
import java.util.Set
import javafx.collections.ListChangeListener
import org.eclipse.emf.ecore.EClass

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddESuperTypeRapidButtonBehavior extends AbstractHostBehavior<EClassNode> {
	
	List<XRapidButton> buttons
	
	Set<ESuperTypeKey> availableKeys = newLinkedHashSet
	Set<ESuperTypeKey> unavailableKeys = newHashSet
	
	new(EClassNode host) {
		super(host)
	}
	
	override getBehaviorKey() {
		AddESuperTypeRapidButtonBehavior
	}
	
	override protected doActivate() {
		availableKeys += host.EClass.ESuperTypes.map[key]
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
								unavailableKeys.add(key as ESuperTypeKey)
						]
					if(change.wasRemoved) 
						change.removed.forEach[
							if(unavailableKeys.remove(key))
								availableKeys.add(key as ESuperTypeKey)
						]
				}
				if(availableKeys.empty)
					host.diagram.buttons -= buttons
			]  			
		}
	}	
	
	protected def getKey(EClass superType) {
		new ESuperTypeKey((host as EClassNode).EClass, superType)
	}
	
	protected def createButtons((XRapidButton)=>void addSuperTypeAction) {
		#[
			new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, 'Discover supertypes'), addSuperTypeAction),
			new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, 'Discover supertypes'), addSuperTypeAction)
		]
	}
	
	protected def createChooser(XRapidButton button) {
		val chooser = new CoverFlowChooser(host, button.getChooserPosition)
		availableKeys.forEach[
			chooser.addChoice(new EClassNode(it.superType), it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			new XConnection(host, choice, getKey((choice as EClassNode).EClass)) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					it.strokeProperty, host.diagram.backgroundPaintProperty, false)
			]
		]
		host.root.currentTool = chooser
	}
}

@Data
class ESuperTypeKey {
	EClass subType
	EClass superType
}