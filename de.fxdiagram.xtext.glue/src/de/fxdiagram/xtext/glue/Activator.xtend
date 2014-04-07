package de.fxdiagram.xtext.glue

import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

class Activator extends AbstractUIPlugin {

	public static val String PLUGIN_ID = 'de.fxdiagram.xtext.glue'

	static Activator plugin
	
	override start(BundleContext context) throws Exception {
		super.start(context)
		plugin = this
	}

	override stop(BundleContext context) throws Exception {
		plugin = null
		super.stop(context)
	}

 	static def getDefault() {
		plugin
	}
}