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
import org.eclipse.osgi.service.resolver.BundleDescription
import static extension de.fxdiagram.pde.BundleUtil.*

class BundleDiagramConfig extends AbstractDiagramConfig {

	override protected createDomainObjectProvider() {
		new BundleDescriptorProvider
	}
	
	val pluginNode = new NodeMapping<BundleDescription>(this, "pluginNode") {
		
		override createNode(IMappedElementDescriptor<BundleDescription> descriptor) {
			new BundleNode(descriptor as BundleDescriptor)
		}
		
		override calls() {
			dependencyConnection.outConnectionForEach [
				bundleDependencies
			].makeLazy[getArrowButton("Add dependency")]
			inverseDependencyConnection.inConnectionForEach [
				inverseBundleDependencies
			].makeLazy[getInverseArrowButton("Add inverse dependency")]
		}
	}
	
	def getPluginNode() { pluginNode }
	
	val dependencyConnection = new ConnectionMapping<BundleDependency>(this, "dependencyConnection") {
		override calls() {
			pluginNode.target [
				dependency
			]
		}
		
		override createConnection(IMappedElementDescriptor<BundleDependency> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  
	
	def getDependencyConnection() { dependencyConnection }

	val inverseDependencyConnection = new ConnectionMapping<BundleDependency>(this, "inverseDependencyConnection") {
		override calls() {
			pluginNode.source [
				owner
			]
		}
		
		override createConnection(IMappedElementDescriptor<BundleDependency> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  

	def getInverseDependencyConnection() { inverseDependencyConnection }
	
	protected def createPluginImportConnection(IMappedElementDescriptor<BundleDependency> descriptor) {
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