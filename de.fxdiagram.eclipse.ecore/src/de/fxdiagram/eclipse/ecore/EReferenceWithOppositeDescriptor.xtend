package de.fxdiagram.eclipse.ecore

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EReference

@ModelNode('uri', 'name', 'oppositeUri')
class EReferenceWithOppositeDescriptor extends AbstractMappedElementDescriptor<EReferenceWithOpposite> {

	@FxProperty(readOnly=true) val String uri
	@FxProperty(readOnly=true) val String oppositeUri
	@FxProperty(readOnly=true) val String name

	new(String uri, String name, String oppositeUri, String oppositeName, String mappingConfigID, String mappingID) {
		super(mappingConfigID, mappingID)
		this.oppositeUriProperty.set(uri)
		this.nameProperty.set(name + '-' + oppositeName)
		this.uriProperty.set(uri)
	}
	
	override EcoreDomainObjectProvider getProvider() {
		super.provider as EcoreDomainObjectProvider
	}
	
	override <U> withDomainObject((EReferenceWithOpposite)=>U lambda) {
		val element = provider.resolveEObject(uri)
		if(element instanceof EReference) 
			return lambda.apply(new EReferenceWithOpposite(element))
		else
			throw new NoSuchElementException('Cannot resolve EReference ' + uri)
	}
	
	override openInEditor(boolean select) {
		val theURI = URI.createURI(uri)
		provider.openEditor(theURI, select)
	}

	override equals(Object obj) {
		if (obj instanceof EReferenceWithOppositeDescriptor)
			return (obj.uri == uri && obj.oppositeUri == oppositeUri) 
			|| (obj.oppositeUri == uri && obj.uri == oppositeUri)
		else
			return false
	}

	override hashCode() {
		super.hashCode() + 563 * (uri.hashCode + oppositeUri?.hashCode) 
	}
}
