package de.fxdiagram.core.services

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider

import static extension de.fxdiagram.core.extensions.ClassLoaderExtensions.*

@ModelNode
class ResourceProvider implements DomainObjectProvider {

	protected def ResourceHandle resolveResourceHandle(ResourceDescriptor description) {
		new ResourceHandle(description.name, description.classLoaderClassName.deserialize, description.relativePath)
	}
	
	override createDescriptor(Object domainObject) {
		switch domainObject {
			ResourceHandle: 
				createResourceDescriptor(domainObject)
			default: throw new IllegalArgumentException('Cannot handle ' + domainObject) 
		}
	}
	
	def createResourceDescriptor(ResourceHandle object) {
		new ResourceDescriptor(object.context.class.serialize,
			object.relativePath,
			object.name, 
			this)
	}
}	

@ModelNode(#['name', 'classLoaderClassName', 'relativePath', 'provider'])
class ResourceDescriptor implements DomainObjectDescriptor {

	@FxProperty String classLoaderClassName

	@FxProperty String relativePath
	
	@FxProperty String name
	
	@FxProperty ResourceProvider provider

	new(String classLoaderClassName, String relativePath, String name, ResourceProvider provider) {
		this.relativePath = relativePath
		this.classLoaderClassName = classLoaderClassName
		this.name = name
		this.provider = provider
	}

	override getId() {
		classLoaderClassName + '/' + relativePath
	}
	
	def toURI() {
		provider.resolveResourceHandle(this).context.toURI(relativePath)
	}
}

@Data
class ResourceHandle {
	String name
	Class<?> context
	String relativePath
}
