package de.fxdiagram.core.layout

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.FxProperty

@ModelNode('type', 'useSplines')
class LayoutParameters {
	@FxProperty LayoutType type = LayoutType.DOT
	@FxProperty boolean useSplines = true
}

enum LayoutType {
	DOT,
	NEATO
}
