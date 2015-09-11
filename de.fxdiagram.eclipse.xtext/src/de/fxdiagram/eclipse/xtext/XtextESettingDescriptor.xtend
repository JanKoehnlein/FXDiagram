package de.fxdiagram.eclipse.xtext

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID
import java.util.NoSuchElementException
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.ui.editor.XtextEditor

import static extension org.eclipse.emf.common.util.URI.*
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

@ModelNode('sourceID', 'targetID', 'eReferenceURI', 'index')
class XtextESettingDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ESetting<ECLASS>> {
	
	@FxProperty(readOnly=true) XtextEObjectID sourceID
	@FxProperty(readOnly=true) XtextEObjectID targetID
	@FxProperty(readOnly=true) String eReferenceURI
	@FxProperty(readOnly=true) int index
	
	EReference eReference

	new(XtextEObjectID sourceID, XtextEObjectID targetID, EReference reference, int index, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(mappingConfigID, mappingID, provider)
		sourceIDProperty.set(sourceID)
		targetIDProperty.set(targetID)
		this.eReference = reference
		eReferenceURIProperty.set(reference.URI.toString) 
		indexProperty.set(index)
	}
	
	override <U> withDomainObject((ESetting<ECLASS>)=>U lambda) {
		val editor = provider.getCachedEditor(sourceID, false, false)
		if(editor instanceof XtextEditor) {
			editor.document.readOnly [ 
				val source = sourceID.resolve(resourceSet)
				val storedTarget = targetID.resolve(resourceSet)
				val setting = new ESetting<ECLASS>(source as ECLASS, getEReference, index)
				val resolvedTarget = setting.target
				if(resolvedTarget != storedTarget)
					throw new NoSuchElementException('Reference target has changed')
				lambda.apply(setting as ESetting<ECLASS>)
			]
		} else 
			throw new NoSuchElementException('Cannot open an Xtext editor for ' + sourceID)

	}
	
	override openInEditor(boolean select) {
		provider.getCachedEditor(sourceID, select, select)
	}
	
	override getName() {
		sourceID.qualifiedName.lastSegment + '--' + getEReference.name  + '-->' + targetID.qualifiedName.lastSegment  
	}
	
	def getEReference() {
		eReference ?: (eReference = resolveReference) 
	}
	
	private def resolveReference() {
		val uri = EReferenceURI.createURI
		val ePackage = EPackage.Registry.INSTANCE.getEPackage(uri.trimFragment.toString)
		val reference = ePackage?.eResource?.getEObject(uri.fragment)
		if(reference instanceof EReference)
			return reference 
		else
			throw new IllegalArgumentException('Cannot resolve EReference ' + EReferenceURI)
	}
	
	override equals(Object obj) {
		if(obj instanceof XtextESettingDescriptor<?>) 
			return super.equals(obj) 
				&& sourceID == obj.sourceID && targetID == obj.targetID
				&& eReference == obj.eReference
				&& index == obj.index
		else
			return false
	}
	
	override hashCode() {
		super.hashCode() + 13 * sourceID.hashCode + 23 * targetID.hashCode + 31 * eReference.hashCode + 37 * index
	}
	
	override protected getResourceServiceProvider() {
		sourceID.resourceServiceProvider
	}
	
}