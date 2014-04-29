package de.fxdiagram.examples.ecore

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.CachedDomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.util.EcoreUtil

@ModelNode
class EcoreDomainObjectProvider implements DomainObjectProvider {
	
	override createDescriptor(Object domainObject) {
		switch domainObject {
			EClass: return createEClassDescriptor(domainObject)
			EReference: return createEReferenceDescriptor(domainObject)
			ESuperTypeHandle: return createESuperClassDescriptor(domainObject)
		}
		return null;
	}
	
	def createEClassDescriptor(EClass object) {
		new EClassDescriptor(object, this)
	}
	
	def createEReferenceDescriptor(EReference object) {
		new EReferenceDescriptor(object, this)
	}
	
	def createESuperClassDescriptor(ESuperTypeHandle object) {
		new ESuperTypeDescriptor(object, this)
	}

	def String getId(EObject it) {
		EcoreUtil.getURI(it).toString
	}
	
	def String getFqn(EClass it) {
		EPackage.name + '.' + name
	}

	def String getFqn(EReference it) {
		EContainingClass.fqn + '.' + name
	}
}

@ModelNode
class EClassDescriptor extends CachedDomainObjectDescriptor<EClass> {
	
	new(EClass eClass, extension EcoreDomainObjectProvider provider) {
		super(eClass, eClass.id, eClass.fqn, provider)
	}

	override resolveDomainObject() {
		val uri = URI.createURI(id)
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val posEquals = uri.fragment.indexOf('=')
		val fragment = if(posEquals == -1) uri.fragment else uri.fragment.substring(0, posEquals) 
		ePackage.eResource.getEObject(fragment) as EClass
	}
}

@ModelNode
class EReferenceDescriptor extends CachedDomainObjectDescriptor<EReference> {
	
	new(EReference eReference, extension EcoreDomainObjectProvider provider) {
		super(eReference, eReference.id, eReference.fqn, provider)
	}

	override resolveDomainObject() {
		val uri = URI.createURI(id)
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val posEquals = uri.fragment.indexOf('=')
		val fragment = if(posEquals == -1) uri.fragment else uri.fragment.substring(0, posEquals) 
		ePackage.eResource.getEObject(fragment) as EReference
	}
	
	override hashCode() {
		domainObject.hashCode + (domainObject.EOpposite ?: domainObject).hashCode 
	}
	
	override equals(Object other) {
		if(other instanceof EReferenceDescriptor) 
			return other.domainObject == this.domainObject || other.domainObject == this.domainObject.EOpposite
		else 
			return false;
	}
}

@ModelNode
class ESuperTypeDescriptor extends CachedDomainObjectDescriptor<ESuperTypeHandle> {
	
	new(ESuperTypeHandle it, extension EcoreDomainObjectProvider provider) {
		super(it, subType.id + '=' + subType.EAllSuperTypes.indexOf(superType),
			subType.id + '=' + subType.EAllSuperTypes.indexOf(superType),
			provider
		)
	}

	override resolveDomainObject() {
		val uri = URI.createURI(id)
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val posEquals = uri.fragment.indexOf('=')
		val fragment = if(posEquals == -1) uri.fragment else uri.fragment.substring(0, posEquals) 
		val eObject = ePackage.eResource.getEObject(fragment)
		val eClass = eObject as EClass
		new ESuperTypeHandle(eClass, eClass.EAllSuperTypes.get(Integer.parseInt(uri.fragment.substring(posEquals + 1))))
	}
}

@Data
class ESuperTypeHandle {
	EClass subType
	EClass superType
}
