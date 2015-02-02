package de.fxdiagram.core.extensions

import java.net.URL
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import org.eclipse.core.runtime.FileLocator
import org.eclipse.core.runtime.Platform
import org.osgi.framework.BundleReference

class ClassLoaderExtensions {
	def static toURI(Object context, String file) {
		val resource =  switch context {
				Class<?>: context
				default: context.class
		}.getResource(file)
		toURI(resource)
	}
	
	protected def static String toURI(URL resource) {
		if(isEquinox)
			FileLocator.toFileURL(resource).toExternalForm
		else
			resource.toExternalForm
	}
	
	def static fxmlNode(Object context, String file) {
		FXMLLoader.<Node>load(new URL(toURI(context, file)))
	}
	
	def static String serialize(Class<?> clazz) {
		if(equinox) {
			val classLoader = clazz.classLoader
			if(classLoader instanceof BundleReference) 
				return classLoader.bundle.symbolicName + ':' + clazz.canonicalName
		}
		return clazz.canonicalName
	}
	
	def static deserialize(String classLoaderClass) {
		val split = classLoaderClass.split(':')
		val bundleName = if(split.size == 2) split.get(0) else null 
		val className = if(split.size == 2) split.get(1) else classLoaderClass 
		if(bundleName != null && equinox) {
			val bundle = Platform.getBundle(bundleName)
			if(bundle != null)
				return bundle.loadClass(className)
		} else {
			return ClassLoaderExtensions.classLoader.loadClass(className)
		}	
	}
	
	def static isEquinox() {
		Platform.isRunning()
	}
}