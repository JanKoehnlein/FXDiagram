package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.List
import javafx.collections.ObservableList

import static javafx.collections.FXCollections.*
import static org.eclipse.core.runtime.Platform.*

interface XDiagramConfig {

	def <T> List<? extends AbstractMapping<T>> getMappings(T domainObject)
}

@ModelNode(#['mappings'])
class AbstractDiagramConfig implements XDiagramConfig {
	
	@FxProperty ObservableList<AbstractMapping<?>> mappings = observableArrayList
	
	override <T> getMappings(T domainObject) {
		mappings.filter[isApplicable(domainObject)].map[it as AbstractMapping<T>].toList
	}
} 

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