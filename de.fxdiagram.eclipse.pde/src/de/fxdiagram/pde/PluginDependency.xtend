package de.fxdiagram.pde

import org.eclipse.osgi.service.resolver.BundleSpecification
import org.eclipse.osgi.service.resolver.ImportPackageSpecification
import org.eclipse.osgi.service.resolver.VersionRange
import org.eclipse.pde.core.plugin.IFragment
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.xtend.lib.annotations.Data

import static extension de.fxdiagram.pde.PluginUtil.*
import org.osgi.framework.Constants

@Data
abstract class PluginDependency {
	IPluginModelBase owner

	def IPluginModelBase getDependency()
	
	def boolean isReexport()
	def boolean isOptional()
	def VersionRange getVersionRange()
}

@Data
class PackageImport extends PluginDependency {
	ImportPackageSpecification packageImport
	
	override getDependency() {
		(packageImport.supplier.supplier).plugin
	}
	
	override isReexport() {
		false
	}
	
	override getVersionRange() {
		packageImport.versionRange ?: VersionRange.emptyRange
	}
	
	override isOptional() {
		val directive = packageImport.getDirective(Constants.RESOLUTION_DIRECTIVE)
		directive == 'optional'
	}
	
}

@Data
class RequireBundle extends PluginDependency {
	BundleSpecification required
	
	override getDependency() {
		(required.supplier.supplier).plugin
	}
	
	override getVersionRange() {
		required.versionRange ?: VersionRange.emptyRange
	}
	
	override isReexport() {
		required.exported
	}
	
	override isOptional() {
		required.optional
	}
	
}

@Data
class FragmentHost extends PluginDependency {
	IFragment fragment

	override getDependency() {
		findPlugin(fragment.pluginId, fragment.version)
	}
	
	override getVersionRange() {
		new VersionRange(fragment.pluginVersion)
	}
	
	override isReexport() {
		false
	}
	
	override isOptional() {
		false
	}
}
