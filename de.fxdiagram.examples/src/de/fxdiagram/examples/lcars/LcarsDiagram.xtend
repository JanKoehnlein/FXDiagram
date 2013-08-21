package de.fxdiagram.examples.lcars

import de.fxdiagram.core.XDiagram
import javafx.scene.paint.Color

class LcarsDiagram extends XDiagram {
	
	LcarsAccess lcarsAccess
	
	new() {
		contentsInitializer = [
			lcarsAccess = new LcarsAccess
			val kirk = lcarsAccess.query('name', 'James T. Kirk').get(0)
			nodes += new LcarsNode(kirk) => [
				width = 120
			]
		]
		backgroundPaint = Color.BLACK
	}
	
	def getLcarsAccess() {
		lcarsAccess
	}
}

