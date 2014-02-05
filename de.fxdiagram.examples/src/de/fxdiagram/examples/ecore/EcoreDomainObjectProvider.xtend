package de.fxdiagram.examples.ecore

import de.fxdiagram.core.model.DomainObjectHandle
import de.fxdiagram.core.model.DomainObjectHandleImpl
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.core.model.ModelElement
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.util.EcoreUtil

class EcoreDomainObjectProvider implements DomainObjectProvider {
	
	override createDomainObjectHandle(Object object) {
		switch object {
			EClass: return createEClassHandle(object)
			EReference: return createEReferenceHandle(object)
			ESuperType: return createESuperClassHandle(object)
		}
		return null;
	}
	
	def createEClassHandle(EClass object) {
		new EClassHandle(object, this)
	}
	
	def createEReferenceHandle(EReference object) {
		new EReferenceHandle(object, this)
	}
	
	def createESuperClassHandle(ESuperType object) {
		new ESuperTypeHandle(object, this)
	}

	override resolveDomainObject(DomainObjectHandle handle) {
		val uri = URI.createURI(handle.id)
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val posEquals = uri.fragment.indexOf('=')
		val fragment = if(posEquals == -1) uri.fragment else uri.fragment.substring(0, posEquals) 
		val eObject = ePackage.eResource.getEObject(fragment)
		if(handle instanceof ESuperTypeHandle) {
			val eClass = eObject as EClass
			return new ESuperType(eClass, eClass.EAllSuperTypes.get(Integer.parseInt(uri.fragment.substring(posEquals + 1))))
		} else {
			return eObject
		}
	}
	
	override populate(ModelElement element) {
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

class EClassHandle extends DomainObjectHandleImpl {
	
	new() {}
	
	new(EClass eClass, extension EcoreDomainObjectProvider provider) {
		super(eClass.id, eClass.fqn, provider)
	}
	
	override EClass getDomainObject() {
		super.domainObject as EClass
	}
}

class EReferenceHandle extends DomainObjectHandleImpl {
	
	new() {}
	
	new(EReference eReference, extension EcoreDomainObjectProvider provider) {
		super(eReference.id, eReference.fqn, provider)
	}
	
	override EReference getDomainObject() {
		super.domainObject as EReference
	}
	
	override hashCode() {
		domainObject.hashCode + (domainObject.EOpposite ?: domainObject).hashCode 
	}
	
	override equals(Object other) {
		if(other instanceof EReferenceHandle) 
			return other.domainObject == this.domainObject || other.domainObject == this.domainObject.EOpposite
		else 
			return false;
	}
}

class ESuperTypeHandle extends DomainObjectHandleImpl {
	
	new() {}
	
	new(ESuperType it, extension EcoreDomainObjectProvider provider) {
		super(subType.id + '=' + subType.EAllSuperTypes.indexOf(superType),
			subType.id + '=' + subType.EAllSuperTypes.indexOf(superType),
			provider
		)
	}
	
	override ESuperType getDomainObject() {
		super.domainObject as ESuperType
	}
	
}

@Data
class ESuperType {
	EClass subType
	EClass superType
}
