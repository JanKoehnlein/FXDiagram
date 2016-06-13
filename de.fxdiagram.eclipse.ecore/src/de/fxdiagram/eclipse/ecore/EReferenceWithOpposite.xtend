package de.fxdiagram.eclipse.ecore

import org.eclipse.emf.ecore.EReference
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtend.lib.annotations.Accessors

@FinalFieldsConstructor
@Accessors(PUBLIC_GETTER)
class EReferenceWithOpposite {
	val EReference to
	val EReference fro
	
	new(EReference to) {
		this.to = to
		this.fro = to.EOpposite
	}
	
	override equals(Object obj) {
		if(obj instanceof EReferenceWithOpposite) 
			(obj.to == to && obj.fro == fro)
			|| (obj.fro == to && obj.to == fro)  
		else
			false
	}
	
}