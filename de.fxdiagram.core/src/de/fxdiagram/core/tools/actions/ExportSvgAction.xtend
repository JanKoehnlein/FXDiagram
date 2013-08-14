package de.fxdiagram.core.tools.actions

import com.google.common.base.Charsets
import com.google.common.io.Files
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.export.SvgExporter
import java.io.File

class ExportSvgAction implements DiagramAction {
	
	override perform(XRoot root) {
		val svgCode = new SvgExporter().toSvg(root.diagram)
		Files.write(svgCode, new File("Diagram.svg"), Charsets.UTF_8)
	}
	
}