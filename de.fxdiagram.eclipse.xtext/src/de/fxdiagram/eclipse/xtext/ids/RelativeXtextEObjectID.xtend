package de.fxdiagram.eclipse.xtext.ids

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.ResourceSet
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
import org.eclipse.xtext.resource.IResourceDescriptions

@ModelNode('parentID', 'relativeFragment')
class RelativeXtextEObjectID extends AbstractXtextEObjectID {

	@FxProperty(readOnly=true) XtextEObjectID parentID
	@FxProperty(readOnly=true) String relativeFragment

	new(XtextEObjectID parentID, EClass eClass, URI elementURI) {
		super(eClass, elementURI)
		this.parentIDProperty.set(parentID)
		val fragment = URI.fragment
		val parentFragment = parentID.URI.fragment
		val relativeFragment = if (fragment.startsWith(parentFragment))
				fragment.substring(parentFragment.length)
			else
				'#' + fragment
		this.relativeFragmentProperty.set(relativeFragment)
	}

	override getQualifiedName() {
		null
	}

	override equals(Object obj) {
		if (obj instanceof RelativeXtextEObjectID)
			return super.equals(obj) 
				&& obj.parentID == parentID
				&& obj.relativeFragment == relativeFragment
		else
			return false
	}

	override hashCode() {
		461 * parentID.hashCode  + 503 * relativeFragment.hashCode + 101 * super.hashCode
	}

	override toString() {
		parentID.toString '->' + relativeFragment
	}

	override findInIndex(IResourceDescriptions index) {
		val resourceDescription = index.getResourceDescription(URI.trimFragment)
		resourceDescription.getExportedObjectsByType(EClass).findFirst[URI == EObjectURI]
	}
	
	override resolve(ResourceSet resourceSet) {
		val parent = parentID.resolve(resourceSet)
		val elementFragment = if (relativeFragment.startsWith('#'))
			relativeFragment
		else
			parent.URI.fragment + relativeFragment
		val element = parent.eResource.getEObject(elementFragment)
		if (element == null || element.eIsProxy)
			throw new NoSuchElementException('Could not resolve element ' + toString)
		if(!EClass.isInstance(element))
			throw new NoSuchElementException('Expected ' + EClass.name + ' but got ' + element.eClass.name)
		return element
	}
}