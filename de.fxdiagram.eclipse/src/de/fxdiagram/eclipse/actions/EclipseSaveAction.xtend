package de.fxdiagram.eclipse.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelSave
import de.fxdiagram.core.tools.actions.DiagramAction
import eu.hansolo.enzo.radialmenu.SymbolType
import java.io.File
import java.io.FileWriter
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.swt.widgets.Display
import org.eclipse.core.runtime.Path
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.NullProgressMonitor

class EclipseSaveAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.S
	}
	
	override getSymbol() {
		SymbolType.SAVE
	}
	
	override getTooltip() {
		'Save diagram'
	}

	override perform(XRoot root) {
		if(root.diagram != null) {
			Display.^default.asyncExec [
				val workspaceDir = ResourcesPlugin.workspace.root.location.toFile
				val file = if(root.fileName != null) {
					new File(workspaceDir, root.fileName)
				} else {
					val fileChooser = new FileChooser() => [
						extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.fxd")
						initialDirectory = workspaceDir
					]
					fileChooser.showSaveDialog(root.scene.window)
				} 
				if(file != null) {
					new ModelSave().save(root, new FileWriter(file))
					val fileName = file.absolutePath
					val workspaceDirName = workspaceDir.absolutePath
					if(fileName.startsWith(workspaceDirName + File.separator))  {
						root.fileName = fileName.substring(workspaceDirName.length)
						val iFile = ResourcesPlugin.workspace.root.getFile(new Path(root.fileName))
						iFile.refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor)
					} else {
						root.fileName = fileName
					}
					root.needsSave = false
				}
			]
		}
	}
}