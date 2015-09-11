package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException

import static de.fxdiagram.pde.BundleUtil.*

@ModelNode('kind', 'ownerSymbolicName', 'ownerVersion', 'importSymbolicName', 'importVersionRange')
class BundleDependencyDescriptor extends AbstractMappedElementDescriptor<BundleDependency> {

	@FxProperty(readOnly = true) BundleDependency.Kind kind
	@FxProperty(readOnly = true) String ownerSymbolicName
	@FxProperty(readOnly = true) String ownerVersion
	@FxProperty(readOnly = true) String importSymbolicName
	@FxProperty(readOnly = true) String importVersionRange
	
	new(BundleDependency.Kind kind, String ownerSymbolicName, String ownerVersion, String importSymbolicName, String importVersionRange,
		String mappingConfigID, String mappingID, BundleDescriptorProvider provider) {
		super(mappingConfigID, mappingID, provider)
		kindProperty.set(kind)
		ownerSymbolicNameProperty.set(ownerSymbolicName)
		ownerVersionProperty.set(ownerVersion)
		importSymbolicNameProperty.set(importSymbolicName)
		importVersionRangeProperty.set(importVersionRange)
	}

	override <U> withDomainObject((BundleDependency)=>U lambda) {
		val dependency = findBundleDependency(kind, ownerSymbolicName, ownerVersion, importSymbolicName, importVersionRange)
		if(dependency == null)
			throw new NoSuchElementException('Bundle dependency from ' + ownerSymbolicName + ' to ' + importSymbolicName + ' not found')
		lambda.apply(dependency)
	}

	override getName() {
		ownerSymbolicName + '->' + importSymbolicName 
	}

	override openInEditor(boolean select) {
	}
}
