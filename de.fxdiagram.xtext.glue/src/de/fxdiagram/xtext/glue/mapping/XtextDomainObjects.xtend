package de.fxdiagram.xtext.glue.mapping

import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtend.lib.annotations.Data
import java.util.List

@Data
class ESetting<ECLASS extends EObject> {
	ECLASS owner	
	EReference reference
	int index
	
	def getTarget() {
		if(reference.many) 
			(owner.eGet(reference) as List<?>).get(index)
		else
			owner.eGet(reference)
	}
}

@Data
class MappedElement<ECLASS_OR_ESETTING> {
	ECLASS_OR_ESETTING element	
	AbstractMapping<ECLASS_OR_ESETTING> mapping
}

