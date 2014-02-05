package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import java.io.FileReader
import javafx.stage.FileChooser

class LoadAction implements DiagramAction {

	override perform(XRoot root) {
		val fileChooser = new FileChooser()
		fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FX Diagram", "*.fxd")
		val file = (fileChooser).showOpenDialog(root.scene.window)
		if(file != null) {
			val node = new ModelLoad().load(new FileReader(file))
			if(node instanceof XRoot) {
				root.domainObjectProviders.setAll(node.domainObjectProviders)
				root.diagram = node.diagram
			}
		}
	}
	
}