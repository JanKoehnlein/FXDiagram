package de.fxdiagram.eclipse.xtext.mapping

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

abstract class AbstractXtextDiagramConfig extends AbstractEclipseDiagramConfig {
	
	override initialize(XDomainObjectShape shape) {
		shape.domainObjectDescriptorProperty?.addInitializingListener(new InitializingListener() => [
			set = [
				if(it instanceof AbstractXtextDescriptor<?>)
					injectMembers(shape)
			]
		])
		super.initialize(shape)
	}
	
	override protected createDomainObjectProvider() {
		new XtextDomainObjectProvider
	}
	
}