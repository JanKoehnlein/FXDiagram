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
import org.eclipse.pde.core.plugin.IPluginModelBase

import static de.fxdiagram.core.layout.LayoutType.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.pde.PluginUtil.*

class AddDependencyPathAction extends RapidButtonAction {

	boolean isInverse
	
	new(boolean isInverse) {
		this.isInverse = isInverse 
	}

	override perform(RapidButton button) {
		val descriptor = button.host.domainObject as PluginDescriptor
		descriptor.withDomainObject [
			doPerform(button, it)
		]
	}
	
	def doPerform(RapidButton button, IPluginModelBase it) {
		val paths = 
			if(isInverse) 
				allInverseDependencies
			else 
				allDependencies
		val root = button.host.root
		val provider = root.getDomainObjectProvider(PluginDescriptorProvider)
		val config = XDiagramConfig.Registry.getInstance.getConfigByID('de.fxdiagram.pde.PluginDiagramConfig') as PluginDiagramConfig
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
				(choice.domainObject as PluginDescriptor).withDomainObject [
					chosenPlugin |
					paths.filter [
						elements.last.owner == chosenPlugin
					].forEach [ 
						path |
						var source = host
						for (pathElement : path.elements) {
							val targetPlugin = 
								if(isInverse) 
									pathElement.owner
								else
									pathElement.dependency
							val midDescriptor = provider.createMappedElementDescriptor(targetPlugin, config.pluginNode) as PluginDescriptor
							var target = (diagram.nodes + additionalShapes + #[choice]).filter(XNode).findFirst[
								domainObject == midDescriptor
							]
							if (target == null) {
								target = config.pluginNode.createNode(midDescriptor)
								additionalShapes += target
							}
							val connectionDescriptor = provider.createMappedElementDescriptor(pathElement,
								config.dependencyConnection)
							var connection = (diagram.connections + additionalShapes).filter(XConnection).findFirst[
								domainObject == connectionDescriptor
							]
							if (connection == null) {
								connection = config.dependencyConnection.createConnection(connectionDescriptor)
								if(isInverse) {
									connection.source = target
									connection.target = source
								} else {
									connection.source = source
									connection.target = target
								}
								additionalShapes += connection
							}
							source = target
						}
					]
					null
				]
				return additionalShapes
			}

			override protected nodeChosen(XNode choice) {
				super.nodeChosen(choice)
				if (choice != null)
					host.root.commandStack.execute(
						new Layouter().createLayoutCommand(DOT, host.root.diagram, 500.millis))
			}
		}
		if(isInverse) {
			chooser.connectionProvider = [
				source, target, choiceInfo |
				new XConnection(target, source)
			]
		}
		paths.map[elements.last.owner].toSet().sortBy[pluginBase.id].forEach [
			val leafDescriptor = provider.createMappedElementDescriptor(it, config.pluginNode) as PluginDescriptor
			chooser.addChoice(config.pluginNode.createNode(leafDescriptor))
		]
		root.currentTool = chooser
		null
	}

}
