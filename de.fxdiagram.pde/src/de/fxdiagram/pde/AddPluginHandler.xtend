package de.fxdiagram.pde

import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.lib.chooser.NodeChooser
import de.fxdiagram.xtext.glue.FXDiagramView
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.ui.PlatformUI

import static de.fxdiagram.pde.PluginUtil.*

class AddPluginHandler extends AbstractHandler {

	override execute(ExecutionEvent event) throws ExecutionException {
		val page = PlatformUI.workbench.activeWorkbenchWindow.activePage
		val view = page.findView("org.eclipse.xtext.glue.FXDiagramView")
		if (view instanceof FXDiagramView) {
			val config = XDiagramConfig.Registry.getInstance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig") as PluginDiagramConfig
			val nodeMapping = config.getMappingByID("pluginNode") as NodeMapping<IPluginModelBase>
			val root = view.root
			val center = root.diagram.sceneToLocal(0.5 * root.scene.width, 0.5 * root.scene.height)
			val nodeChooser = new NodeChooser(root.diagram, center, new CoverFlowChoice, false)
			allPlugins.forEach[
				val descriptor = config.domainObjectProvider.createMappedElementDescriptor(it, nodeMapping)
				nodeChooser.addChoice(nodeMapping.createNode(descriptor))
			]
			root.currentTool = nodeChooser
		}
		null
	}

}
