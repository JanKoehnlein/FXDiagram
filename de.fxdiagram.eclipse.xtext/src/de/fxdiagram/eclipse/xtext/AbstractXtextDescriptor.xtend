package de.fxdiagram.eclipse.xtext

import com.google.inject.Injector
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import org.eclipse.xtext.resource.IResourceServiceProvider

@ModelNode
abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING> extends AbstractMappedElementDescriptor<ECLASS_OR_ESETTING> {

	new(String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(mappingConfigID, mappingID, provider)
	}
	
	def void injectMembers(Object object) {
		resourceServiceProvider.get(Injector).injectMembers(object)
	}

	abstract protected def IResourceServiceProvider getResourceServiceProvider()
	
	override XtextDomainObjectProvider getProvider() {
		super.provider as XtextDomainObjectProvider
	}
	
	override equals(Object obj) {
		return obj instanceof AbstractXtextDescriptor<?> && super.equals(obj)
	}
}