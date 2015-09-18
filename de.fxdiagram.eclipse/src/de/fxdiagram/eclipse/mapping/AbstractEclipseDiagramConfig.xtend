package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior
import de.fxdiagram.mapping.execution.AbstractDiagramConfig

abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
	
	override initialize(XDomainObjectShape shape) {
		shape.addBehavior(new OpenElementInEditorBehavior(shape))
	}
	
}