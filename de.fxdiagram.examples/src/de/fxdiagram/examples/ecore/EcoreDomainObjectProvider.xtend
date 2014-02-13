package de.fxdiagram.examples.ecore

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectDescriptorImpl
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

	override resolveDomainObject(DomainObjectDescriptor descriptor) {
		val uri = URI.createURI(descriptor.id)
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val posEquals = uri.fragment.indexOf('=')
		val fragment = if(posEquals == -1) uri.fragment else uri.fragment.substring(0, posEquals) 
		val eObject = ePackage.eResource.getEObject(fragment)
		if(descriptor instanceof ESuperTypeHandle) {
			val eClass = eObject as EClass
			return new ESuperTypeHandle(eClass, eClass.EAllSuperTypes.get(Integer.parseInt(uri.fragment.substring(posEquals + 1))))
		} else {
			return eObject
		}
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

@ModelNode(#['id', 'name', 'provider'])
class EClassDescriptor extends DomainObjectDescriptorImpl<EClass> {
	
	new(EClass eClass, extension EcoreDomainObjectProvider provider) {
		super(eClass.id, eClass.fqn, provider)
	}
}

@ModelNode(#['id', 'name', 'provider'])
class EReferenceDescriptor extends DomainObjectDescriptorImpl<EReference> {
	
	new(EReference eReference, extension EcoreDomainObjectProvider provider) {
		super(eReference.id, eReference.fqn, provider)
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

@ModelNode(#['id', 'name', 'provider'])
class ESuperTypeDescriptor extends DomainObjectDescriptorImpl<ESuperTypeHandle> {
	
	new(ESuperTypeHandle it, extension EcoreDomainObjectProvider provider) {
		super(subType.id + '=' + subType.EAllSuperTypes.indexOf(superType),
			subType.id + '=' + subType.EAllSuperTypes.indexOf(superType),
			provider
		)
	}
}

@Data
class ESuperTypeHandle {
	EClass subType
	EClass superType
}
