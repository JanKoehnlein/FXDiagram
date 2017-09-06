package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException
import org.eclipse.xtext.resource.IEObjectDescription

@ModelNode("elementID")
class EObjectDescriptionDescriptor extends AbstractMappedElementDescriptor<IEObjectDescription> {
	
	@FxProperty(readOnly = true) XtextEObjectID elementID
	
	new(XtextEObjectID elementID, String mappingConfigId, String mappingId) {
		super(mappingConfigId, mappingId)
		this.elementIDProperty.set(elementID)
	}
	
	override XtextDomainObjectProvider getProvider() {
		super.provider as XtextDomainObjectProvider
	}
	
	override <U> withDomainObject((IEObjectDescription)=>U lambda) {
		val index = provider.getIndex(elementID)
		val description = elementID.findInIndex(index)
		if(description === null)
			throw new NoSuchElementException('Element ' + elementID + ' does not exist')
		lambda.apply(description)
	}
	
	override openInEditor(boolean select) {
		(provider as XtextDomainObjectProvider).getCachedEditor(elementID, true, true)
	}
	
	override getName() {
		elementID?.qualifiedName?.lastSegment
	}
	
	override equals(Object obj) {
		if(obj instanceof EObjectDescriptionDescriptor)
			return super.equals(obj) && obj.elementID == elementID 
		else
			return false
	}
	
	override hashCode() {
		super.hashCode + 131 * elementID.hashCode
	}
}