package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelSave
import eu.hansolo.enzo.radialmenu.Symbol
import java.io.FileWriter
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser

class SaveAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.S
	}
	
	override getSymbol() {
		Symbol.Type.ROCKET
	}

	override perform(XRoot root) {
		if(root.diagram != null) {
			val fileChooser = new FileChooser()
			fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FX Diagram", "*.fxd")
			val file = (fileChooser).showSaveDialog(root.scene.window)
			if(file != null) {
				new ModelSave().save(root, root.model, new FileWriter(file))
			}
		}
	}
	
}