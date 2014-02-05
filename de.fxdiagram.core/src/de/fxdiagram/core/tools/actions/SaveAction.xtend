package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelSave
import java.io.FileWriter
import javafx.stage.FileChooser

class SaveAction implements DiagramAction {

	override perform(XRoot root) {
		if(root.diagram != null) {
			val fileChooser = new FileChooser()
			fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FX Diagram", "*.fxd")
			val file = (fileChooser).showSaveDialog(root.scene.window)
			if(file != null) {
				new ModelSave().save(root, new FileWriter(file))
			}
		}
	}
	
}