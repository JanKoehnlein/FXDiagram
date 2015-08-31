package de.fxdiagram.eclipse.xtext.ids

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.NoSuchElementException
import javafx.collections.ObservableList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.naming.QualifiedName

import static javafx.collections.FXCollections.*

@ModelNode('nameSegments')
class DefaultXtextEObjectID extends AbstractXtextEObjectID {

	QualifiedName qualifiedName

	@FxProperty(readOnly=true) ObservableList<String> nameSegments = observableArrayList

	new(QualifiedName qualifiedName, EClass eClass, URI elementURI) {
		super(eClass, elementURI)
		this.qualifiedName = qualifiedName
		this.nameSegmentsProperty.setAll(qualifiedName.segments)
	}
	
	override getQualifiedName() {
		qualifiedName ?: {
			if(nameSegments.empty) null else qualifiedName = QualifiedName.create(nameSegments)
		}
	}

	override equals(Object obj) {
		if (obj instanceof DefaultXtextEObjectID)
			return super.equals(obj) &&  obj.qualifiedName == qualifiedName
		else
			return false
	}

	override hashCode() {
		super.hashCode + getQualifiedName.hashCode * 101 
	}

	override toString() {
		qualifiedName.toString + '||' + uriAsString
	}

	override resolve(ResourceSet resourceSet) {
		val resourceURI = getURI.trimFragment
		val resource = resourceSet.getResource(resourceURI, true)
		if (resource == null)
			throw new NoSuchElementException('Cannot load resource ' + resourceURI)
		val resourceDescription = resourceServiceProvider.resourceDescriptionManager.
			getResourceDescription(resource)
		val eObjectDescriptions = resourceDescription.getExportedObjects(EClass, qualifiedName, false)
		if (eObjectDescriptions.size != 1)
			throw new NoSuchElementException('Expected a single element but got ' + eObjectDescriptions.size)
		val eObjectDescription = eObjectDescriptions.head
		val element = EcoreUtil.resolve(eObjectDescription.EObjectOrProxy, resource)
		if (element == null)
			throw new NoSuchElementException('Cannot resolve element ' + eObjectDescription.EObjectURI)
		return element
	}

}