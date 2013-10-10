package de.fxdiagram.core.extensions

import org.eclipse.core.runtime.FileLocator
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import java.net.URL

class UriExtensions {
	def static toURI(Object context, String file) {
		val resource = context.class.getResource(file)
		if(org.eclipse.core.internal.runtime.Activator.getDefault != null)
			FileLocator.toFileURL(resource).toExternalForm
		else
			resource.toExternalForm
	}
	
	def static fxmlNode(Object context, String file) {
		FXMLLoader.<Node>load(new URL(toURI(context, file)))
	}
}