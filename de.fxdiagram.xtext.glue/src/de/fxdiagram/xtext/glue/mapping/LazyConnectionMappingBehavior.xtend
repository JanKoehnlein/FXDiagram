package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.tools.AbstractChooser
import de.fxdiagram.lib.tools.CarusselChooser
import de.fxdiagram.lib.tools.CoverFlowChooser
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import java.util.List
import javafx.geometry.VPos

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class LazyConnectionMappingBehavior<MODEL, ARG> extends AbstractHostBehavior<XNode> {
	
	XDiagramProvider diagramProvider

	AbstractConnectionMappingCall<MODEL, ARG> mappingCall
	
	List<XRapidButton> buttons = newArrayList
	
	String tooltip
	
	boolean hostIsSource

	new(XNode host, AbstractConnectionMappingCall<MODEL, ARG> mappingCall, XDiagramProvider diagramProvider, String tooltip, boolean hostIsSource) {
		super(host)
		this.mappingCall = mappingCall
		this.diagramProvider = diagramProvider
		this.tooltip = tooltip
		this.hostIsSource = hostIsSource
	}
	
	new(XNode host) {
		super(host)
	}
	
	override getBehaviorKey() {
		class
	}
	
	override protected doActivate() {
		val activateChooserAction = [
			XRapidButton button |
			val chooser = createChooser(button)
			chooser.populateChooser
			host.root.currentTool = chooser
		]
		buttons += createButtons(activateChooserAction)
		host.diagram.buttons += buttons
	}	
	
	protected def Iterable<XRapidButton> createButtons((XRapidButton)=>void activateChooserAction) {
		#[	
			new XRapidButton(host, 0, 0.5, getTriangleButton(LEFT, tooltip), activateChooserAction),
			new XRapidButton(host, 1, 0.5, getTriangleButton(RIGHT, tooltip), activateChooserAction),
			new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, tooltip), activateChooserAction),
			new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, tooltip), activateChooserAction) 
		]
	}
		
	def protected createChooser(XRapidButton button) {
		val position = button.chooserPosition
		val chooser = if(position.vpos == VPos.CENTER) {
			new CarusselChooser(host, position)
		} else {
			new CoverFlowChooser(host, position)			
		}
		chooser
	}
	
	protected def populateChooser(AbstractChooser chooser) {
		val hostDescriptor = host.domainObject as XtextDomainObjectDescriptor<ARG>
		val existingConnectionDescriptors = host.diagram.connections.map[domainObject].toSet
		hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = diagramProvider.select(mappingCall, domainArgument)
			connectionDomainObjects.forEach [ connectionDomainObject |
				val connectionDescriptor = diagramProvider.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = diagramProvider.select(nodeMappingCall, connectionDomainObject)
					nodeDomainObjects.forEach [	
						chooser.addChoice(createNode(nodeMappingCall.nodeMapping), connectionDescriptor)
					]
				}
			]
			chooser.connectionProvider = [ host, other, connectionDesc |
				val descriptor = connectionDesc as XtextDomainObjectDescriptor<MODEL>
				mappingCall.connectionMapping.createConnection.apply(descriptor) => [
					onMouseClicked = [
						if (clickCount == 2)
							descriptor.revealInEditor
					]
					if(hostIsSource) {
						source = host
						target = other
					} else {
						source = other
						target = host
					}
				]
			]	
			null
		]
	}
	
	protected def <NODE> createNode(Object nodeDomainObject, NodeMapping<?> nodeMapping) {
		if (nodeMapping.isApplicable(nodeDomainObject)) {
			val nodeMappingCasted = nodeMapping as NodeMapping<NODE>
			val descriptor = diagramProvider.getDescriptor(nodeDomainObject as NODE, nodeMappingCasted)
			val node = nodeMappingCasted.createNode.apply(descriptor) => [
				onMouseClicked = [
					if (clickCount == 2)
						descriptor.revealInEditor
				]
			]
			nodeMappingCasted.outgoing.filter[lazy].forEach[
				node.addBehavior(new LazyConnectionMappingBehavior(node, it, diagramProvider, 'TODO', true))
			]
			nodeMappingCasted.incoming.filter[lazy].forEach[
				node.addBehavior(new LazyConnectionMappingBehavior(node, it, diagramProvider, 'TODO', false))
			]
			node
		} else { 
			null
		}
	}
}

