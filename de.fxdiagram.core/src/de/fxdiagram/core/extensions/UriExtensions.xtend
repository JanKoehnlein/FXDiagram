package de.fxdiagram.core.extensions

import java.net.URL
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import org.eclipse.core.runtime.FileLocator

class UriExtensions {
	def static toURI(Object context, String file) {
		val resource =  switch context {
				Class<?>: context
				default: context.class
		}.getResource(file)
		toURI(resource)
	}
	
	protected def static toURI(URL resource) {
		if(org.eclipse.core.internal.runtime.Activator.getDefault != null)
			FileLocator.toFileURL(resource).toExternalForm
		else
			resource.toExternalForm
	}
	
	def static toURI(ClassLoader classLoader, String path) {
		toURI(classLoader.getResource(path))
	}
	
	def static fxmlNode(Object context, String file) {
		FXMLLoader.<Node>load(new URL(toURI(context, file)))
	}
}