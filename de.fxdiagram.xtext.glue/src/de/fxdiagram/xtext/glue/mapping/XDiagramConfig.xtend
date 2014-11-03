package de.fxdiagram.xtext.glue.mapping

import de.fxdiagram.annotations.logging.Logging
import java.util.List
import java.util.Map
import org.eclipse.xtend.lib.annotations.Accessors

import static org.eclipse.core.runtime.Platform.*

interface XDiagramConfig {

	def <ARG> Iterable<? extends MappingCall<?, ARG>> getEntryCalls(ARG domainObject)
	
	def AbstractMapping<?> getMappingByID(String mappingID)
	
	def String getID()
	
	def <ARG> void addMapping(AbstractMapping<ARG> mapping)
	
	def IMappedElementDescriptorProvider getDomainObjectProvider()
	
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
}

@Logging
abstract class AbstractDiagramConfig implements XDiagramConfig {
	
	Map<String, AbstractMapping<?>> mappings = newHashMap

	@Accessors String ID
	
	@Accessors(PUBLIC_GETTER)
	IMappedElementDescriptorProvider domainObjectProvider = createDomainObjectProvider()
	
	protected def IMappedElementDescriptorProvider createDomainObjectProvider() {
		new XtextDomainObjectProvider
	}
	
	override getMappingByID(String mappingID) {
		mappings.get(mappingID)
	}
	
	protected abstract def <ARG> void entryCalls(ARG domainArgument, MappingAcceptor<ARG> acceptor)
	
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

