package de.fxdiagram.examples.lcars

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import javafx.scene.paint.Color

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@ModelNode(#['nodes', 'connections', 'parentDiagram'])
class LcarsDiagram extends XDiagram {

	new() {
		backgroundPaint = Color.BLACK
		foregroundPaint = Color.WHITE
		connectionPaint = Color.WHITE
	}
	
	override doActivate() {
		contentsInitializer = [
			val provider = root.getDomainObjectProvider(LcarsModelProvider)
			val kirk = provider.query('name', 'James T. Kirk').get(0)
			val handle = provider.createLcarsEntryDescriptor(kirk)
			nodes += new LcarsNode(handle) => [
				width = 120
			]
		]
		super.doActivate()
	}
	
}
