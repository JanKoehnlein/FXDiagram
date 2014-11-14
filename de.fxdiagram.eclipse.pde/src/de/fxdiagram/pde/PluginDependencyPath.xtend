package de.fxdiagram.pde

import java.util.List
import org.eclipse.xtend.lib.annotations.Data

@Data
class PluginDependencyPath {
	
	List<? extends PluginDependency> elements 
	
	new() {
		elements = #[]
	}
	
	private new(List<PluginDependency> elements) {
		this.elements = elements
	}
		
	def append(PluginDependency element) {
		new PluginDependencyPath((elements + #[element]).toList)
	}
	
}