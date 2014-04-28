package de.fxdiagram.xtext.glue.mapping

import java.util.Map

import static org.eclipse.core.runtime.Platform.*
import de.fxdiagram.annotations.logging.Logging

interface XDiagramConfig {

	def <T> Iterable<? extends AbstractMapping<T>> getMappings(T domainObject)
	
	def AbstractMapping<?> getMappingByID(String mappingID)
	
	def String getID()
}

@Logging
class AbstractDiagramConfig implements XDiagramConfig {
	
	Map<String, AbstractMapping<?>> mappings = newHashMap

	@Property String ID
	
	override <T> getMappings(T domainObject) {
		mappings.values.filter[isApplicable(domainObject)].map[it as AbstractMapping<T>]
	}
	
	override getMappingByID(String mappingID) {
		mappings.get(mappingID)
	}
	
	def addMapping(AbstractMapping<?> mapping) {
		if(mappings.containsKey(mapping.ID)) {
			LOG.severe('''Duplicate mapping id=«mapping.ID» in «ID»''')
		} else {
			mapping.config = this
			mappings.put(mapping.getID(), mapping)
		}
	}
} 

@Logging
class XDiagramConfigRegistry {
	
	static XDiagramConfigRegistry instance
	
	Map<String, XDiagramConfig> configs = newHashMap
	
	static def getInstance() {
		instance ?: (instance = new XDiagramConfigRegistry)
	}
	
	private new() {
		addStaticConfigurations
	}
	
	def Iterable<? extends XDiagramConfig> getConfigurations() {
		configs.values
	}
	
	protected def addStaticConfigurations() {
		extensionRegistry.getConfigurationElementsFor('de.fxdiagram.xtext.glue.fxDiagramConfig').forEach[
			val config = createExecutableExtension('class') as AbstractDiagramConfig
			val id = getAttribute('id')
			config.setID(id)
			if(configs.containsKey(id))
				LOG.severe("Duplicate fxDiagramConfig id=" + id)
			else
				configs.put(id, config)
		]
	}
	
	def XDiagramConfig getConfigByID(String configID) {
		configs.get(configID)
	}
}