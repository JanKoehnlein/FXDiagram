package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID
import de.fxdiagram.mapping.AbstractMappedElementDescriptor
import java.util.NoSuchElementException
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider
import org.eclipse.xtext.ui.refactoring.impl.ProjectUtil
import org.eclipse.xtext.ui.resource.IResourceSetProvider

@ModelNode("elementID")
class EObjectDescriptionDescriptor extends AbstractMappedElementDescriptor<IEObjectDescription> {
	
	@FxProperty(readOnly = true) XtextEObjectID elementID
	
	new(XtextEObjectID elementID, String mappingConfigId, String mappingId, XtextDomainObjectProvider provider) {
		super(mappingConfigId, mappingId, provider)
		this.elementIDProperty.set(elementID)
	}
	
	override <U> withDomainObject((IEObjectDescription)=>U lambda) {
		val rsp = elementID.resourceServiceProvider
		val project = rsp.get(ProjectUtil).getProject(elementID.URI)
		if(project == null)
			throw new NoSuchElementException('Project ' + elementID.URI + ' does not exist')
		val resourceSet = rsp.get(IResourceSetProvider).get(project)
		resourceSet.loadOptions.put(ResourceDescriptionsProvider.PERSISTED_DESCRIPTIONS, true)
		val index = rsp.get(ResourceDescriptionsProvider).getResourceDescriptions(resourceSet)
		val description = index.getExportedObjects(elementID.EClass, elementID.qualifiedName, false).findFirst[EObjectURI == elementID.URI]
		if(description == null)
			throw new NoSuchElementException('Element ' + elementID + ' does not exist')
		lambda.apply(description)
	}
	
	override openInEditor(boolean select) {
		(provider as XtextDomainObjectProvider).getCachedEditor(elementID, true, true)
	}
	
	override getName() {
		elementID.qualifiedName.lastSegment
	}
	
	override equals(Object obj) {
		if(obj instanceof EObjectDescriptionDescriptor)
			return super.equals(obj) && obj.elementID == elementID && obj.elementID.URI == elementID.URI
		else
			return false
	}
	
	override hashCode() {
		super.hashCode + 131 * elementID.hashCode + 177 * elementID.URI.hashCode
	}
}