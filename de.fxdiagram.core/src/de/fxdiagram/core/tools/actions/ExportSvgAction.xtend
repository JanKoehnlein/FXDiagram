package de.fxdiagram.core.tools.actions

import com.google.common.base.Charsets
import com.google.common.io.Files
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.export.SvgExporter
import eu.hansolo.enzo.radialmenu.Symbol
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser

class ExportSvgAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.E
	}
	
	override getSymbol() {
		Symbol.Type.CAMERA
	}

	override getTooltip() {
		'Export to SVG'
	}
	
	override perform(XRoot root) {
		val fileChooser = new FileChooser()
		fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.svg")
		val file = (fileChooser).showSaveDialog(root.scene.window)
		if(file != null) {
			val svgCode = new SvgExporter().toSvg(root.diagram, file.parentFile)
			Files.write(svgCode, file, Charsets.UTF_8)
		}
	}
}