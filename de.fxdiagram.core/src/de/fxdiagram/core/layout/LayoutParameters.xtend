package de.fxdiagram.core.layout

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode

import static de.fxdiagram.core.XConnection.Kind.*
import de.fxdiagram.core.XConnection

@ModelNode('type', 'connectionKind')
class LayoutParameters {
	@FxProperty LayoutType type = LayoutType.DOT
	@FxProperty XConnection.Kind connectionKind = CUBIC_CURVE
}

enum LayoutType {
	DOT,
	NEATO
}
