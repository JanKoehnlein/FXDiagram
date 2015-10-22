package de.fxdiagram.eclipse.xtext.mapping

import com.google.inject.Inject
import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider
import org.eclipse.core.resources.IFile
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.resource.IStorage2UriMapper

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

abstract class AbstractXtextDiagramConfig extends AbstractEclipseDiagramConfig {
	
	@Inject extension IStorage2UriMapper
	 
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
	
	def getFilePath(EObject element) {
		(element.eResource.URI.storages.head.first as IFile)
			.fullPath.removeFileExtension.toString + '.fxd'
	}
}