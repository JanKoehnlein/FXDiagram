package de.fxdiagram.pde

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.buttons.RapidButtonAction
import de.fxdiagram.lib.chooser.CarusselChoice
import de.fxdiagram.lib.chooser.ConnectedNodeChooser
import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import org.eclipse.pde.core.plugin.IPluginImport
import org.eclipse.pde.core.plugin.IPluginModelBase

import static de.fxdiagram.core.layout.LayoutType.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.pde.PluginUtil.*

class AddImportPathAction extends RapidButtonAction {

	boolean isImport
	
	new(boolean isImport) {
		this.isImport = isImport 
	}

	override perform(RapidButton button) {
		val descriptor = button.host.domainObject as PluginDescriptor
		descriptor.withDomainObject [
			doPerform(button, it)
		]
	}
	
	def doPerform(RapidButton button, IPluginModelBase it) {
		val paths = 
			if(isImport) 
				allImportPaths
			else 
				allDependentsPaths
		val root = button.host.root
		val provider = root.getDomainObjectProvider(PluginDescriptorProvider)
		val diagramConfig = XDiagramConfig.Registry.getInstance.getConfigByID('de.fxdiagram.pde.PluginDiagramConfig')
		val pluginNodeMapping = diagramConfig.getMappingByID('pluginNode') as NodeMapping<IPluginModelBase>
		val importConnectionMapping = diagramConfig.getMappingByID('importConnection') as ConnectionMapping<IPluginImport>
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
						elements.last.plugin == chosenPlugin
					].forEach [ 
						path |
						var source = host
						for (pathElement : path.elements) {
							val midDescriptor = provider.createMappedElementDescriptor(pathElement.plugin, pluginNodeMapping) as PluginDescriptor
							var target = (diagram.nodes + additionalShapes + #[choice]).filter(XNode).findFirst[
								domainObject == midDescriptor
							]
							if (target == null) {
								target = pluginNodeMapping.createNode(midDescriptor)
								additionalShapes += target
							}
							val connectionDescriptor = provider.createMappedElementDescriptor(pathElement.pluginImport,
								importConnectionMapping)
							var connection = (diagram.connections + additionalShapes).filter(XConnection).findFirst[
								domainObject == connectionDescriptor
							]
							if (connection == null) {
								connection = importConnectionMapping.createConnection(connectionDescriptor)
								if(isImport) {
									connection.source = source
									connection.target = target
								} else {
									connection.source = target
									connection.target = source
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
		if(!isImport) {
			chooser.connectionProvider = [
				source, target, choiceInfo |
				new XConnection(target, source)
			]
		}
		paths.map[elements.last.plugin].toSet().sortBy[pluginBase.id].forEach [
			val leafDescriptor = provider.createMappedElementDescriptor(it, pluginNodeMapping) as PluginDescriptor
			chooser.addChoice(pluginNodeMapping.createNode(leafDescriptor))
		]
		root.currentTool = chooser
		null
	}

}
