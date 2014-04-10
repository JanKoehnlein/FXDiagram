package de.fxdiagram.xtext.fowlerdsl.ui

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.lib.simple.SimpleNode
import de.fxdiagram.xtext.glue.ShowInDiagramHandler
import de.fxdiagram.xtext.glue.mapping.BaseMapping
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import java.util.List
import org.eclipse.xtext.example.fowlerdsl.statemachine.State
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition

class StatemachineShowInDiagramHandler extends ShowInDiagramHandler {
	
	override protected createDiagramConfig() {
		return new StatemachineDiagramConfig
	}
}

class StatemachineDiagramConfig implements XDiagramConfig {
	
	List<BaseMapping<?>> mappings = newArrayList
	 
	new() {
		val statemachineDiagram = new DiagramMapping(Statemachine)
		val stateNode = new NodeMapping(State) => [
			createNode = [ new SimpleNode(it) ]
		]
		val transitionConnection = new ConnectionMapping(Transition) => [
			createConnection = [ descriptor |
				new XConnection(descriptor) => [
					new XConnectionLabel(it) => [ label |
						label.text.text = descriptor.withDomainObject[event.name]
					]		
				]
			]
		] 
		mappings += statemachineDiagram => [
			nodeForEach(stateNode, [states])
		] 
		mappings += stateNode => [
			outConnectionForEach(transitionConnection, [transitions]).makeLazy
		]
		mappings += transitionConnection => [
			target(stateNode, [state])
		]
	}
	
	override <T> getMappings(T domainObject) {
		mappings.filter[isApplicable(domainObject)].map[it as BaseMapping<T>].toList
	}
}