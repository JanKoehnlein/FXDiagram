package de.fxdiagram.pde

import org.eclipse.osgi.service.resolver.BundleDescription
import org.eclipse.osgi.service.resolver.BundleSpecification
import org.eclipse.osgi.service.resolver.HostSpecification
import org.eclipse.osgi.service.resolver.ImportPackageSpecification
import org.eclipse.osgi.service.resolver.VersionRange
import org.eclipse.xtend.lib.annotations.Data
import org.osgi.framework.Constants

import static de.fxdiagram.pde.BundleDependency.Kind.*

@Data
abstract class BundleDependency {
	BundleDescription owner

	def BundleDescription getDependency()

	def boolean isReexport()

	def boolean isOptional()

	def VersionRange getVersionRange()

	def Kind getKind()

	static enum Kind {
		PACKAGE_IMPORT,
		REQUIRE_BUNDLE,
		FRAGMENT_HOST,
		UNQUALIFIED
	}
}

@Data
class UnqualifiedDependency extends BundleDependency {
	
	BundleDescription dependency
	
	override isReexport() {
		false
	}
	
	override isOptional() {
		false
	}
	
	override getVersionRange() {
		new VersionRange(
			dependency.version, true,
			dependency.version, true
		)
	}
	
	override getKind() {
		UNQUALIFIED
	}
	
} 

@Data
class PackageImport extends BundleDependency {
	ImportPackageSpecification packageImport

	override getKind() {
		PACKAGE_IMPORT
	}

	override getDependency() {
		packageImport.supplier?.supplier
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
class RequireBundle extends BundleDependency {
	BundleSpecification required

	override getKind() {
		REQUIRE_BUNDLE
	}

	override getDependency() {
		required.supplier?.supplier
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
class FragmentHost extends BundleDependency {
	BundleDescription host
	HostSpecification hostSpecification

	override getKind() {
		FRAGMENT_HOST
	}

	override getDependency() {
		hostSpecification.supplier?.supplier
	}

	override getVersionRange() {
		hostSpecification.versionRange ?: VersionRange.emptyRange
	}

	override isReexport() {
		false
	}

	override isOptional() {
		false
	}
}
