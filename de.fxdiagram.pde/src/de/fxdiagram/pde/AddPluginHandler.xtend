package de.fxdiagram.pde

import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.lib.chooser.NodeChooser
import de.fxdiagram.xtext.glue.FXDiagramView
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.ToolItem
import org.eclipse.ui.PlatformUI

import static de.fxdiagram.pde.PluginUtil.*

class AddPluginHandler extends AbstractHandler {

	EventHandler<MouseEvent> mouseHandler = [
		if(clickCount == 2) {
			val view = diagramView
			if (view instanceof FXDiagramView) {
				val config = XDiagramConfig.Registry.getInstance.getConfigByID("de.fxdiagram.pde.PluginDiagramConfig") as PluginDiagramConfig
				val nodeMapping = config.getMappingByID("pluginNode") as NodeMapping<IPluginModelBase>
				val root = view.root
				val center = root.diagram.sceneToLocal(sceneX, sceneY)
				val nodeChooser = new NodeChooser(root.diagram, center, new CoverFlowChoice, false)
				allPlugins.forEach[
					val descriptor = config.domainObjectProvider.createMappedElementDescriptor(it, nodeMapping)
					nodeChooser.addChoice(nodeMapping.createNode(descriptor))
				]
				root.currentTool = nodeChooser
			}
		}
	]

	override execute(ExecutionEvent event) throws ExecutionException {
		val selected = (((event.trigger as Event).widget) as ToolItem).selection
		val view = diagramView
		if(view instanceof FXDiagramView) {
			if(selected) {
				view.root.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler)								
			} else {
				view.root.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler)
			}
		}
		null
	}

	protected def getDiagramView() {
		val page = PlatformUI.workbench?.activeWorkbenchWindow?.activePage
		val view = page?.findView("org.eclipse.xtext.glue.FXDiagramView")
		return view
	}

}
