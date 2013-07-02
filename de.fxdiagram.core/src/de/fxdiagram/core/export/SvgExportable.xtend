package de.fxdiagram.core.export

interface SvgExportable {
	
	def CharSequence toSvgElement(@Extension SvgExporter exporter)
}