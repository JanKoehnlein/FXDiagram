package de.fxdiagram.core.model

class ToString {
	static def String toString(XModelProvider modelProvider) {
		val modelElement = new ModelElementImpl(null)
		modelProvider.populate(modelElement)
		'''
			«modelProvider.class.simpleName» «modelElement.propertiesToString»
		'''
	}
	
	protected static def getPropertiesToString(ModelElement it) {
		if(properties.empty && listProperties.empty)
			return ''
		else 
			return '''
				[
					«FOR lp: listProperties.filter[!isEmpty]»
						«lp.name»: [
							«FOR value: lp.value»
								«value.toString»
							«ENDFOR»
						]
					«ENDFOR»
					«FOR p: properties.filter[value != null]»
						«p.name»: «p.value.toString»
					«ENDFOR»
				]
			'''
	}
}