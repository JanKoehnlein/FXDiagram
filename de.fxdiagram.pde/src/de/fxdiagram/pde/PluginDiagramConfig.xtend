package de.fxdiagram.pde

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.shapes.BaseConnection
import org.eclipse.pde.core.plugin.IPluginImport
import org.eclipse.pde.core.plugin.IPluginModelBase

import static de.fxdiagram.pde.PluginUtil.*

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*

class PluginDiagramConfig extends AbstractDiagramConfig {

	override protected createDomainObjectProvider() {
		new PluginDescriptorProvider
	}
	
	val pluginNode = new NodeMapping<IPluginModelBase>(this, "pluginNode") {
		
		override createNode(IMappedElementDescriptor<IPluginModelBase> descriptor) {
			new PluginNode(descriptor as PluginDescriptor)
		}
		
		override calls() {
			importConnection.outConnectionForEach[ 
				pluginBase.imports.toList.filter[
					findPlugin(id, version) != null
				]
			].makeLazy[getArrowButton("Add dependency")]
			exportConnection.inConnectionForEach[ plugin |
				plugin.bundleDescription.dependents.map[
					val dependent = findPlugin(symbolicName, version.toString)
					dependent.pluginBase.imports.findFirst[getId == plugin.pluginBase.id]
				].filterNull
			].makeLazy[getInverseArrowButton("Add dependent")]
		}
	}
	
	val importConnection = new ConnectionMapping<IPluginImport>(this, "importConnection") {
		override calls() {
			pluginNode.target [
				findPlugin(id, version)
			]
		}
		
		override createConnection(IMappedElementDescriptor<IPluginImport> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  

	val exportConnection = new ConnectionMapping<IPluginImport>(this, "exportConnection") {
		override calls() {
			pluginNode.source [
				pluginModel
			]
		}
		
		override createConnection(IMappedElementDescriptor<IPluginImport> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  

	protected def createPluginImportConnection(IMappedElementDescriptor<IPluginImport> descriptor) {
		new BaseConnection(descriptor) => [ connection |
			val label = new XConnectionLabel(connection)
			descriptor.withDomainObject [
			 	label.text.text = version
			 	if(optional) 
			 		connection.strokeDashArray.setAll(5.0, 5.0)
			 	if(!reexported)
			 		connection.targetArrowHead = new LineArrowHead(connection, false)
			 	null
			]
		]
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			IPluginModelBase: 
				add(pluginNode)
		}
	}	
	
}