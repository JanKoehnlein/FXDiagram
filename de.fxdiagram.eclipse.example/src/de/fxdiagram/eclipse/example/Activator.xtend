package de.fxdiagram.eclipse.example

import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

class Activator extends AbstractUIPlugin {

	public static val PLUGIN_ID = "de.fxdiagram.eclipse"

	static Activator plugin
	
	override start(BundleContext context) throws Exception {
		super.start(context)
		plugin = this
	}

	override stop(BundleContext context) throws Exception {
		plugin = null
		super.stop(context)
	}

	def static Activator getDefault() {
		plugin
	}
}