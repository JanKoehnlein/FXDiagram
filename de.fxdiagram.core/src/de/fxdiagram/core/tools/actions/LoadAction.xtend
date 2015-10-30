package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelLoad
import eu.hansolo.enzo.radialmenu.SymbolType
import java.io.FileInputStream
import java.io.InputStreamReader
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser

class LoadAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.O
	}
	
	override getSymbol() {
		SymbolType.LOAD
	}
	
	override getTooltip() {
		'Load diagram'
	}

	override perform(XRoot root) {
		val fileChooser = new FileChooser()
		fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.fxd")
		val file = (fileChooser).showOpenDialog(root.scene.window)
		if(file != null) {
			val node = new ModelLoad().load(new InputStreamReader(new FileInputStream(file), 'UTF-8'))
			if(node instanceof XRoot) {
				root.replaceDomainObjectProviders(node.domainObjectProviders)
				root.rootDiagram = node.diagram
				root.fileName = file.path 
			}
		}
	}
}