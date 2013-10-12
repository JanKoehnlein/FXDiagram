package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.List
import java.util.Map
import javafx.collections.ListChangeListener

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class AddSuperTypeRapidButtonBehavior extends AbstractBehavior<JavaTypeNode> {
	
	List<XRapidButton> buttons
	
	Map<Object, Class<?>> key2availableSuperType = newHashMap
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected doActivate() {
		val model = host.javaTypeModel
		model.superTypes.forEach[ key2availableSuperType.put(getKey(it), it) ]
		if(!key2availableSuperType.empty) {
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
						change.addedSubList.forEach[ key2availableSuperType.remove(key) ]
				}
				if(key2availableSuperType.empty)
					host.diagram.buttons -= buttons
			]  			
		}
	}
	
	protected def getKey(Class<?> superType) {
		(host as JavaTypeNode).javaType.simpleName + ' extends ' + superType.simpleName
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
			chooser.addChoice(new JavaTypeNode(superType))
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