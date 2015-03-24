package de.fxdiagram.pde

import de.fxdiagram.eclipse.FXDiagramView
import de.fxdiagram.lib.chooser.CoverFlowChoice
import de.fxdiagram.lib.chooser.NodeChooser
import de.fxdiagram.mapping.XDiagramConfig
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.MenuItem
import org.eclipse.swt.widgets.ToolItem
import org.eclipse.ui.PlatformUI

import static de.fxdiagram.pde.BundleUtil.*

import static extension de.fxdiagram.pde.HandlerHelper.*

class AddBundleHandler extends AbstractHandler {

	EventHandler<MouseEvent> mouseHandler = [
		if(clickCount == 2) {
			val view = diagramView
			if (view instanceof FXDiagramView) {
				val config = XDiagramConfig.Registry.getInstance.getConfigByID("de.fxdiagram.pde.BundleDiagramConfig") as BundleDiagramConfig
				val root = view.currentRoot
				val center = root.diagram.sceneToLocal(sceneX, sceneY)
				val nodeChooser = new NodeChooser(root.diagram, center, new CoverFlowChoice, false)
				allBundles.forEach[
					val descriptor = config.domainObjectProvider.createMappedElementDescriptor(it, config.pluginNode)
					nodeChooser.addChoice(config.pluginNode.createNode(descriptor))
				]
				root.currentTool = nodeChooser
			}
		}
	]

	override execute(ExecutionEvent event) throws ExecutionException {
		val view = diagramView
		if(view instanceof FXDiagramView) {
			if(event.isWidgetChecked) {
				view.addGlobalEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler)								
			} else {
				view.removeGlobalEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler)
			}
		}
		null
	}

	protected def getDiagramView() {
		val page = PlatformUI.workbench?.activeWorkbenchWindow?.activePage
		val view = page?.findView("de.fxdiagram.eclipse.FXDiagramView")
		return view
	}
}

class ConsiderOptionalDependeniesHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		BundleUtil.setConsiderOptional(event.isWidgetChecked)
	}
}

class ConsiderFragmentDependeniesHandler extends AbstractHandler {
	
	override execute(ExecutionEvent event) throws ExecutionException {
		BundleUtil.setConsiderFragments(event.isWidgetChecked)
	}
}

package class HandlerHelper {
	static def isWidgetChecked(ExecutionEvent event) {
		val widget = (event.trigger as Event).widget
		switch widget {
			MenuItem: widget.selection
			ToolItem: widget.selection
			default: false
		}
	}
}