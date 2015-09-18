package de.fxdiagram.mapping.shapes

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.execution.XDiagramConfig

/**
 * Call {@link XDiagramConfig#initialize()} on load, as soon as the 
 * {@link XDomainObjectShape#domainObjectDescriptor} is set.
 */
class BaseShapeInitializer {
	def static initializeLazily(XDomainObjectShape shape) {
		shape.domainObjectDescriptorProperty.addListener [ p, o, newDescriptor |
			if (newDescriptor instanceof IMappedElementDescriptor<?>) {
				newDescriptor?.mapping?.config?.initialize(shape)
			}
			p.removeListener(self)
		]
	}
}