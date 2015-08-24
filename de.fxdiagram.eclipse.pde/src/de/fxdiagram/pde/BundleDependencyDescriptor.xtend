package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException

import static de.fxdiagram.pde.BundleUtil.*

@ModelNode('kind')
class BundleDependencyDescriptor extends AbstractMappedElementDescriptor<BundleDependency> {

	@FxProperty(readOnly = true) BundleDependency.Kind kind

	new(BundleDependency.Kind kind, String ownerSymbolicName, String ownerVersion, String importSymbolicName, String importVersionRange,
		String mappingConfigID, String mappingID, BundleDescriptorProvider provider) {
		super(#[ownerSymbolicName, ownerVersion, importSymbolicName, importVersionRange].join('#'),
			importSymbolicName + ' ' + importVersionRange, mappingConfigID, mappingID, provider)
		kindProperty.set(kind)
	}

	override <U> withDomainObject((BundleDependency)=>U lambda) {
		val dependency = findBundleDependency(kind, ownerSymbolicName, ownerVersion, importSymbolicName, importVersionRange)
		if(dependency == null)
			throw new NoSuchElementException('Bundle dependency from ' + ownerSymbolicName + ' to ' + importSymbolicName + ' not found')
		lambda.apply(dependency)
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
	}
}
