package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.ui.refactoring.participant.JvmElementFinder
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.ui.resource.IResourceSetProvider
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices

import static extension org.eclipse.xtext.EcoreUtil2.*

class JvmDomainUtil {

	@Inject extension Primitives

	@Inject CommonTypeComputationServices services
	
	@Inject extension JvmElementFinder
	
	@Inject IResourceSetProvider resourceSetProvider

	def getAttributes(JvmDeclaredType owner) {
		owner.declaredFields.filter[isAttributeType]
	}

	def getReferences(JvmDeclaredType owner) {
		owner.declaredFields.filter[!isAttributeType]
	}

	def isAttributeType(JvmField it) {
		val componentType = type.componentType
		componentType.isPrimitive || componentType.qualifiedName == 'java.lang.String'
	}

	def getComponentType(JvmTypeReference it) {
		val type = new StandardTypeReferenceOwner(services, it).toLightweightTypeReference(it)
		val componentType = if (type.isArray)
				type.componentType
			else if (type.isSubtypeOf(Iterable) && !type.typeArguments.empty)
				type.typeArguments.head.invariantBoundSubstitute
			else
				type
		return componentType.toJavaCompliantTypeReference
	}
	
	def getJvmType(IJavaElement javaElement) {
		val project = javaElement.javaProject.project
		val resourceSet = resourceSetProvider.get(project)
		val jvmElement = javaElement.getCorrespondingJvmElement(resourceSet)
		val jvmType = jvmElement.getContainerOfType(JvmDeclaredType)
		return getOriginalJvmType(jvmType)
	}
	
	def getOriginalJvmType(JvmDeclaredType jvmType) {
		val resourceSet = jvmType.eResource.resourceSet
		val indexedJvmType = jvmType.findJvmElementDeclarationInIndex(null, resourceSet)
		return if(indexedJvmType instanceof JvmDeclaredType)
				indexedJvmType 
			else 
				jvmType
	}
}
