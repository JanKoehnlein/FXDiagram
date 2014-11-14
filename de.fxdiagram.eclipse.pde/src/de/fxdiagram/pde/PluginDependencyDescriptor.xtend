package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.mapping.AbstractMappedElementDescriptor

import static de.fxdiagram.pde.PluginUtil.*

@ModelNode
class PluginDependencyDescriptor extends AbstractMappedElementDescriptor<PluginDependency> {

	new(String ownerSymbolicName, String ownerVersion, String importSymbolicName, String importVersionRange,
		String mappingConfigID, String mappingID, PluginDescriptorProvider provider) {
		super(#[ownerSymbolicName, ownerVersion, importSymbolicName, importVersionRange].join('#'),
			importSymbolicName + ' ' + importVersionRange, mappingConfigID, mappingID, provider)
	}

	override <U> withDomainObject((PluginDependency)=>U lambda) {
		val handle = findPluginDependency(ownerSymbolicName, ownerVersion, importSymbolicName, importVersionRange)
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
