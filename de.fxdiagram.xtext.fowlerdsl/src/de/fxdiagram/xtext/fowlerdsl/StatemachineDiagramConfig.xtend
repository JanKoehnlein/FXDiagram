package de.fxdiagram.xtext.fowlerdsl

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.eclipse.mapping.AbstractDiagramConfig
import de.fxdiagram.eclipse.mapping.ConnectionMapping
import de.fxdiagram.eclipse.mapping.DiagramMapping
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor
import de.fxdiagram.eclipse.mapping.MappingAcceptor
import de.fxdiagram.eclipse.mapping.NodeMapping
import org.eclipse.xtext.example.fowlerdsl.statemachine.State
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition

/**
 * A very simple example how to create a diagram mapping for an the Fowler DSL shipped 
 * as an Xtext example language.
 * 
 * We implemented it as a fragment such that we can use the example plug-ins as they are, 
 * but usually you would just add the code to the UI plug-in of your language.
 *
 * The configuration contains three mappings:
 * <ol>
 * <li>A {@link Statemachine} is mapped to a diagram,</li>
 * <li>A {@link State} is mapped to a node, and</li>
 * <li>A {@link Transition} is mapped to a connection.</li>
 * </ol>
 *  
 * In addition to this class, you just have to apply two changes to the plugin/fragment.xml:
 * <ol>
 * <li>Register this configuration to the extension point 
 * <code>de.fxdiagram.eclipse.fxDiagramConfig</code>.</li>
 * <li>Add a handler for the commandId <code>de.fxdiagram.eclipse.showInDiagramCommand</code> 
 * that adds the selected element in the editor to the diagram.
 * </ol>
 */
class StatemachineDiagramConfig extends AbstractDiagramConfig {
	 
	val statemachineDiagram = new DiagramMapping<Statemachine>(this, "statemachineDiagram") {
		override calls() {
			// when adding a statemachine diagram automatically add a node for each state
			stateNode.nodeForEach[states]
		}		
	}
	
	val stateNode = new NodeMapping<State>(this, "stateNode") {
		override calls() {
			// when adding a state automatically add a connection for each transition
			transitionConnection.outConnectionForEach[ transitions ]
		}
	}
	
	val transitionConnection = new ConnectionMapping<Transition>(this, "transitionConnection") {
		override createConnection(IMappedElementDescriptor<Transition> descriptor) {
			// create a connection with a label denoting the transition's event name
			new XConnection(descriptor) => [
				new XConnectionLabel(it) => [ label |
					label.text.text = descriptor.withDomainObject[event.name]
				]		
			]
		}
		
		override calls() {
			// when adding a transition automatically add a node for the targe state
			stateNode.target [state]
		}
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			Statemachine: 
				add(statemachineDiagram)
			State:
				add(stateNode)
			Transition:
				add(transitionConnection)	
		}
	}	
}