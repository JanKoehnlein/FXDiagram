package de.fxdiagram.core.services

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProviderWithState

import static extension de.fxdiagram.core.extensions.UriExtensions.*

@ModelNode
class ResourceProvider implements DomainObjectProviderWithState {

	BiMap<String, ClassLoader> classLoaderMap = HashBiMap.create
	
	ClassLoader defaultClassLoader	

	new(ClassLoader defaultClassLoader) {
		this.defaultClassLoader = defaultClassLoader
	}

	def addClassLoader(String id, ClassLoader classLoader) {
		classLoaderMap.put(id, classLoader)
	}

	protected def getClassLoader(String id) {
		if(id == null)
			defaultClassLoader
		else 
			classLoaderMap.get(id)
	}

	protected def getClassLoaderId(ClassLoader classLoader) {
		classLoaderMap.inverse.get(classLoader)
	}

	protected def ResourceHandle resolveResourceHandle(ResourceDescriptor description) {
		new ResourceHandle(description.name, loadClass(description), description.relativePath)
	}
	
	protected def loadClass(ResourceDescriptor description) {
		getClassLoader(description.classLoaderId).loadClass(description.className)
	}

	override createDescriptor(Object domainObject) {
		switch domainObject {
			ResourceHandle: 
				createResourceDescriptor(domainObject)
			default: throw new IllegalArgumentException('Cannot handle ' + domainObject) 
		}
	}
	
	def createResourceDescriptor(ResourceHandle object) {
		new ResourceDescriptor(object.context.classLoader.classLoaderId, 
			object.context.canonicalName, 
			object.relativePath,
			object.name, 
			this)
	}
	
	override copyState(DomainObjectProviderWithState other) {
		if(other instanceof ResourceProvider) {
			defaultClassLoader = other.defaultClassLoader
			classLoaderMap.putAll(other.classLoaderMap)		
		}
	}
}

@ModelNode(#['name', 'classLoaderId', 'className', 'relativePath', 'provider'])
class ResourceDescriptor implements DomainObjectDescriptor {

	@FxProperty String classLoaderId

	@FxProperty String className

	@FxProperty String relativePath
	
	@FxProperty String name
	
	@FxProperty ResourceProvider provider

	new(String classLoaderId, String className, String relativePath, String name, ResourceProvider provider) {
		this.relativePath = relativePath
		this.classLoaderId = classLoaderId
		this.className = className
		this.name = name
		this.provider = provider
	}

	override getId() {
		classLoaderId + ':' + className + ':' + relativePath
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
