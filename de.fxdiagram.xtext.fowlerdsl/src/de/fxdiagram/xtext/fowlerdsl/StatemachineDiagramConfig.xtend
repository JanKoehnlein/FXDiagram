package de.fxdiagram.xtext.fowlerdsl

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectDescriptor
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
		override createConnection(XtextDomainObjectDescriptor<Transition> descriptor) {
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