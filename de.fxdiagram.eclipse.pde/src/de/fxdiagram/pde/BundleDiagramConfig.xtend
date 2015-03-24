package de.fxdiagram.pde

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.CircleArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.MappingAcceptor
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.shapes.BaseConnection
import org.eclipse.osgi.service.resolver.BundleDescription

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*
import static extension de.fxdiagram.pde.BundleUtil.*

class BundleDiagramConfig extends AbstractEclipseDiagramConfig {

	override protected createDomainObjectProvider() {
		new BundleDescriptorProvider
	}
	
	val pluginNode = new NodeMapping<BundleDescription>(this, "pluginNode", "Plug-in") {
		
		override createNode(IMappedElementDescriptor<BundleDescription> descriptor) {
			new BundleNode(descriptor as BundleDescriptor)
		}
		
		override calls() {
			dependencyConnection.outConnectionForEach [
				bundleDependencies
			].asButton[getArrowButton("Add dependency")]
			inverseDependencyConnection.inConnectionForEach [
				inverseBundleDependencies
			].asButton[getInverseArrowButton("Add inverse dependency")]
		}
	}
	
	def getPluginNode() { pluginNode }
	
	val dependencyConnection = new ConnectionMapping<BundleDependency>(this, "dependencyConnection", "Plug-in dependency") {
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

	val inverseDependencyConnection = new ConnectionMapping<BundleDependency>(this, "inverseDependencyConnection", "Inverse Plug-in dependency") {
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
			 	if(kind == BundleDependency.Kind.FRAGMENT_HOST) 
			 		connection.sourceArrowHead = new CircleArrowHead(connection, true)
			 	null
			]
		]
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			BundleDescription: 
				add(pluginNode)
		}
	}	
	
}