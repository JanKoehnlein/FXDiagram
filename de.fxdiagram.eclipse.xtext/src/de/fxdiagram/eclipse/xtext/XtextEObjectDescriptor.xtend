package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import java.util.NoSuchElementException
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.XtextEditor
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID

@ModelNode('elementID')
class XtextEObjectDescriptor <T extends EObject> extends AbstractXtextDescriptor<T> {

	@FxProperty(readOnly=true) XtextEObjectID elementID

	new(XtextEObjectID elementID, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(mappingConfigID, mappingID, provider)
		elementIDProperty.set(elementID)
	}
	
	override <U> withDomainObject((T)=>U lambda) {
		val editor = provider.getCachedEditor(elementID, false, false)
		if(editor instanceof XtextEditor) {
			editor.document.readOnly [ 
				val element = elementID.resolve(resourceSet)
				lambda.apply(element as T)
			]
		} else 
			throw new NoSuchElementException('Cannot open an Xtext editor for ' + elementID)
	}

	override openInEditor(boolean select) {
		provider.getCachedEditor(elementID, select, select)
	}
	
	override getName() {
		elementID.qualifiedName.lastSegment
	}
	
	override getId() {
		elementID.toString
	}
	
	override equals(Object obj) {
		if(obj instanceof XtextEObjectDescriptor<?>) 
			return super.equals(obj) && elementID == obj.elementID
		else
			return false
	}
	
	override hashCode() {
		super.hashCode() + 17 * elementID.hashCode
	}
	
	override protected getResourceServiceProvider() {
		elementID.resourceServiceProvider
	}
	
}
