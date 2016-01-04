package de.fxdiagram.eclipse.xtext

import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtend.lib.annotations.Data

/**
 * The value of an {@link EReference} in the context of an {@link EObject}.
 */
@Data
class ESetting<ECLASS extends EObject> {
	ECLASS owner	
	EReference reference
	int index
	
	def EObject getTarget() {
		(if(reference.many) 
			(owner.eGet(reference) as List<?>).get(index)
		else
			owner.eGet(reference)) as EObject
	}
}

