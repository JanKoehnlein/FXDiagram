package de.fxdiagram.core.export

/**
 * Implement if you want to fine tune the SVG export of a node.
 */
interface SvgExportable {
	
	def CharSequence toSvgElement(@Extension SvgExporter exporter)
}