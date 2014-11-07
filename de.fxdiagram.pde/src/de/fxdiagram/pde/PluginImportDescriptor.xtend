package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.xtext.glue.mapping.AbstractMappedElementDescriptor
import org.eclipse.pde.core.plugin.IPluginImport

@ModelNode
class PluginImportDescriptor extends AbstractMappedElementDescriptor<IPluginImport> {
	
	new(String ownerSymbolicName, String ownerVersion, 
		String importSymbolicName, String importVersionRange, 
		String mappingConfigID, String mappingID,
		PluginDescriptorProvider provider) {
		super(#[ownerSymbolicName, ownerVersion, importSymbolicName, importVersionRange].join('#'), 
			importSymbolicName + ' ' + importVersionRange, 
			mappingConfigID, mappingID, provider)
	}	
	
	override <U> withDomainObject((IPluginImport)=>U lambda) {
		val handle = (provider as PluginDescriptorProvider).getPluginImport(ownerSymbolicName, ownerVersion, importSymbolicName)
		lambda.apply(handle) 
	}
	
	def getOwnerSymbolicName() {
		id.split('#').head
	}
	
	def getOwnerVersion() {
		id.split('#').get(1)
	}

	def getImportSymbolicName() {
		id.split('#').get(2)
	}
	
	def getImportVersionRange() {
		id.split('#').last
	}
	
	override openInEditor(boolean select) {
		null
	}
}

