package de.fxdiagram.xtext.glue.behavior

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRapidButtonAction
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.tools.AbstractChooser
import de.fxdiagram.lib.tools.CarusselChooser
import de.fxdiagram.lib.tools.CoverFlowChooser
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter
import java.util.List
import javafx.geometry.VPos

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class LazyConnectionMappingBehavior<MODEL, ARG> extends AbstractHostBehavior<XNode> {
	
	List<XRapidButton> buttons = newArrayList
	
	String tooltip
	
	XRapidButtonAction action

	new(XNode host, AbstractConnectionMappingCall<MODEL, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		super(host)
		this.tooltip = 'Add ' + mappingCall.role
		this.action = createAction(mappingCall, configInterpreter, hostIsSource)
	}
	
	override getBehaviorKey() {
		class
	}
	
	override protected doActivate() {
		buttons += createButtons
		host.diagram.buttons += buttons
	}	

	protected def XRapidButtonAction createAction(AbstractConnectionMappingCall<MODEL, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		new LazyConnectionRapidButtonAction(mappingCall, configInterpreter, hostIsSource)
	}
	
	protected def Iterable<XRapidButton> createButtons() {
		#[	
			new XRapidButton(host, 0, 0.5, getTriangleButton(LEFT, tooltip), action),
			new XRapidButton(host, 1, 0.5, getTriangleButton(RIGHT, tooltip), action),
			new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, tooltip), action),
			new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, tooltip), action) 
		]
	}
}

class LazyConnectionRapidButtonAction<MODEL, ARG> extends XRapidButtonAction {
	
	XDiagramConfigInterpreter configInterpreter

	AbstractConnectionMappingCall<MODEL, ARG> mappingCall

	boolean hostIsSource

	new(AbstractConnectionMappingCall<MODEL, ARG> mappingCall, XDiagramConfigInterpreter configInterpreter, boolean hostIsSource) {
		this.mappingCall = mappingCall
		this.configInterpreter = configInterpreter
		this.hostIsSource = hostIsSource
	}
	
	override isEnabled(XRapidButton button) {
		val hostDescriptor = button.host.domainObject as XtextDomainObjectDescriptor<ARG>
		val existingConnectionDescriptors = button.host.diagram.connections.map[domainObject].toSet
		hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = configInterpreter.select(mappingCall, domainArgument)
			for(connectionDomainObject: connectionDomainObjects) {
				val connectionDescriptor = configInterpreter.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = configInterpreter.select(nodeMappingCall, connectionDomainObject)
					return !nodeDomainObjects.empty
				}
			}
			return false 
		]
	}
	
	override perform(XRapidButton button) {
		val chooser = createChooser(button)
		chooser.populateChooser(button.host)
		button.host.root.currentTool = chooser
	}
	
	def protected createChooser(XRapidButton button) {
		val position = button.chooserPosition
		val chooser = if(position.vpos == VPos.CENTER) {
			new CarusselChooser(button.host, position)
		} else {
			new CoverFlowChooser(button.host, position)			
		}
		chooser
	}
	
	protected def populateChooser(AbstractChooser chooser, XNode host) {
		val hostDescriptor = host.domainObject as XtextDomainObjectDescriptor<ARG>
		val existingConnectionDescriptors = host.diagram.connections.map[domainObject].toSet
		hostDescriptor.withDomainObject[ 
			domainArgument |
			val connectionDomainObjects = configInterpreter.select(mappingCall, domainArgument)
			connectionDomainObjects.forEach [ connectionDomainObject |
				val connectionDescriptor = configInterpreter.getDescriptor(connectionDomainObject, mappingCall.connectionMapping)
				if(existingConnectionDescriptors.add(connectionDescriptor)) {
					val nodeMappingCall = (mappingCall.connectionMapping.source ?: mappingCall.connectionMapping.target)
					val nodeDomainObjects = configInterpreter.select(nodeMappingCall, connectionDomainObject)
					nodeDomainObjects.forEach [	
						chooser.addChoice(createNode(nodeMappingCall.nodeMapping), connectionDescriptor)
					]
				}
			]
			chooser.connectionProvider = [ thisNode, thatNode, connectionDesc |
				val descriptor = connectionDesc as XtextDomainObjectDescriptor<MODEL>
				mappingCall.connectionMapping.createConnection(descriptor) => [
					if(hostIsSource) {
						source = thisNode
						target = thatNode
					} else {
						source = thatNode
						target = thisNode
					}
				]
			]	
			null
		]
	}
	
	protected def <NODE> createNode(Object nodeDomainObject, NodeMapping<?> nodeMapping) {
		if (nodeMapping.isApplicable(nodeDomainObject)) {
			val nodeMappingCasted = nodeMapping as NodeMapping<NODE>
			val descriptor = configInterpreter.getDescriptor(nodeDomainObject as NODE, nodeMappingCasted)
			val node = nodeMappingCasted.createNode(descriptor) 
			node
		} else { 
			null
		}
	}
}