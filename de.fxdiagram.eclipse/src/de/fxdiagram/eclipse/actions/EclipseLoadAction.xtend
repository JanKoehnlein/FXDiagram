package de.fxdiagram.eclipse.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import de.fxdiagram.core.tools.actions.LoadAction
import java.io.InputStreamReader
import javafx.stage.FileChooser
import org.eclipse.core.resources.ResourcesPlugin

import static extension de.fxdiagram.eclipse.actions.FileExtensions.*

class EclipseLoadAction extends LoadAction {
	
	override perform(XRoot root) {
		val workspaceDir = ResourcesPlugin.workspace.root.location.toFile
		val fileChooser = new FileChooser() => [
			title = 'Load diagram'
			extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.fxd")
			initialDirectory = workspaceDir
		]
		val file = fileChooser.showOpenDialog(root.scene.window).toWorkspaceFile
		if(file != null) {
			val node = new ModelLoad().load(new InputStreamReader(file.contents))
			if(node instanceof XRoot) {
				root.replaceDomainObjectProviders(node.domainObjectProviders)
				root.rootDiagram = node.diagram
				root.fileName = file.fullPath.toOSString
			}
		}
	}
}