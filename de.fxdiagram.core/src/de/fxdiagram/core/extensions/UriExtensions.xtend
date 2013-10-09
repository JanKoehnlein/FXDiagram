package de.fxdiagram.core.extensions

import org.eclipse.core.runtime.FileLocator

class UriExtensions {
	def static toURI(Object context, String file) {
		val resource = context.class.getResource(file)
		if(org.eclipse.core.internal.runtime.Activator.getDefault != null)
			FileLocator.toFileURL(resource).toExternalForm
		else
			resource.toExternalForm
	}
}