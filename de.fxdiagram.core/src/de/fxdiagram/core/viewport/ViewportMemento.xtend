package de.fxdiagram.core.viewport

import org.eclipse.xtend.lib.annotations.Data

@Data
class ViewportMemento {
	double translateX
	double translateY
	double scale
	double rotate
	
	override toString() {
		'''ViewportMemento [translateX=«translateX», translateY=«translateY», scale=«scale», rotate=«rotate»]'''
	}
}