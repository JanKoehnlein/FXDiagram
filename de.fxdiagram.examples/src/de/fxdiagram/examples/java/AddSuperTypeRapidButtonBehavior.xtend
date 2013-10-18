package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.List
import java.util.Set
import javafx.collections.ListChangeListener

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddSuperTypeRapidButtonBehavior extends AbstractHostBehavior<JavaTypeNode> {
	
	List<XRapidButton> buttons
	
	Set<SuperTypeKey> availableKeys = newLinkedHashSet
	Set<SuperTypeKey> unavailableKeys = newHashSet
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override getBehaviorKey() {
		AddSuperTypeRapidButtonBehavior
	}
	
	override protected doActivate() {
		availableKeys += host.javaTypeModel.superTypes.map[key]
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
								unavailableKeys.add(key as SuperTypeKey)
						]
					if(change.wasRemoved) 
						change.removed.forEach[
							if(unavailableKeys.remove(key))
								availableKeys.add(key as SuperTypeKey)
						]
				}
				if(availableKeys.empty)
					host.diagram.buttons -= buttons
			]  			
		}
	}
	
	protected def getKey(Class<?> superType) {
		new SuperTypeKey((host as JavaTypeNode).javaType, superType)
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
			chooser.addChoice(new JavaTypeNode(superType), it)
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			new XConnection(host, choice, getKey((choice as JavaTypeNode).javaType)) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					it.strokeProperty, host.diagram.backgroundPaintProperty, false)
			]
		]
		host.root.currentTool = chooser
	}
}

@Data
class SuperTypeKey {
	Class<?> subType
	Class<?> superType
}