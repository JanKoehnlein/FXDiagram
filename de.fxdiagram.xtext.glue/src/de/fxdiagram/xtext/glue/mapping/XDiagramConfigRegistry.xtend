package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.annotations.properties.FxProperty
import java.util.List
import javafx.collections.ObservableList

import static javafx.collections.FXCollections.*
import static org.eclipse.core.runtime.Platform.*

class XDiagramConfigRegistry {
	
	static XDiagramConfigRegistry instance
	
	@FxProperty ObservableList<XDiagramConfig> configs = observableArrayList
	
	static def getInstance() {
		instance ?: (instance = new XDiagramConfigRegistry)
	}
	
	private new() {
		configs += staticConfigurations
	}
	
	def List<XDiagramConfig> getConfigurations() {
		configs
	}
	
	protected def getStaticConfigurations() {
		extensionRegistry.getConfigurationElementsFor('de.fxdiagram.xtext.glue.fxDiagramConfig').map[
			createExecutableExtension('class') as XDiagramConfig
		].toList
	}
}