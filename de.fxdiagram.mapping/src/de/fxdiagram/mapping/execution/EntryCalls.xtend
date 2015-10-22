package de.fxdiagram.mapping.execution

import de.fxdiagram.mapping.DiagramMappingCall
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.NodeMappingCall
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.ConnectionMappingCall
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.XDiagramConfig
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtend.lib.annotations.Accessors

interface EntryCall<ARG> {
	
	def XDiagramConfig getConfig()
	
	def void execute(ARG domainObject, XDiagramConfigInterpreter interpreter, InterpreterContext context) 
	
	def String getText()
}

@FinalFieldsConstructor
@Accessors(PUBLIC_GETTER)
abstract class AbstractEntryCall<ARG> implements EntryCall<ARG> {
	val XDiagramConfig config
	val String text
}

class NodeEntryCall<RESULT, ARG> implements EntryCall<ARG> {
	
	@Accessors(PUBLIC_GETTER)
	NodeMappingCall<RESULT, ARG> mappingCall
	
	new((ARG)=>RESULT selector, NodeMapping<RESULT> mapping) {
		mappingCall = new NodeMappingCall(selector, mapping)
	}
	
	override getConfig() {
		mappingCall.mapping.config
	}
	
	override getText() {
		mappingCall.mapping.displayName + ' (' + mappingCall.mapping.config.label + ')'
	}
	
	override execute(ARG domainObject, XDiagramConfigInterpreter interpreter, InterpreterContext context) {
		interpreter.execute(mappingCall, domainObject, context)
	}
}

class DiagramEntryCall<RESULT, ARG> implements EntryCall<ARG> {
	
	@Accessors(PUBLIC_GETTER)
	DiagramMappingCall<RESULT, ARG> mappingCall
	
	new((ARG)=>RESULT selector, DiagramMapping<RESULT> mapping) {
		mappingCall = new DiagramMappingCall(selector, mapping)
	}
	
	override getConfig() {
		mappingCall.mapping.config
	}
	
	override getText() {
		mappingCall.mapping.displayName + ' (' + mappingCall.mapping.config.label + ')'
	}
	
	override execute(ARG domainObject, XDiagramConfigInterpreter interpreter, InterpreterContext context) {
		interpreter.execute(mappingCall, domainObject, context)
	}
}

class ConnectionEntryCall<RESULT, ARG> implements EntryCall<ARG> {
	
	@Accessors(PUBLIC_GETTER)
	ConnectionMappingCall<RESULT, ARG> mappingCall
	
	ConnectionMapping<RESULT> mapping
	
	new((ARG)=>RESULT selector, ConnectionMapping<RESULT> mapping) {
		mappingCall = new ConnectionMappingCall(selector, mapping)
		this.mapping = mapping
	}
	
	override getConfig() {
		mappingCall.mapping.config
	}
	
	override getText() {
		mappingCall.mapping.displayName + ' (' + mappingCall.mapping.config.label + ')'
	}
	
	override execute(ARG domainObject, XDiagramConfigInterpreter interpreter, InterpreterContext context) {
		if (mapping.source != null && mapping.target != null) {
			interpreter.execute(mappingCall, domainObject, [], context)
		}
	}
}