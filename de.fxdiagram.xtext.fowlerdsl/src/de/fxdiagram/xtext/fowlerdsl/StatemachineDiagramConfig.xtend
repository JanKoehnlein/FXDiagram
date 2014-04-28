package de.fxdiagram.xtext.fowlerdsl

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import org.eclipse.xtext.example.fowlerdsl.statemachine.State
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition

class StatemachineDiagramConfig extends AbstractDiagramConfig {
	 
	new() {
		val statemachineDiagram = new DiagramMapping(Statemachine)
		val stateNode = new NodeMapping(State)
		val transitionConnection = new ConnectionMapping(Transition) => [
			createConnection = [ descriptor |
				new XConnection(descriptor) => [
					new XConnectionLabel(it) => [ label |
						label.text.text = descriptor.withDomainObject[event.name]
					]		
				]
			]
		] 
		addMapping(statemachineDiagram => [
			nodeForEach(stateNode, [states])
		])
		addMapping(stateNode => [
			outConnectionForEach(transitionConnection, [transitions])
		])
		addMapping(transitionConnection => [
			target(stateNode, [state])
		])
	}
}