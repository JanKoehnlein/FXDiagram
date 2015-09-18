package de.fxdiagram.pde

import de.fxdiagram.core.anchors.CircleArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
import de.fxdiagram.mapping.ConnectionLabelMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.NodeHeadingMapping
import de.fxdiagram.mapping.NodeLabelMapping
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.execution.MappingAcceptor
import de.fxdiagram.mapping.shapes.BaseConnection
import de.fxdiagram.mapping.shapes.BaseNodeLabel
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import org.eclipse.osgi.service.resolver.BundleDescription

import static de.fxdiagram.pde.BundleNode.*

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
			pluginSymbolicName.labelFor[it]
			pluginVersion.labelFor[it]
			pluginName.labelFor[it]
			pluginProvider.labelFor[it]
			pluginExecutionEnvironment.labelFor[it]
			dependencyConnection.outConnectionForEach [
				bundleDependencies
			].asButton[getArrowButton("Add dependency")]
			inverseDependencyConnection.inConnectionForEach [
				inverseBundleDependencies
			].asButton[getInverseArrowButton("Add inverse dependency")]
		}
	}
	
	val pluginSymbolicName = new NodeHeadingMapping<BundleDescription>(this, BUNDLE_SYMBOLIC_NAME) {
		override createLabel(IMappedElementDescriptor<BundleDescription> descriptor, BundleDescription bundle) {
			super.createLabel(descriptor, bundle) => [
				text => [
					text = bundle.symbolicName 
					if(bundle.isSingleton) 
						font = Font.font(font.family, FontWeight.BOLD, FontPosture.ITALIC, font.size * 1.1)
					else 
						font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
				]
			]
		}
	} 
	
	val pluginVersion = new NodeLabelMapping<BundleDescription>(this, BUNDLE_VERSION) {
		override createLabel(IMappedElementDescriptor<BundleDescription> descriptor, BundleDescription bundle) {
			super.createLabel(descriptor, bundle) => [
				text => [
					text = bundle.version.toString 
					font = Font.font(font.family, font.size * 0.8)
				]
			]
		}
	} 
	
	val pluginName = new NodeLabelMapping<BundleDescription>(this, BUNDLE_NAME) {
		override createLabel(IMappedElementDescriptor<BundleDescription> descriptor, BundleDescription it) {
			new BaseNodeLabel(descriptor) => [
				text.text = (descriptor as BundleDescriptor).withPlugin [
					pluginBase.getResourceString(pluginBase.name)
				]
			]
		}
	}
	
	val pluginProvider = new NodeLabelMapping<BundleDescription>(this, BUNDLE_PROVIDER) {
		override createLabel(IMappedElementDescriptor<BundleDescription> descriptor, BundleDescription it) {
			new BaseNodeLabel(descriptor) => [
				text.text = (descriptor as BundleDescriptor).withPlugin [
					pluginBase.getResourceString(pluginBase.providerName)
				]
			]
		}
	} 
	
	val pluginExecutionEnvironment = new NodeLabelMapping<BundleDescription>(this, BUNDLE_EXECUTION_ENVIRONMENT) {
		override getText(BundleDescription bundle) {
			bundle.executionEnvironments.join(', ')
		}
	} 
	
	def getPluginNode() { pluginNode }
	
	val dependencyConnection = new ConnectionMapping<BundleDependency>(this, "dependencyConnection", "Plug-in dependency") {
		override calls() {
			versionRange.labelFor[it]
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
			versionRange.labelFor[it]
			pluginNode.source [
				owner
			]
		}
		
		override createConnection(IMappedElementDescriptor<BundleDependency> descriptor) {
			createPluginImportConnection(descriptor)
		}
	}  

	def getInverseDependencyConnection() { inverseDependencyConnection }
	
	val versionRange = new ConnectionLabelMapping<BundleDependency>(this, 'versionRange') {
		override getText(BundleDependency element) {
			if(element.versionRange.empty) 
				''
			else 
				element.versionRange.toString
		}
	}
	
	protected def createPluginImportConnection(IMappedElementDescriptor<BundleDependency> descriptor) {
		new BaseConnection(descriptor) => [ connection |
			descriptor.withDomainObject [
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