package de.fxdiagram.eclipse.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import de.fxdiagram.core.tools.actions.LoadAction
import java.io.File
import java.io.FileReader
import javafx.stage.FileChooser
import org.eclipse.core.resources.ResourcesPlugin

class EclipseLoadAction extends LoadAction {
	
	override perform(XRoot root) {
		val workspaceDir = ResourcesPlugin.workspace.root.location.toFile
		val fileChooser = new FileChooser() => [
			title = 'Load diagram'
			extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.fxd")
			initialDirectory = workspaceDir
		]
		val file = fileChooser.showOpenDialog(root.scene.window)
		if(file != null) {
			val node = new ModelLoad().load(new FileReader(file))
			if(node instanceof XRoot) {
				root.replaceDomainObjectProviders(node.domainObjectProviders)
				root.rootDiagram = node.diagram
				val fileName = file.absolutePath
				val workspaceDirName = workspaceDir.absolutePath
				if(fileName.startsWith(workspaceDirName + File.separator)) 
					root.fileName = fileName.substring(workspaceDirName.length)
				else
					root.fileName = fileName
			}
		}
	}
	
}