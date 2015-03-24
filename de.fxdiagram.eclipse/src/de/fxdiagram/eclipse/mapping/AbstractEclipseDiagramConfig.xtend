package de.fxdiagram.eclipse.mapping

import de.fxdiagram.mapping.AbstractDiagramConfig
import de.fxdiagram.core.XShape
import de.fxdiagram.eclipse.behavior.OpenElementInEditorBehavior

abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
	
	override initialize(XShape shape) {
		shape.addBehavior(new OpenElementInEditorBehavior(shape))
	}
	
}