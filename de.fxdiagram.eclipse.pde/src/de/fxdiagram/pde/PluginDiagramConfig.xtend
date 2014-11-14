package de.fxdiagram.pde

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.eclipse.mapping.AbstractDiagramConfig
import de.fxdiagram.eclipse.mapping.ConnectionMapping
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor
import de.fxdiagram.eclipse.mapping.MappingAcceptor
import de.fxdiagram.eclipse.mapping.NodeMapping
import de.fxdiagram.eclipse.shapes.BaseConnection
import org.eclipse.pde.core.plugin.IPluginModelBase

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*
import static extension de.fxdiagram.pde.PluginUtil.*

class PluginDiagramConfig extends AbstractDiagramConfig {

	override protected createDomainObjectProvider() {
		new PluginDescriptorProvider
	}
	
	val pluginNode = new NodeMapping<IPluginModelBase>(this, "pluginNode") {
		
		override createNode(IMappedElementDescriptor<IPluginModelBase> descriptor) {
			new PluginNode(descriptor as PluginDescriptor)
		}
		
		override calls() {
			dependencyConnection.outConnectionForEach[ 
				dependencies
			].makeLazy[getArrowButton("Add dependency")]
			inverseDependencyConnection.inConnectionForEach[ 
				inverseDependencies
			].makeLazy[getInverseArrowButton("Add inverse dependency")]
		}
	}
	
	def getPluginNode() { pluginNode }
	
	val dependencyConnection = new ConnectionMapping<PluginDependency>(this, "dependencyConnection") {
		override calls() {
			pluginNode.target [
				dependency
			]
		}
		
		override createConnection(IMappedElementDescriptor<PluginDependency> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  
	
	def getDependencyConnection() { dependencyConnection }

	val inverseDependencyConnection = new ConnectionMapping<PluginDependency>(this, "inverseDependencyConnection") {
		override calls() {
			pluginNode.source [
				owner
			]
		}
		
		override createConnection(IMappedElementDescriptor<PluginDependency> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  

	def getInverseDependencyConnection() { inverseDependencyConnection }
	
	protected def createPluginImportConnection(IMappedElementDescriptor<PluginDependency> descriptor) {
		new BaseConnection(descriptor) => [ connection |
			val label = new XConnectionLabel(connection)
			descriptor.withDomainObject [
				if(!versionRange.empty)
			 		label.text.text = versionRange.toString
			 	if(optional) 
			 		connection.strokeDashArray.setAll(5.0, 5.0)
			 	if(!reexport)
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