package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException
import org.apache.log4j.Logger
import org.eclipse.osgi.service.resolver.BundleDescription
import org.eclipse.pde.core.plugin.IMatchRules
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.pde.core.plugin.PluginRegistry
import org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor

import static de.fxdiagram.pde.BundleUtil.*

@ModelNode
class BundleDescriptor extends AbstractMappedElementDescriptor<BundleDescription> {
	
	static val LOG = Logger.getLogger(BundleDescriptor)
	
	new(String symbolicName, String version, String mappingConfigID, String mappingID, BundleDescriptorProvider provider) {
		super(symbolicName + '#' + version, symbolicName, mappingConfigID, mappingID, provider)
	}	
	
	override <U> withDomainObject((BundleDescription)=>U lambda) {
		val bundle = findBundle(symbolicName, version)
		if(bundle == null) 
			throw new NoSuchElementException('Bundle ' + symbolicName + ' not found')
		lambda.apply(bundle)
	}
	
	def <U> withPlugin((IPluginModelBase)=>U lambda) {
		val plugin = PluginRegistry.findModel(symbolicName, version, IMatchRules.PERFECT, null)
		if(plugin != null) {
			lambda.apply(plugin)
		} else {
			LOG.warn('Invalid BundleDescriptor ' + this) 
			null			
		}
	}
	
	def getSymbolicName() {
		id.split('#').head
	}
	
	def getVersion() {
		id.split('#').last
	}
	
	override openInEditor(boolean select) {
		withDomainObject [
			val plugin = PluginRegistry.findModel(symbolicName, version.toString, IMatchRules.PERFECT, null)
			ManifestEditor.openPluginEditor(plugin)	
		]
	}
}