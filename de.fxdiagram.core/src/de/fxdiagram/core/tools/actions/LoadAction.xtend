package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import eu.hansolo.enzo.radialmenu.Symbol
import java.io.FileReader
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser

class LoadAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.O
	}
	
	override getSymbol() {
		Symbol.Type.CLOUD
	}
	
	override getTooltip() {
		'Load diagram'
	}

	override perform(XRoot root) {
		val fileChooser = new FileChooser()
		fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FX Diagram", "*.fxd")
		val file = (fileChooser).showOpenDialog(root.scene.window)
		if(file != null) {
			val node = new ModelLoad().load(new FileReader(file))
			if(node instanceof XRoot) {
				root.replaceDomainObjectProviders(node.domainObjectProviders)
				root.diagram = node.diagram
			}
		}
	}
	
}