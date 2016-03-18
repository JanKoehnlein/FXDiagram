package de.fxdiagram.eclipse.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelSave
import de.fxdiagram.core.tools.actions.DiagramAction
import eu.hansolo.enzo.radialmenu.SymbolType
import java.io.ByteArrayInputStream
import java.io.StringWriter
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.dialogs.SaveAsDialog

import static extension de.fxdiagram.eclipse.actions.FileExtensions.*

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
				doSave(root)			
			]
		}
	}
	
	/**
	 * Must be run in Display thread
	 */
	def doSave(XRoot root) {
		val workspaceDir = ResourcesPlugin.workspace.root
		val file = if(root.fileName != null) {
			workspaceDir.getFile(new Path(root.fileName))
		} else {
			val saveAsDialog = new SaveAsDialog(Display.^default.activeShell)
			saveAsDialog.originalName = 'Diagram.fxd'
			val result = saveAsDialog.open
			if(result == SaveAsDialog.OK) {
				workspaceDir.getFile(saveAsDialog.result)
			} else {
				null
			}
		} 
		if(file != null) {
			file.createParents
			val writer = new StringWriter
			new ModelSave().save(root, writer)
			val stream = new ByteArrayInputStream(writer.toString.getBytes(file.getCharset(true)))
			if(file.exists)
				file.setContents(stream, true, true, new NullProgressMonitor)
			else
				file.create(stream, true, new NullProgressMonitor)
			root.fileName = file.fullPath.toOSString
			root.needsSave = false
		}
	}
}