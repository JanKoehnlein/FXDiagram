package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XShape
import de.fxdiagram.mapping.AbstractDiagramConfig
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior

abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
	
	override initialize(XShape shape) {
		shape.addBehavior(new OpenElementInEditorBehavior(shape))
	}
	
}