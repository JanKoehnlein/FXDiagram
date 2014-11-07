package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.xtext.glue.mapping.AbstractMappedElementDescriptor
import org.eclipse.pde.core.plugin.IPluginModelBase

@ModelNode
class PluginDescriptor extends AbstractMappedElementDescriptor<IPluginModelBase> {
	
	new(String symbolicName, String version, String name, String mappingConfigID, String mappingID, PluginDescriptorProvider provider) {
		super(symbolicName + '#' + version, name, mappingConfigID, mappingID, provider)
	}	
	
	override <U> withDomainObject((IPluginModelBase)=>U lambda) {
		val plugin = (provider as PluginDescriptorProvider).getPlugin(symbolicName, version)
		lambda.apply(plugin) 
	}
	
	def getSymbolicName() {
		id.split('#').head
	}
	
	def getVersion() {
		id.split('#').last
	}
	
	override openInEditor(boolean select) {
		// TODO: open manifest multi-page editor
		null
	}
	
}