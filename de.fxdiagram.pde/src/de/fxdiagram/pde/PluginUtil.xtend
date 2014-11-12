package de.fxdiagram.pde

import java.util.List
import java.util.Set
import org.eclipse.pde.core.plugin.IMatchRules
import org.eclipse.pde.core.plugin.IPluginModelBase

import static org.eclipse.pde.core.plugin.PluginRegistry.*

class PluginUtil {
	
	static def getAllDependentsPaths(IPluginModelBase plugin) {
		val result = <PluginDependencyPath>newArrayList
		plugin.addDependentsPaths(new PluginDependencyPath, result, newHashSet)
		result
	}
	
	protected static def void addDependentsPaths(IPluginModelBase plugin, PluginDependencyPath currentPath, List<PluginDependencyPath> pathes, Set<IPluginModelBase> processed) {
		val dependency = plugin.bundleDescription
		dependency.dependents.forEach [
			val importer = findPlugin(symbolicName, version.toString)
			if(importer != null) {
				val pluginImport = findPluginImport(importer, dependency.symbolicName) 
				if(pluginImport != null) {
					// TODO fragments
					val newPath = currentPath.append(new PluginDependencyPath.PathElement(pluginImport, importer))
					pathes += newPath
					if(processed.add(importer))
						importer.addDependentsPaths(newPath, pathes, processed)
				}
			}
		]
	}
	
	static def getAllImportPaths(IPluginModelBase plugin) {
		val result = <PluginDependencyPath>newArrayList
		plugin.addImportPaths(new PluginDependencyPath, result, newHashSet)
		result
	}
	
	protected static def void addImportPaths(IPluginModelBase plugin, PluginDependencyPath currentPath, List<PluginDependencyPath> pathes, Set<IPluginModelBase> processed) {
		// TODO fragments
		plugin.pluginBase.imports.forEach [
			val imported = findPlugin(id, version)
			if(imported != null) {
				val newPath = currentPath.append(new PluginDependencyPath.PathElement(it, imported))
				pathes += newPath
				if(processed.add(imported))
					imported.addImportPaths(newPath, pathes, processed)
			}
		]
	}
	
	
	static def allPlugins() {
		allModels
	}
	
	static def findPlugin(String id, String version) {
		findModel(id, version, IMatchRules.GREATER_OR_EQUAL, null)
	}

	static def findPluginImport(String id, String version, String importName) {
		val owner = findPlugin(id, version)
		return findPluginImport(owner, importName)
	}
	
	static def findPluginImport(IPluginModelBase owner, String importName) {
		owner.pluginBase.imports.findFirst[getId == importName]
	}
}
