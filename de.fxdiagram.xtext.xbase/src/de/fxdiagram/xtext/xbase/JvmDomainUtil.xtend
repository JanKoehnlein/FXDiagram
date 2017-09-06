package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import java.util.Map
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmOperation
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.ui.refactoring.participant.JvmElementFinder
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.ui.resource.IResourceSetProvider
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices

import static extension org.eclipse.xtext.EcoreUtil2.*
import org.eclipse.xtext.common.types.util.jdt.JavaElementFinder
import org.eclipse.xtext.common.types.JvmIdentifiableElement

class JvmDomainUtil {

	@Inject extension Primitives

	@Inject CommonTypeComputationServices services
	
	@Inject extension JvmElementFinder

	@Inject extension JavaElementFinder
	
	@Inject IResourceSetProvider resourceSetProvider

	def getAttributes(JvmDeclaredType owner) {
		owner.declaredFields.filter[isAttributeType]
	}

	def getMethods(JvmDeclaredType owner) {
		val fields = owner.declaredFields.toMap[simpleName]
		owner.declaredOperations.filter [
			!isSetter(fields) && !isGetter(fields)
		]
	}
	
	def getSignature(JvmField it) {
		'''«visibility.toString.toLowerCase» «
			IF static»static «ENDIF»«
			IF final»final «ENDIF»«
			IF volatile»volatile «ENDIF»«
			type.qualifiedName» «simpleName»'''
		.toString		
	}
		
	protected def isSetter(JvmOperation it, Map<String, JvmField> fields) {
		if(simpleName.startsWith('set') && parameters.size == 1) {
			val field = fields.get(simpleName.substring(3).toFirstLower)
			val fieldType = field?.type?.lightweight
			if(fieldType !== null) 
				return fieldType.isAssignableFrom(parameters.head.parameterType.lightweight)
		}
		return false
	}

	protected def isGetter(JvmOperation it, Map<String, JvmField> fields) {
		if(parameters.size == 0 && returnType !== null) {
			val field = 
				if(simpleName.startsWith('get'))
					fields.get(simpleName.substring(3).toFirstLower)
				else if(simpleName.startsWith('is')) 
					fields.get(simpleName.substring(2).toFirstLower)
				else return false
			if(field !== null)
				return returnType.lightweight.isAssignableFrom(field?.type.lightweight)
		} 
		return false
	}

	def getReferences(JvmDeclaredType owner) {
		owner.declaredFields.filter[!isAttributeType]
	}

	def isAttributeType(JvmField it) {
		val componentType = type.componentType
		componentType.isPrimitive || componentType.qualifiedName == 'java.lang.String'
	}

	def getComponentType(JvmTypeReference it) {
		val type = lightweight
		val componentType = if (type.isArray)
				type.componentType
			else if (type.isSubtypeOf(Iterable) && !type.typeArguments.empty)
				type.typeArguments.head.invariantBoundSubstitute
			else
				type
		return componentType.toJavaCompliantTypeReference
	}
	
	protected def getLightweight(JvmTypeReference it) {
		return if(it === null)
				null 
			else 
		 		new StandardTypeReferenceOwner(services, it).toLightweightTypeReference(it)
	}
	
	def getJvmElement(IJavaElement javaElement) {
		val project = javaElement.javaProject.project
		val resourceSet = resourceSetProvider.get(project)
		val jvmElement = javaElement.getCorrespondingJvmElement(resourceSet) as JvmIdentifiableElement
		val jvmType = jvmElement.getContainerOfType(JvmDeclaredType)
		val originalType = getOriginalJvmType(jvmType)
		if(jvmElement != jvmType) {
			if(originalType != jvmType) 
				return null
			else 
				return jvmElement
		}
		return originalType
	}
	
	def getOriginalJvmType(JvmDeclaredType jvmType) {
		val resourceSet = jvmType.eResource.resourceSet
		val indexedJvmType = jvmType.findJvmElementDeclarationInIndex(null, resourceSet)
		return if(indexedJvmType instanceof JvmDeclaredType)
				indexedJvmType 
			else 
				jvmType
	}
	
	def getJavaElement(JvmIdentifiableElement type) {
		type.findElementFor
	}
}
