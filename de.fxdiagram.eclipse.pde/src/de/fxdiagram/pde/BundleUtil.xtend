package de.fxdiagram.pde

import java.util.Iterator
import java.util.List
import java.util.Set
import org.eclipse.osgi.service.resolver.BundleDescription
import org.eclipse.osgi.service.resolver.VersionRange
import org.eclipse.pde.core.plugin.IMatchRules
import org.eclipse.pde.core.plugin.PluginRegistry
import org.apache.log4j.Logger

class BundleUtil {
	
	static val LOG = Logger.getLogger(BundleUtil)
	
	static def isConsiderFragments() {
		false
	}
	
	static def getAllDependencyBundles(BundleDescription bundle) {
		val result = newHashSet
		bundle.addDependencies(result)
		result
	}
	
	protected static def void addDependencies(BundleDescription bundle, Set<BundleDescription> dependencies) {
		bundle.dependencyBundles.forEach [
			if(dependencies.add(it))
				addDependencies(dependencies)
		]
	}
	
	static def getDependencyBundles(BundleDescription bundle) {
		val dependencies = bundle.resolvedRequires + bundle.resolvedImports.map[bundle]
		if(considerFragments) 
			dependencies + bundle.fragments
		else 
			dependencies
	} 
	
	static def getAllDependentBundles(BundleDescription bundle) {
		val result = newHashSet
		bundle.addInverseDependencies(result)
		result
	}
	
	protected static def void addInverseDependencies(BundleDescription bundle, Set<BundleDescription> inverseDependencies) {
		bundle.dependentBundles.forEach [
			if(inverseDependencies.add(it)) 
				addInverseDependencies(inverseDependencies)
		]
	}
	
	static def getDependentBundles(BundleDescription bundle) {
		bundle.dependents.filter[host?.supplier?.supplier != bundle]
	} 
	
	static def getAllBundleDependencies(BundleDescription source, BundleDescription target) {
		val paths = <BundleDependencyPath>newLinkedList
		source.addBundleDependencies(new BundleDependencyPath, paths, newHashSet)
		val result = newArrayList
		var endNodes = #{ target }
		val processedEndNodes = newHashSet
		do {
			LOG.info('#paths: ' + paths.size + '  #endnodes: ' + endNodes.size)
			val newEndNodes = newHashSet
			for(var Iterator<BundleDependencyPath> i = paths.iterator; i.hasNext;) {
				val path= i.next()
				if(endNodes.contains(path.elements.last.dependency)) {
					result += path.elements
					i.remove
					newEndNodes += path.elements.map [
						owner
					].filter[
						!processedEndNodes.contains(it)
					]
				}
			}
			processedEndNodes += endNodes
			endNodes = newEndNodes
		} while(!endNodes.empty) 
		result
	}
	
	protected static def void addBundleDependencies(BundleDescription bundle, BundleDependencyPath currentPath, List<BundleDependencyPath> pathes, Set<BundleDependency> processed) {
		bundle.bundleDependencies.forEach [
			if(processed.add(it)) {
				val newPath = currentPath.append(it)
				pathes += newPath
				dependency.addBundleDependencies(newPath, pathes, processed)
			}
		]
	}
	
	static def getBundleDependencies(BundleDescription bundle) {
		val result = <BundleDependency>newArrayList
		bundle.requiredBundles.forEach [
			result += new RequireBundle(bundle, it)
		]
		bundle.importPackages.forEach [
			result += new PackageImport(bundle, it)
		]
		if(considerFragments) {
			bundle.fragments.forEach [
				result += new FragmentHost(bundle, it)
			]
		}
		return result.filter [ dependency != null ]
	}
	
	static def getInverseBundleDependencies(BundleDescription bundle) {
		val result = <BundleDependency>newArrayList()
		bundle.dependents.forEach [ owner |
			owner.requiredBundles.filter [
				supplier?.supplier == bundle
			].forEach[
				result += new RequireBundle(owner, it)					
			] 
			owner.importPackages.filter [
				supplier?.supplier == bundle
			].forEach[
				result += new PackageImport(owner, it)					
			] 
			if(considerFragments && bundle.host?.supplier?.supplier == owner) 
				result += new FragmentHost(owner, bundle)
		]
		return result.filter [ dependency != null ]
	}
	
	static def allBundles() {
		PluginRegistry.getAllModels.map[bundleDescription]
	}
	
	static def findBundle(String id, String version) {
		PluginRegistry.findModel(id, version, IMatchRules.PERFECT, null).bundleDescription
	}

	static def findCompatibleBundle(String id, String versionRange) {
		PluginRegistry.findModel(id, versionRange, IMatchRules.COMPATIBLE, null).bundleDescription
	}

	static def findBundleDependency(BundleDependency.Kind kind, String ownerID, String ownerVersion, String dependencyID, String dependencyVersionRange) {
		val owner = findBundle(ownerID, ownerVersion)
		return findBundleDependency(kind, owner, dependencyID, new VersionRange(dependencyVersionRange))
	}
	
	static def findBundleDependency(BundleDependency.Kind kind, BundleDescription owner, String dependencyID, VersionRange dependencyVersionRange) {
		switch kind {
			case REQUIRE_BUNDLE: {
				val requireBundle = owner.requiredBundles.findFirst [
					supplier?.name == dependencyID && versionRange == dependencyVersionRange
				]
				if(requireBundle != null)
					return new RequireBundle(owner, requireBundle)
			}
			case PACKAGE_IMPORT: {
				val packageImport = owner.importPackages.findFirst [
					name == dependencyID && versionRange == dependencyVersionRange
				]
				if(packageImport != null) 
					return new PackageImport(owner, packageImport)
			}
			case FRAGMENT_HOST: {
				val fragment = owner.fragments.findFirst [
					symbolicName == dependencyID
				]
				if(fragment != null)
					return new FragmentHost(owner, fragment)
			}
			case UNQUALIFIED: {
				new UnqualifiedDependency(owner, findCompatibleBundle(dependencyID, dependencyVersionRange.toString))
			}
		}
		return null
	}
}
