package de.fxdiagram.eclipse.xtext.mapping

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider

abstract class AbstractXtextDiagramConfig extends AbstractEclipseDiagramConfig {
	
	override initialize(XShape shape) {
		val domainObjectProperty = switch shape {
			XNode: shape.domainObjectProperty
			XConnection: shape.domainObjectProperty
		}
		domainObjectProperty?.addInitializingListener(new InitializingListener() => [
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