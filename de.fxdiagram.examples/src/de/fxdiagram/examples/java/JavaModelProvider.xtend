package de.fxdiagram.examples.java

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.CachedDomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider

@ModelNode
class JavaModelProvider implements DomainObjectProvider {
	
	override createDescriptor(Object domainObject) {
		switch domainObject {
			Class<?>: return createJavaTypeDescriptor(domainObject)
			JavaProperty: return createJavaPropertyDescriptor(domainObject)
			JavaSuperTypeHandle: return createJavaSuperClassDescriptor(domainObject)
		}
		return null;
	}
	
	def createJavaSuperClassDescriptor(JavaSuperTypeHandle javaSuperType) {
		return new JavaSuperTypeDescriptor(javaSuperType, this)
	}
	
	def createJavaPropertyDescriptor(JavaProperty property) {
		return new JavaPropertyDescriptor(property, this)
	}
	
	def createJavaTypeDescriptor(Class<?> clazz) {
		return new JavaTypeDescriptor(clazz, this)
	}
}

@ModelNode
class JavaTypeDescriptor extends CachedDomainObjectDescriptor<Class<?>> {
	
	new(Class<?> javaClass, JavaModelProvider provider) {
		super(javaClass, javaClass.canonicalName, javaClass.canonicalName, provider)
	}
	
	override resolveDomainObject() {
		Class.forName(id)
	}
}

@ModelNode
class JavaPropertyDescriptor extends CachedDomainObjectDescriptor<JavaProperty> {
	
	
	new(JavaProperty it, JavaModelProvider provider) {
		super(it, type.canonicalName + ' ' + name, type.canonicalName + ' ' + name, provider)
	}

	override resolveDomainObject() {
		val split = id.split(' ')
		new JavaProperty(split.get(1), Class.forName(split.get(0)))
	}
}

@ModelNode
class JavaSuperTypeDescriptor extends CachedDomainObjectDescriptor<JavaSuperTypeHandle> {

	new(JavaSuperTypeHandle it, JavaModelProvider provider) {
		super(it, subType.canonicalName + '->' + superType.canonicalName,
			subType.canonicalName + '->' + superType.canonicalName,
			provider
		)
	}
	
	override resolveDomainObject() {
		val split = id.split('->')
		new JavaSuperTypeHandle(Class.forName(split.get(0)), Class.forName(split.get(1)))
	}
	
}

