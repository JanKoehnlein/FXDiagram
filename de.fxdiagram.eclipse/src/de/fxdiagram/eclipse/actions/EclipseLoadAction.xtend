package de.fxdiagram.eclipse.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import de.fxdiagram.core.tools.actions.LoadAction
import java.io.FileReader
import javafx.stage.FileChooser
import org.eclipse.ui.PlatformUI
import de.fxdiagram.eclipse.FXDiagramView

class EclipseLoadAction extends LoadAction {
	
	override perform(XRoot root) {
		val fileChooser = new FileChooser()
		fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.fxd")
		val file = (fileChooser).showOpenDialog(root.scene.window)
		if(file != null) {
			val node = new ModelLoad().load(new FileReader(file))
			if(node instanceof XRoot) {
				root.replaceDomainObjectProviders(node.domainObjectProviders)
				root.rootDiagram = node.diagram
				val diagramView = PlatformUI.workbench.activeWorkbenchWindow.activePage.findView('de.fxdiagram.eclipse.FXDiagramView') as FXDiagramView
				diagramView.linkWithEditor = diagramView.linkWithEditor
			}
		}
	}
	
}