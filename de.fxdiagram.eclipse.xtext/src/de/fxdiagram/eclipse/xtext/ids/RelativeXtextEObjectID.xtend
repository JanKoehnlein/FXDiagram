package de.fxdiagram.eclipse.xtext.ids

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.ResourceSet
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
@ModelNode('parentID', 'relativeFragment')
class RelativeXtextEObjectID extends AbstractXtextEObjectID {

	@FxProperty(readOnly=true) XtextEObjectID parentID
	@FxProperty(readOnly=true) String relativeFragment

	new(XtextEObjectID parentID, EClass eClass, URI elementURI, String relativeFragment) {
		super(eClass, elementURI)
		this.parentIDProperty.set(parentID)
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

	override resolve(ResourceSet resourceSet) {
		val parent = parentID.resolve(resourceSet)
		val elementFragment = if (relativeFragment.startsWith('#'))
			relativeFragment
		else
			parent.URI.fragment + relativeFragment
		val element = parent.eResource.getEObject(elementFragment)
		if (!EClass.isInstance(element))
			throw new NoSuchElementException('Could not resolve element ' + toString)
		return element
	}
}