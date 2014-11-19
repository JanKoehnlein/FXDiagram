package de.fxdiagram.pde

import java.util.List
import org.eclipse.xtend.lib.annotations.Data

@Data
class BundleDependencyPath {
	
	List<? extends BundleDependency> elements 
	
	new() {
		elements = #[]
	}
	
	private new(List<BundleDependency> elements) {
		this.elements = elements
	}
		
	def append(BundleDependency element) {
		new BundleDependencyPath((elements + #[element]).toList)
	}
	
}