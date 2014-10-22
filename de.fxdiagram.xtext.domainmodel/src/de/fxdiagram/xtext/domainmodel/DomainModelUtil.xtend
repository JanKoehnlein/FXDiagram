package de.fxdiagram.xtext.domainmodel

import com.google.inject.Inject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices

class DomainModelUtil {
	
	@Inject extension IJvmModelAssociations
	
	@Inject CommonTypeComputationServices services
	 
	def getReferencedEntity(JvmTypeReference it) {
		val sourceType = it?.componentType?.type?.primarySourceElement
		if(sourceType instanceof Entity) 
			return sourceType
		else 
			null
	}
	
	def getComponentType(JvmTypeReference it) {
		val type = new StandardTypeReferenceOwner(services, it).toLightweightTypeReference(it)
		val componentType = if(type.isArray) 
				type.componentType
			else if(type.isSubtypeOf(Iterable) && !type.typeArguments.empty) 
				type.typeArguments.head.invariantBoundSubstitute
			else
				type
		return componentType.toJavaCompliantTypeReference
	}
	
}