package de.fxdiagram.mapping

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XDomainObjectOwner
import de.fxdiagram.mapping.execution.ConnectionEntryCall
import de.fxdiagram.mapping.execution.DiagramEntryCall
import de.fxdiagram.mapping.execution.EntryCall
import de.fxdiagram.mapping.execution.NodeEntryCall
import java.util.List
import java.util.Map
import org.eclipse.core.runtime.Platform
import org.eclipse.xtend.lib.annotations.Accessors

import static de.fxdiagram.core.extensions.ClassLoaderExtensions.*

/**
 * Stores a set of {@link AbstractMapping}s for a sepecific domain.
 * 
 * {@link XDiagramConfig}s must be registered to the {@link Registry}
 * to be picked up by the runtime using the extension point 
 * <code>de.fxdiagram.eclipse.fxDiagramConfig</code>.
 * 
 * Consider inheriting from {@link AbstractDiagramConfig} instead of directly 
 * implementing this interface.
 */
interface XDiagramConfig {

	/**
	 * @return all possible calls to add a diagram element for the given domain object 
	 */
	def <ARG> Iterable<? extends EntryCall<ARG>> getEntryCalls(ARG domainObject)

	def Iterable<? extends AbstractMapping<?>> getMappings()
	
	def AbstractMapping<?> getMappingByID(String mappingID)
	
	def String getID()

	def String getLabel()
	
	def <ARG> void addMapping(AbstractMapping<ARG> mapping)
	
	def IMappedElementDescriptorProvider getDomainObjectProvider()
	
	def void initialize(XDomainObjectOwner shape)
	
	@Logging  
	static class Registry {
 	
		static XDiagramConfig.Registry instance
		
		Map<String, XDiagramConfig> configs = newHashMap
		
		static def getInstance() {
			instance ?: (instance = new XDiagramConfig.Registry)
		}
		
		private new() {
			addStaticConfigurations
		}
		
		def Iterable<? extends XDiagramConfig> getConfigurations() {
			configs.values
		}
		
		protected def addStaticConfigurations() {
			if(equinox) {
				Platform.extensionRegistry.getConfigurationElementsFor('de.fxdiagram.mapping.fxDiagramConfig').forEach[
					try {
						val config = createExecutableExtension('class') as AbstractDiagramConfig
						val id = getAttribute('id')
						config.setID(id)
						config.setLabel(getAttribute('label'))
						addConfig(config)						
					} catch (Exception exc) {
						LOG.severe(exc.message)
					}
				]
			}
		}
		
		def addConfig(XDiagramConfig config) {
			val id = config.ID
			if(configs.containsKey(id))
				LOG.severe("Duplicate fxDiagramConfig id=" + id)
			else
				configs.put(id, config)	
		}
		
		def XDiagramConfig getConfigByID(String configID) {
			configs.get(configID)
		}
	}
}

@Logging
abstract class AbstractDiagramConfig implements XDiagramConfig {
	
	Map<String, AbstractMapping<?>> mappings = newHashMap

	@Accessors String ID
	@Accessors String label
	
	IMappedElementDescriptorProvider domainObjectProvider
	
	protected abstract def IMappedElementDescriptorProvider createDomainObjectProvider()
	
	override getMappingByID(String mappingID) {
		mappings.get(mappingID)
	}
	
	protected abstract def <ARG> void entryCalls(ARG domainArgument, MappingAcceptor<ARG> acceptor)

	override getMappings() {
 		mappings.values
    }
    
	override <ARG> getEntryCalls(ARG domainArgument) {
		val acceptor = new MappingAcceptor<ARG>
		entryCalls(domainArgument, acceptor)
		acceptor.entryCalls
	}

	//TODO: remove and make sure serialization works	
	override <ARG> addMapping(AbstractMapping<ARG> mapping) {
		if(mapping instanceof AbstractLabelMapping<?> || !mappings.containsKey(mapping.ID)) {
			mappings.put(mapping.ID, mapping)
		} else {
			LOG.severe('''Duplicate mapping id=«mapping.ID» in «ID»''')
		}
	}
	
	override getDomainObjectProvider() {
		domainObjectProvider ?: (domainObjectProvider = createDomainObjectProvider) 
	}
} 

class MappingAcceptor<ARG> {
	
	val List<EntryCall<ARG>> entryCalls = newArrayList
	
	def add(AbstractMapping<?> mapping) {
		add(mapping as AbstractMapping<ARG>, [it])
	}
	
	def <T> add(AbstractMapping<T> mapping, (ARG)=>T selector) {
		entryCalls.add(
			switch mapping {
				NodeMapping<T>: new NodeEntryCall(selector, mapping)
				DiagramMapping<T>: new DiagramEntryCall(selector, mapping) 
				ConnectionMapping<T>: new ConnectionEntryCall(selector, mapping) 
			})
	}
	
	def add(EntryCall<ARG> execution) {
		entryCalls.add(execution)
	}
	
	def getEntryCalls() {
		entryCalls
	}
}

