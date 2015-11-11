package de.fxdiagram.eclipse.ecore

import org.eclipse.emf.ecore.EClass
import org.eclipse.xtend.lib.annotations.Data

@Data
class ESuperType {
	EClass subType
	EClass superType
}