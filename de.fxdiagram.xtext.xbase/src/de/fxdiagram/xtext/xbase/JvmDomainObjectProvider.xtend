package de.fxdiagram.xtext.xbase

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider
import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.ui.JavaUI
import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.resource.IResourceServiceProvider

import static extension org.eclipse.xtext.EcoreUtil2.*

class JvmDomainObjectProvider extends XtextDomainObjectProvider {
	
	protected def getJvmDomainUtil(URI uri) {
		val serviceLookupURI = if(uri.scheme == 'java')
				URI.createURI('dummy.___xbase')
			else
				uri
		val resourceServiceProvider = IResourceServiceProvider.Registry.INSTANCE
			.getResourceServiceProvider(serviceLookupURI)
		resourceServiceProvider.get(JvmDomainUtil)
	}
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<? extends T> mapping) {
		switch it: domainObject {
				EObject: {
					val elementID = createXtextEObjectID
					if(eResource.URI.scheme.endsWith('java')) {
						val identifiableJvmElement = getContainerOfType(JvmIdentifiableElement)
						val javaElement = getJvmDomainUtil(eResource.URI).getJavaElement(identifiableJvmElement)
						if(javaElement !== null)
							return new JavaElementDescriptor(elementID, javaElement.handleIdentifier, mapping.config.ID, mapping.ID)
								as IMappedElementDescriptor<T>	
					}
					return new JvmEObjectDescriptor(elementID, mapping.config.ID, mapping.ID)
						as IMappedElementDescriptor<T>
				} 
				default:
					return null
			}
	}
	
}

class JvmEObjectDescriptor<ECLASS extends EObject> extends XtextEObjectDescriptor<ECLASS> {

	new() {
	}

	new(XtextEObjectID elementID, String mappingConfigID, String mappingID) {
		super(elementID, mappingConfigID, mappingID)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
	
	override equals(Object obj) {
		if(obj instanceof JvmEObjectDescriptor<?>) 
			super.equals(obj) && elementID.URI == obj.elementID.URI
		else
			false
	}
	
	override hashCode() {
		super.hashCode() + 137 * elementID.URI.hashCode
	}
}

@ModelNode('handleIdentifier')
class JavaElementDescriptor<ECLASS extends EObject> extends JvmEObjectDescriptor<ECLASS> {

	@FxProperty(readOnly=true) String handleIdentifier

	new() {
	}

	new(XtextEObjectID elementID, String javaElementHandle, String mappingConfigID, String mappingID) {
		super(elementID, mappingConfigID, mappingID)
		handleIdentifierProperty.set(javaElementHandle)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
	
	override <T> withDomainObject((ECLASS)=>T lambda) {
		val javaElement = JavaCore.create(handleIdentifier)
		if(javaElement === null)
			throw new NoSuchElementException('Java element ' + handleIdentifier + ' not found')
		val domainUtil = (provider as JvmDomainObjectProvider).getJvmDomainUtil(elementID.URI)
		val jvmElement = domainUtil.getJvmElement(javaElement)
		if(jvmElement === null)
			throw new NoSuchElementException('JVM element for ' + javaElement.elementName + ' not found')
		val realJvmElement = jvmElement.eResource.getEObject(elementID.URI.fragment)
		lambda.apply(realJvmElement as ECLASS)
	}

	override openInEditor(boolean isSelect) {
		val javaElement = JavaCore.create(handleIdentifier)
		JavaUI.openInEditor(javaElement, true, isSelect)
	}
}
