package de.fxdiagram.eclipse.mapping

import de.fxdiagram.core.XDomainObjectOwner
import de.fxdiagram.mapping.AbstractDiagramConfig
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior
import de.fxdiagram.core.XDomainObjectShape

abstract class AbstractEclipseDiagramConfig extends AbstractDiagramConfig {
	
	override initialize(XDomainObjectOwner shape) {
		if (shape instanceof XDomainObjectShape) 
			shape.addBehavior(new OpenElementInEditorBehavior(shape))
	}
	
}