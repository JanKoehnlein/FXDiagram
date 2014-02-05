package de.fxdiagram.examples.java

import de.fxdiagram.core.model.DomainObjectHandle
import de.fxdiagram.core.model.DomainObjectHandleImpl
import de.fxdiagram.core.model.DomainObjectProvider
import de.fxdiagram.core.model.ModelElement

class JavaModelProvider implements DomainObjectProvider {
	
	override createDomainObjectHandle(Object object) {
		switch object {
			Class<?>: return createJavaTypeHandle(object)
			JavaProperty: return createJavaPropertyHandle(object)
			JavaSuperType: return createJavaSuperClassHandle(object)
		}
		return null;
	}
	
	def createJavaSuperClassHandle(JavaSuperType javaSuperType) {
		return new JavaSuperTypeHandle(javaSuperType, this)
	}
	
	def createJavaPropertyHandle(JavaProperty property) {
		return new JavaPropertyHandle(property, this)
	}
	
	def createJavaTypeHandle(Class<?> clazz) {
		return new JavaTypeHandle(clazz, this)
	}
	
	override resolveDomainObject(DomainObjectHandle handle) {
		switch handle {
			JavaTypeHandle: return Class.forName(handle.id)
			JavaPropertyHandle: {
				val split = handle.id.split(' ')
				new JavaProperty(split.get(1), Class.forName(split.get(0)))
			}
			JavaSuperTypeHandle: {
				val split = handle.id.split('->')
				new JavaSuperType(Class.forName(split.get(0)), Class.forName(split.get(1)))
			}
		}
	}
	
	override populate(ModelElement element) {
	}
	
}

class JavaTypeHandle extends DomainObjectHandleImpl {
	
	new() {}
	
	new(Class<?> javaClass, JavaModelProvider provider) {
		super(javaClass.canonicalName, javaClass.canonicalName, provider)
	}
	
	override Class<?> getDomainObject() {
		super.domainObject as Class<?>
	}
}

class JavaPropertyHandle extends DomainObjectHandleImpl {
	
	new() {}
	
	new(JavaProperty it, JavaModelProvider provider) {
		super(type.canonicalName + ' ' + name, type.canonicalName + ' ' + name, provider)
	}
	
	override JavaProperty getDomainObject() {
		super.domainObject as JavaProperty
	}
}

class JavaSuperTypeHandle extends DomainObjectHandleImpl {

	new() {}
	
	new(JavaSuperType it, JavaModelProvider provider) {
		super(subType.canonicalName + '->' + superType.canonicalName,
			subType.canonicalName + '->' + superType.canonicalName,
			provider
		)
	}
	
	override JavaSuperType getDomainObject() {
		super.domainObject as JavaSuperType
	}
}

