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

class StatemachineDiagramConfig extends AbstractDiagramConfig {
	 
	val statemachineDiagram = new DiagramMapping<Statemachine>(this, "statemachineDiagram") {
		override calls() {
			stateNode.nodeForEach[states]
		}		
	}
	
	val stateNode = new NodeMapping<State>(this, "stateNode") {
		override calls() {
			transitionConnection.outConnectionForEach[ transitions ]
		}
	}
	
	val transitionConnection = new ConnectionMapping<Transition>(this, "transitionConnection") {
		override createConnection(IMappedElementDescriptor<Transition> descriptor) {
			new XConnection(descriptor) => [
				new XConnectionLabel(it) => [ label |
					label.text.text = descriptor.withDomainObject[event.name]
				]		
			]
		}
		
		override calls() {
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