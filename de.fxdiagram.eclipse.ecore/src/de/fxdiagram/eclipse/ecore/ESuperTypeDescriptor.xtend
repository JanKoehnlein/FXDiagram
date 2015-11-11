package de.fxdiagram.eclipse.ecore

import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import java.util.NoSuchElementException

@ModelNode('subTypeUri', 'superTypeUri', 'name')
class ESuperTypeDescriptor extends AbstractMappedElementDescriptor<ESuperType> {

	@FxProperty(readOnly=true) val String subTypeUri
	@FxProperty(readOnly=true) val String superTypeUri
	@FxProperty(readOnly=true) val String name

	new(String subTypeUri, String subTypeName, String superTypeUri, String superTypeName ,String mappingConfigID, String mappingID) {
		super(mappingConfigID, mappingID)
		this.subTypeUriProperty.set(subTypeUri)
		this.superTypeUriProperty.set(superTypeUri)
		this.nameProperty.set(subTypeName + '->' + superTypeName)
	}
	
	override EcoreDomainObjectProvider getProvider() {
		super.provider as EcoreDomainObjectProvider
	}
	
	override <U> withDomainObject((ESuperType)=>U lambda) {
		val subType = provider.resolveEObject(subTypeUri)
		val superType = provider.resolveEObject(superTypeUri)
		if(subType instanceof EClass) {
			if(superType instanceof EClass) {
				if(superType.isSuperTypeOf(subType)) {
					val eSuperType = new ESuperType(subType, superType)
					return lambda.apply(eSuperType)
				}
			}
		} 
		throw new NoSuchElementException('Cannot resolve ESupertType ' + subTypeUri + '->' + superTypeUri)
	}
	
	override openInEditor(boolean select) {
		val theURI = URI.createURI(subTypeUri)
		provider.openEditor(theURI, select)
	}

	override equals(Object obj) {
		if (obj instanceof ESuperTypeDescriptor)
			return obj.subTypeUri == subTypeUri && obj.superTypeUri == superTypeUri
		else
			return false
	}

	override hashCode() {
		super.hashCode() + 563 * subTypeUri.hashCode + 547 * superTypeUri.hashCode
	}
}