package de.fxdiagram.pde

import java.util.List
import org.eclipse.pde.core.plugin.IPluginImport
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.xtend.lib.annotations.Data

@Data
class PluginDependencyPath {
	
	List<? extends PathElement> elements 
	
	new() {
		elements = #[]
	}
	
	private new(List<PathElement> elements) {
		this.elements = elements
	}
		
	def append(PathElement element) {
		new PluginDependencyPath((elements + #[element]).toList)
	}
	
	@Data
	static class PathElement {
		IPluginImport pluginImport
		IPluginModelBase plugin
	}
}