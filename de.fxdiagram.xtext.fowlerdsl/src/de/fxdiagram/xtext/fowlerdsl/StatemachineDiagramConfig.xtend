package de.fxdiagram.xtext.fowlerdsl

import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig
import de.fxdiagram.mapping.ConnectionLabelMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.MappingAcceptor
import de.fxdiagram.mapping.NodeLabelMapping
import de.fxdiagram.mapping.NodeMapping
import org.eclipse.xtext.example.fowlerdsl.statemachine.Event
import org.eclipse.xtext.example.fowlerdsl.statemachine.State
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*
import static extension org.eclipse.xtext.EcoreUtil2.*

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
class StatemachineDiagramConfig extends AbstractXtextDiagramConfig {
	 
	val statemachineDiagram = new DiagramMapping<Statemachine>(this, 'statemachineDiagram', 'Statemachine') {
		override calls() {
			// when adding a statemachine diagram automatically add a node for each state
			// and a connetion for each transition
			stateNode.nodeForEach[states]
			transitionConnection.connectionForEach[states.map[transitions].flatten]
		}		
	}
	
	val stateNode = new NodeMapping<State>(this, 'stateNode', 'State') {
		override protected calls() {
			stateLabel.labelFor[it]
			// when adding a state allow to explore its transitions via rapid button
			transitionConnection.outConnectionForEach[transitions].asButton[
				getArrowButton('Add transition')
			]
		}
	}
	
	val stateLabel = new NodeLabelMapping<State>(this, 'stateLabel', '') {
		override getText(State it) {
			name
		}
	}
	
	val transitionConnection = new ConnectionMapping<Transition>(this, 'transitionConnection', 'Transition') {
		override protected calls() {
			eventLabel.labelFor[event]
			// when adding a transition, automatically add its source and target
			stateNode.source[eContainer as State]
			stateNode.target[state]
		}
	}

	val eventLabel = new ConnectionLabelMapping<Event>(this, 'eventLabel', '') {
		override getText(Event it) {
			name
		}
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			Statemachine: 
				add(statemachineDiagram)
			State: {
				add(statemachineDiagram, [domainArgument.getContainerOfType(Statemachine)])
				add(stateNode, [domainArgument])
			}
			Transition: {
				add(statemachineDiagram, [domainArgument.getContainerOfType(Statemachine)])	
				add(transitionConnection, [domainArgument])
			}
		}
	}	
}