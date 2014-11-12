package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.xtext.glue.mapping.AbstractMappedElementDescriptor
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor

import static de.fxdiagram.pde.PluginUtil.*

@ModelNode
class PluginDescriptor extends AbstractMappedElementDescriptor<IPluginModelBase> {
	
	new(String symbolicName, String version, String name, String mappingConfigID, String mappingID, PluginDescriptorProvider provider) {
		super(symbolicName + '#' + version, name, mappingConfigID, mappingID, provider)
	}	
	
	override <U> withDomainObject((IPluginModelBase)=>U lambda) {
		val plugin = findPlugin(symbolicName, version)
		lambda.apply(plugin) 
	}
	
	def getSymbolicName() {
		id.split('#').head
	}
	
	def getVersion() {
		id.split('#').last
	}
	
	override openInEditor(boolean select) {
		withDomainObject[
			ManifestEditor.openPluginEditor(it)	
		]
	}
}