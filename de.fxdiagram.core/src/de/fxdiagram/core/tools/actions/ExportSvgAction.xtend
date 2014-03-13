package de.fxdiagram.core.tools.actions

import com.google.common.base.Charsets
import com.google.common.io.Files
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.export.SvgExporter
import eu.hansolo.enzo.radialmenu.Symbol
import java.io.File
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class ExportSvgAction implements DiagramAction {
	
	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.E
	}
	
	override getSymbol() {
		Symbol.Type.CAMERA
	}

	override perform(XRoot root) {
		val svgCode = new SvgExporter().toSvg(root.diagram)
		Files.write(svgCode, new File("Diagram.svg"), Charsets.UTF_8)
	}
	
}