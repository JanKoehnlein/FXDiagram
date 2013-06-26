package de.fxdiagram.core.layout

import com.google.inject.Guice
import de.cau.cs.kieler.kiml.graphviz.dot.GraphvizDotRuntimeModule
import de.cau.cs.kieler.kiml.graphviz.dot.GraphvizDotStandaloneSetup
import de.cau.cs.kieler.kiml.graphviz.dot.transform.DotHandler
import de.cau.cs.kieler.kiml.service.TransformationService
import de.cau.cs.kieler.kiml.service.formats.GraphFormatData
import java.util.logging.Level
import java.util.logging.Logger
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IConfigurationElement

/** 
 * KIELER's Graphviz layouts rely on the TransformationService to be initialized. 
 */
class LoggingTransformationService extends TransformationService {
	
	static val LOG = Logger.getLogger(LoggingTransformationService.canonicalName)
	
	GraphFormatData dotFormatData
	
	override getFormatData(String id) {
		if (id==DotHandler.ID) {
			if(dotFormatData == null) {
				GraphvizDotStandaloneSetup.doSetup
				val graphvizDotRuntimeModule = new GraphvizDotRuntimeModule
				val injector = Guice.createInjector(graphvizDotRuntimeModule)
				dotFormatData = new GraphFormatData	=> [
					handler = injector.getInstance(DotHandler)
				]	
			} 
			dotFormatData
		} else {
			super.getFormatData(id)
		}
	}
	
	override protected reportError(String extensionPoint, IConfigurationElement element, String attribute, Throwable exception) {
		LOG.log(Level.SEVERE, "Error in LoggingTransformationService", exception)
	}
	
	override protected reportError(CoreException exception) {
		LOG.log(Level.SEVERE, "Error in LoggingTransformationService", exception)
	}
		
}