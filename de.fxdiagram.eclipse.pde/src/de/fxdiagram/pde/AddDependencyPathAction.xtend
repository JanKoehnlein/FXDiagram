package de.fxdiagram.pde

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.eclipse.mapping.XDiagramConfig
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.CarusselChoice
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.chooser.CoverFlowChoice
import org.eclipse.osgi.service.resolver.BundleDescription

import static de.fxdiagram.core.layout.LayoutType.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.pde.BundleUtil.*

class AddDependencyPathAction extends RapidButtonAction {

	boolean isInverse
	
	new(boolean isInverse) {
		this.isInverse = isInverse 
	}

	override isEnabled(XNode host) {
		(host.domainObject as BundleDescriptor).withDomainObject[
			if(isInverse) 
				!dependentBundles.empty
			else 
				!dependencyBundles.empty
		]
	}
	
	override perform(RapidButton button) {
		val descriptor = button.host.domainObject as BundleDescriptor
		descriptor.withDomainObject [
			doPerform(button, it)
		]
	}
	
	def doPerform(RapidButton button, BundleDescription hostBundle) {
		val root = button.host.root
		val provider = root.getDomainObjectProvider(BundleDescriptorProvider)
		val config = XDiagramConfig.Registry.getInstance.getConfigByID('de.fxdiagram.pde.BundleDiagramConfig') as BundleDiagramConfig
		val host = button.host
		val choiceGraphics = if (button.position.vertical)
				new CarusselChoice
			else
				new CoverFlowChoice
		val chooser = new ConnectedNodeChooser(host, button.position, choiceGraphics) {
			override getAdditionalShapesToAdd(XNode choice, DomainObjectDescriptor choiceInfo) {
				// throw away direct connection
				for (it : super.getAdditionalShapesToAdd(choice, choiceInfo).filter(XConnection))
					removeConnection
				val diagram = host.diagram
				val additionalShapes = <XShape>newLinkedHashSet()
				val descriptor2node = diagram.nodes.toMap[domainObject]
				descriptor2node.put(choice.domainObject, choice)
				val descriptor2connection = diagram.connections.toMap[domainObject] 
				(choice.domainObject as BundleDescriptor).withDomainObject [
					chosenBundle |
					if (isInverse)
						chosenBundle.getAllBundleDependencies(hostBundle)
					else
						hostBundle.getAllBundleDependencies(chosenBundle)
				].forEach [ bundleDependency |
					val owner = provider.createMappedElementDescriptor(bundleDependency.owner, config.pluginNode) as BundleDescriptor
					var sourceNode = descriptor2node.get(owner)
					if (sourceNode == null) {
						sourceNode = config.pluginNode.createNode(owner)
						additionalShapes += sourceNode
						descriptor2node.put(owner, sourceNode)
					}
					val dependency = provider.createMappedElementDescriptor(bundleDependency.dependency, config.pluginNode) as BundleDescriptor
					var targetNode = descriptor2node.get(dependency)
					if (targetNode == null) {
						targetNode = config.pluginNode.createNode(dependency)
						additionalShapes += targetNode
						descriptor2node.put(dependency, targetNode)
					}
					val connectionDescriptor = provider.createMappedElementDescriptor(bundleDependency,
						config.dependencyConnection)
					var connection = descriptor2connection.get(connectionDescriptor)
					if (connection == null) {
						connection = config.dependencyConnection.createConnection(connectionDescriptor)
						connection.source = sourceNode
						connection.target = targetNode
						additionalShapes += connection
						descriptor2connection.put(connectionDescriptor, connection)
					}
				]
				return additionalShapes
			}

			override protected nodeChosen(XNode choice) {
				super.nodeChosen(choice)
				if (choice != null)
					root.commandStack.execute(
						new Layouter().createLayoutCommand(DOT, host.root.diagram, 500.millis))
			}
		}
		if(isInverse) {
			chooser.connectionProvider = [
				source, target, choiceInfo |
				new XConnection(target, source)
			]
		}
		
		val candidates = 
			if(isInverse) 
				hostBundle.allDependentBundles
			else 
				hostBundle.allDependencyBundles
		candidates.forEach [
			val candidate = provider.createMappedElementDescriptor(it, config.pluginNode) as BundleDescriptor
			chooser.addChoice(config.pluginNode.createNode(candidate))
		]		
		root.currentTool = chooser
		null
	}

}
