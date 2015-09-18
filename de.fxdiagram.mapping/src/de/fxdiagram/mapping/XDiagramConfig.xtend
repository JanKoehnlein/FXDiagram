package de.fxdiagram.mapping

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XDomainObjectShape
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
	def <ARG> Iterable<? extends MappingCall<?, ARG>> getEntryCalls(ARG domainObject)

	def <ARG> Iterable<? extends AbstractMapping<ARG>> getMappings(ARG domainObject)
	
	def AbstractMapping<?> getMappingByID(String mappingID)
	
	def String getID()

	def String getLabel()
	
	def <ARG> void addMapping(AbstractMapping<ARG> mapping)
	
	def IMappedElementDescriptorProvider getDomainObjectProvider()
	
	def void initialize(XDomainObjectShape shape)
	
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
					val config = createExecutableExtension('class') as AbstractDiagramConfig
					val id = getAttribute('id')
					config.setID(id)
					config.setLabel(getAttribute('label'))
					addConfig(config)
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
	
	@Accessors(PUBLIC_GETTER)
	IMappedElementDescriptorProvider domainObjectProvider = createDomainObjectProvider()
	
	protected abstract def IMappedElementDescriptorProvider createDomainObjectProvider()
	
	override getMappingByID(String mappingID) {
		mappings.get(mappingID)
	}
	
	protected abstract def <ARG> void entryCalls(ARG domainArgument, MappingAcceptor<ARG> acceptor)

	override <ARG> getMappings(ARG domainObject) {
		mappings.values.filter[isApplicable(domainObject)].map[it as AbstractMapping<ARG>]
	}
	
	override <ARG> getEntryCalls(ARG domainArgument) {
		val acceptor = new MappingAcceptor<ARG>
		entryCalls(domainArgument, acceptor)
		acceptor.mappingCalls
	}

	//TODO: remove and make sure serialization works	
	override <ARG> addMapping(AbstractMapping<ARG> mapping) {
		if(mappings.containsKey(mapping.ID)) {
			LOG.severe('''Duplicate mapping id=«mapping.ID» in «ID»''')
		} else {
			mappings.put(mapping.ID, mapping)
		}
	}
} 

class MappingAcceptor<ARG> {
	
	val List<MappingCall<?, ARG>> mappingCalls = newArrayList
	
	def add(AbstractMapping<?> mapping) {
		add(mapping as AbstractMapping<ARG>, [it])
	}
	
	def <T> add(AbstractMapping<T> mapping, (ARG)=>T selector) {
		mappingCalls.add(switch mapping {
			NodeMapping<T>: new NodeMappingCall(selector, mapping)
			DiagramMapping<T>: new DiagramMappingCall(selector, mapping) 
			ConnectionMapping<T>: new ConnectionMappingCall(selector, mapping) 
		})
	}
	
	def getMappingCalls() {
		mappingCalls
	}
}

