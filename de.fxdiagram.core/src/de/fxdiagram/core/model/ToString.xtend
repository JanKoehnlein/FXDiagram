package de.fxdiagram.core.model

import java.util.Map

class ToString {
	static def String toString(XModelProvider modelProvider) {
		toString(modelProvider, newHashMap)
	}
	
	protected static def dispatch String toString(XModelProvider modelProvider, Map<XModelProvider, Integer> seen) {
		val number = seen.get(modelProvider)
		if(number == null) {
			val newNumber = seen.size
			seen.put(modelProvider, newNumber)
			val modelElement = new ModelElementImpl(null)
			modelProvider.populate(modelElement)
			'''
				#«newNumber» «modelProvider.class.simpleName» «modelElement.getPropertiesToString(seen)»
			'''
		} else {
			'''
				«modelProvider.class.simpleName» #«number»
			'''
		}
	}
	
	protected static def dispatch String toString(Object element, Map<XModelProvider, Integer> seen) {
		element.toString
	}
	
	protected static def getPropertiesToString(ModelElement it,  Map<XModelProvider, Integer> seen) {
		if(properties.empty && listProperties.empty)
			return ''
		else 
			return '''
				[
					«FOR lp: listProperties.filter[!isEmpty]»
						«lp.name»: [
							«FOR value: lp.value»
								«value.toString(seen)»
							«ENDFOR»
						]
					«ENDFOR»
					«FOR p: properties.filter[value != null]»
						«p.name»: «p.value.toString(seen)»
					«ENDFOR»
				]
			'''
	}
}