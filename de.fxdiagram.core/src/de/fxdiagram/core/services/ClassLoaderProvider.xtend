package de.fxdiagram.core.services

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProviderWithState
import org.eclipse.core.runtime.FileLocator
import org.eclipse.core.runtime.Platform
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.Data
import org.osgi.framework.BundleReference

import static de.fxdiagram.core.extensions.ClassLoaderExtensions.*

@ModelNode
class ClassLoaderProvider implements DomainObjectProviderWithState {
	
	@Accessors ClassLoader rootClassLoader
	
	override <T> createDescriptor(T domainObject) {
		switch domainObject {
			Class<?>: {
				return new ClassLoaderDescriptor(getClassLoaderID(domainObject), this) 
			}
			ResourceHandle: {
				return createResourceDescriptor(domainObject.name, domainObject.context, domainObject.relativePath)
			}
		}
		return null
	}
	
	def createResourceDescriptor(String name, Class<?> context, String relativePath) {
		val absoulutePath = context.package.name.replace('.', '/') + '/' + relativePath
		new ResourceDescriptor(getClassLoaderID(context),
			absoulutePath,
			name,
			this)
	}
	
	protected def getClassLoaderID(Class<?> clazz) {
		if(equinox && rootClassLoader != null) {
			val classLoader = clazz.classLoader
			if(classLoader instanceof BundleReference) 
				return classLoader.bundle.symbolicName
		}
		return 'root'
	}
	
	def loadClass(String className, ClassLoaderDescriptor descriptor) {
		if(descriptor.classLoaderID == 'root' || !equinox) 
			rootClassLoader.loadClass(className)
		else 
			Platform.getBundle(descriptor.classLoaderID)?.loadClass(className)
	}
	
	def toURI(String resourcePath, ClassLoaderDescriptor descriptor) {
		val url = if(descriptor.classLoaderID == 'root' || !equinox) 
			rootClassLoader.getResource(resourcePath)
		else 
			FileLocator.toFileURL(Platform.getBundle(descriptor.classLoaderID)?.getResource(resourcePath))
		return url.toExternalForm
	}

	override copyState(DomainObjectProviderWithState from) {
		rootClassLoader = (from as ClassLoaderProvider).rootClassLoader
	}
	
}

@ModelNode('classLoaderID', 'provider')
class ClassLoaderDescriptor implements DomainObjectDescriptor {
	
	@FxProperty(readOnly=true) String classLoaderID
	@FxProperty(readOnly=true) ClassLoaderProvider provider
	
	new(String classLoaderID, ClassLoaderProvider provider) {
		classLoaderIDProperty.set(classLoaderID)
		providerProperty.set(provider)
	}

	override getName() {
		classLoaderID
	}

	def toURI(String resourcePath) {
		provider.toURI(resourcePath, this)
	}
	
	def loadClass(String className) {
		provider.loadClass(className,this)
	}
}

@ModelNode('name', 'absolutePath')
class ResourceDescriptor extends ClassLoaderDescriptor {

	@FxProperty(readOnly=true) String absolutePath
	
	@FxProperty(readOnly=true) String name
	
	new(String classLoaderID, String relativePath, String name, ClassLoaderProvider provider) {
		super(classLoaderID, provider)
		absolutePathProperty.set(relativePath)
		nameProperty.set(name)
	}

	def toURI() {
		super.toURI(absolutePath)
	}
}

@Data
class ResourceHandle {
	String name
	Class<?> context
	String relativePath
}
