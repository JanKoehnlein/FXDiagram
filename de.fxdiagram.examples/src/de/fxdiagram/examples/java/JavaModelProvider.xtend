package de.fxdiagram.examples.java

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectDescriptorImpl
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
	
	override resolveDomainObject(DomainObjectDescriptor descriptor) {
		switch descriptor {
			JavaTypeDescriptor: return Class.forName(descriptor.id)
			JavaPropertyDescriptor: {
				val split = descriptor.id.split(' ')
				new JavaProperty(split.get(1), Class.forName(split.get(0)))
			}
			JavaSuperTypeDescriptor: {
				val split = descriptor.id.split('->')
				new JavaSuperTypeHandle(Class.forName(split.get(0)), Class.forName(split.get(1)))
			}
		}
	}
}

@ModelNode(#['id', 'name', 'provider'])
class JavaTypeDescriptor extends DomainObjectDescriptorImpl<Class<?>> {
	
	new(Class<?> javaClass, JavaModelProvider provider) {
		super(javaClass.canonicalName, javaClass.canonicalName, provider)
	}
}

@ModelNode(#['id', 'name', 'provider'])
class JavaPropertyDescriptor extends DomainObjectDescriptorImpl<JavaProperty> {
	
	new(JavaProperty it, JavaModelProvider provider) {
		super(type.canonicalName + ' ' + name, type.canonicalName + ' ' + name, provider)
	}
}

@ModelNode(#['id', 'name', 'provider'])
class JavaSuperTypeDescriptor extends DomainObjectDescriptorImpl<JavaSuperTypeHandle> {

	new(JavaSuperTypeHandle it, JavaModelProvider provider) {
		super(subType.canonicalName + '->' + superType.canonicalName,
			subType.canonicalName + '->' + superType.canonicalName,
			provider
		)
	}
}

