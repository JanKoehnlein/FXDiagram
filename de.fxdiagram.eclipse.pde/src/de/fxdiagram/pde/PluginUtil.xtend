package de.fxdiagram.pde

import java.util.List
import java.util.Set
import org.eclipse.osgi.service.resolver.BundleDescription
import org.eclipse.pde.core.plugin.IFragmentModel
import org.eclipse.pde.core.plugin.IMatchRules
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.osgi.framework.Version

import static org.eclipse.pde.core.plugin.PluginRegistry.*
import org.eclipse.osgi.service.resolver.VersionRange

class PluginUtil {
	
	static def getInverseDependencies(IPluginModelBase plugin) {
		val dependency = plugin.bundleDescription
		dependency.dependents.map [
			val importer = findPlugin(symbolicName, version.toString)
			importer.findPluginDependency(dependency.symbolicName, dependency.version) 
		].filterNull
	}

	static def getAllInverseDependencies(IPluginModelBase plugin) {
		val result = <PluginDependencyPath>newArrayList
		plugin.addInverseDependencies(new PluginDependencyPath, result, newHashSet)
		result
	}
	
	protected static def void addInverseDependencies(IPluginModelBase plugin, PluginDependencyPath currentPath, List<PluginDependencyPath> pathes, Set<IPluginModelBase> processed) {
		plugin.inverseDependencies.forEach[
			val newPath = currentPath.append(it)
			pathes += newPath
			if(processed.add(owner))
				owner.addInverseDependencies(newPath, pathes, processed)
		]
	}
	
	static def getDependencies(IPluginModelBase plugin) {
		val bundleDescription = plugin.bundleDescription
		val result = <PluginDependency>newArrayList
		bundleDescription.requiredBundles.forEach [
			if(supplier != null) {
				val required = findPlugin(supplier.name, supplier.version.toString)
				if(required != null) 
					result += new RequireBundle(plugin, it)
			}
		]
		bundleDescription.importPackages.forEach [
			if(supplier != null) {
				val required = findPlugin(supplier.name, supplier.version.toString)
				if(required != null) 
					result += new PackageImport(plugin, it)
			}
		]
		if(bundleDescription instanceof IFragmentModel) {
			val fragment = bundleDescription.fragment
			val fragmentHost = findPlugin(fragment.name, fragment.version)
			if(fragmentHost != null) 
				result += new FragmentHost(plugin, fragment)
		}
		result
	}
	
	static def getAllDependencies(IPluginModelBase plugin) {
		val result = <PluginDependencyPath>newArrayList
		plugin.addDependencies(new PluginDependencyPath, result, newHashSet)
		result
	}
	
	protected static def void addDependencies(IPluginModelBase plugin, PluginDependencyPath currentPath, List<PluginDependencyPath> pathes, Set<IPluginModelBase> processed) {
		plugin.dependencies.forEach [
			val newPath = currentPath.append(it)
			pathes += newPath
			if(processed.add(dependency))
				dependency.addDependencies(newPath, pathes, processed)
		]
	}
	
	static def allPlugins() {
		allModels
	}
	
	static def findPlugin(String id, String versionRange) {
		findModel(id, versionRange, IMatchRules.COMPATIBLE, null)
	}

	static def getPlugin(BundleDescription it) {
		findModel(symbolicName, version.toString, IMatchRules.PERFECT, null)
	}

	static def findPluginDependency(String ownerID, String ownerVersion, String dependencyID, String dependencyVersionRange) {
		val owner = findPlugin(ownerID, ownerVersion)
		return findPluginDependency(owner, dependencyID, new VersionRange(dependencyVersionRange))
	}
	
	static def findPluginDependency(IPluginModelBase owner, String dependencyID, VersionRange dependencyVersionRange) {
		val requireBundle = owner.bundleDescription.requiredBundles.findFirst [
			supplier?.name == dependencyID && versionRange == dependencyVersionRange
		]
		if(requireBundle != null)
			return new RequireBundle(owner, requireBundle)
		val packageImport = owner.bundleDescription.importPackages.findFirst [
			name == dependencyID && versionRange == dependencyVersionRange
		]
		if(packageImport != null) 
			return new PackageImport(owner, packageImport)
		if(owner instanceof IFragmentModel) {
			val fragment = owner.fragment
			if(fragment?.pluginId == dependencyID && dependencyVersionRange.isIncluded(new Version(fragment.pluginVersion))) 
				return new FragmentHost(owner, fragment)
		}
		return null
	}
	
	static def findPluginDependency(IPluginModelBase owner, String dependencyID, Version dependencyVersion) {
		val requireBundle = owner.bundleDescription.requiredBundles.findFirst [
			supplier?.name == dependencyID && versionRange.isIncluded(dependencyVersion)
		]
		if(requireBundle != null)
			return new RequireBundle(owner, requireBundle)
		val packageImport = owner.bundleDescription.importPackages.findFirst [
			name == dependencyID && versionRange.isIncluded(dependencyVersion)
		]
		if(packageImport != null) 
			return new PackageImport(owner, packageImport)
		if(owner instanceof IFragmentModel) {
			val fragment = owner.fragment
			if(fragment?.pluginId == dependencyID && dependencyVersion == fragment.pluginVersion) 
				return new FragmentHost(owner, fragment)
		}
		return null
	}
}
