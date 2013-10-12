package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.List
import java.util.Map
import javafx.collections.ListChangeListener
import org.eclipse.emf.ecore.EClass

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddEReferenceRapidButtonBehavior extends AbstractBehavior<EClassNode> {
	
	List<XRapidButton> buttons
	
	Map<Object, EClass> key2availableSuperType = newHashMap
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected doActivate() {
		host.EClass.ESuperTypes.forEach[ key2availableSuperType.put(getKey(it), it) ]
		if(!key2availableSuperType.empty) {
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
						change.addedSubList.forEach[ key2availableSuperType.remove(key) ]
				}
				if(key2availableSuperType.empty)
					host.diagram.buttons -= buttons
			]  			
		}
	}
	
	protected def getKey(EClass superType) {
		(host as EClassNode).EClass.name + ' extends ' + superType.name
	}
	
	protected def createButtons((XRapidButton)=>void addSuperTypeAction) {
		#[
			new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, 'Discover supertypes'), addSuperTypeAction),
			new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, 'Discover supertypes'), addSuperTypeAction)
		]
	}
	
	protected def createChooser(XRapidButton button) {
		val chooser = new CoverFlowChooser(host, button.getChooserPosition)
		key2availableSuperType.values.forEach[
			superType | 
			chooser.addChoice(new EClassNode(superType))
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