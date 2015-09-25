package de.fxdiagram.xtext.xbase

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.xtext.ESetting
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider
import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor
import de.fxdiagram.eclipse.xtext.XtextESettingDescriptor
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import java.util.NoSuchElementException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.ui.JavaUI
import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.resource.IResourceServiceProvider
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID

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
					if(eResource.URI.scheme.endsWith('java') && it instanceof JvmIdentifiableElement) {
						val javaElement = getJvmDomainUtil(eResource.URI).getJavaElement(it as JvmIdentifiableElement)
						return new JavaElementDescriptor(elementID, javaElement.handleIdentifier, mapping.config.ID, mapping.ID, this)
							as IMappedElementDescriptor<T>	
					}
					return new JvmEObjectDescriptor(elementID, mapping.config.ID, mapping.ID, this)
						as IMappedElementDescriptor<T>
				} 
				ESetting<?>: {
					val sourceID = owner.createXtextEObjectID
					val targetID = target.createXtextEObjectID
					return new JvmESettingDescriptor(sourceID, targetID, reference, index, mapping.config.ID, mapping.ID, this)
				}
				IJavaElement: {
					val jvmType = getJvmDomainUtil(URI.createURI('dummy.___xbase')).getJvmElement(it)
					val elementID = jvmType.createXtextEObjectID				
					return new JavaElementDescriptor(elementID, handleIdentifier, mapping.config.ID, mapping.ID, this)
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

	new(XtextEObjectID elementID, String mappingConfigID, String mappingID, JvmDomainObjectProvider provider) {
		super(elementID, mappingConfigID, mappingID, provider)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
}

@ModelNode
class JvmESettingDescriptor<ECLASS extends EObject> extends XtextESettingDescriptor<ECLASS> {

	new() {
	}

	new(XtextEObjectID sourceID, XtextEObjectID targetID, EReference reference, int index, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(sourceID, targetID, reference, index, mappingConfigID, mappingID, provider)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
}

@ModelNode('handleIdentifier')
class JavaElementDescriptor extends JvmEObjectDescriptor<JvmIdentifiableElement> {

	@FxProperty(readOnly=true) String handleIdentifier

	new() {
	}

	new(XtextEObjectID elementID, String javaElementHandle, String mappingConfigID, String mappingID, JvmDomainObjectProvider provider) {
		super(elementID, mappingConfigID, mappingID, provider)
		handleIdentifierProperty.set(javaElementHandle)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
	
	override <T> withDomainObject((JvmIdentifiableElement)=>T lambda) {
		val javaElement = JavaCore.create(handleIdentifier)
		if(javaElement == null)
			throw new NoSuchElementException('Java element ' + handleIdentifier + ' not found')
		val domainUtil = (provider as JvmDomainObjectProvider).getJvmDomainUtil(elementID.URI)
		val jvmElement = domainUtil.getJvmElement(javaElement)
		if(jvmElement == null)
			throw new NoSuchElementException('JVM element for ' + javaElement.elementName + ' not found')
		lambda.apply(jvmElement)
	}

	override openInEditor(boolean isSelect) {
		val javaElement = JavaCore.create(handleIdentifier)
		JavaUI.openInEditor(javaElement, true, isSelect)
	}
}
