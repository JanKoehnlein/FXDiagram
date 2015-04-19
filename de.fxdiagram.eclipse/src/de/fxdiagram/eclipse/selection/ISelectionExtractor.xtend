package de.fxdiagram.eclipse.selection

import java.util.List
import org.apache.log4j.Logger
import org.eclipse.core.runtime.Platform
import org.eclipse.ui.IWorkbenchPart

interface ISelectionExtractor {
	def boolean addSelectedElement(IWorkbenchPart activePart, Acceptor acceptor)
	
	interface Acceptor {
		def boolean accept(Object selectedElement)  
	}
	
	class Registry {
		static val LOG = Logger.getLogger(Registry)

		static Registry INSTANCE
		 
		List<ISelectionExtractor> extractors = newArrayList
		
		private new() {
			Platform.extensionRegistry.getConfigurationElementsFor('de.fxdiagram.eclipse.selectionExtractor').forEach[
				try {
					val extractor = createExecutableExtension('class') as ISelectionExtractor
					extractors += extractor							
				} catch(Exception exc) {
					LOG.error(exc)
				}
			]
		}
		
		static def getInstance() {
			INSTANCE ?: (INSTANCE = new Registry)
		}
		
		def Iterable<ISelectionExtractor> getSelectionExtractors() {
			extractors
		}		
	} 
}