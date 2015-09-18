package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.mapping.AbstractDiagramConfig
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior

abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
	
	override initialize(XDomainObjectShape shape) {
		shape.addBehavior(new OpenElementInEditorBehavior(shape))
	}
	
}