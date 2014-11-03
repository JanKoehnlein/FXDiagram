package de.fxdiagram.xtext.xbase

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.xtext.glue.mapping.AbstractMapping
import de.fxdiagram.xtext.glue.mapping.ESetting
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider
import de.fxdiagram.xtext.glue.mapping.XtextEObjectDescriptor
import de.fxdiagram.xtext.glue.mapping.XtextESettingDescriptor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.ui.JavaUI
import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.resource.IResourceServiceProvider

import static extension org.eclipse.emf.ecore.util.EcoreUtil.*

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
	
	override <T> createMappedElementDescriptor(T domainObject, AbstractMapping<T> mapping) {
		switch it: domainObject {
				EObject: {
					if(eResource.URI.scheme.endsWith('java') && it instanceof JvmIdentifiableElement) {
						val javaElement = getJvmDomainUtil(eResource.URI).getJavaElement(it as JvmIdentifiableElement)
						return new JavaElementDescriptor(URI.toString, fullyQualifiedName, javaElement.handleIdentifier, mapping.config.ID, mapping.ID, this)
							as IMappedElementDescriptor<T>	
					}
					return new JvmEObjectDescriptor(URI.toString, fullyQualifiedName, mapping.config.ID, mapping.ID, this)
						as IMappedElementDescriptor<T>
				} 
				ESetting<?>:
					return new JvmESettingDescriptor(owner.URI.toString, owner.fullyQualifiedName, reference, index, mapping.config.ID, mapping.ID, this)
				IJavaElement: {
					val jvmType = getJvmDomainUtil(URI.createURI('dummy.___xbase')).getJvmElement(it)					
					return new JavaElementDescriptor(jvmType.URI.toString, jvmType.fullyQualifiedName, handleIdentifier, mapping.config.ID, mapping.ID, this)
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

	new(String uri, String fqn, String mappingConfigID, String mappingID, JvmDomainObjectProvider provider) {
		super(uri, fqn, mappingConfigID, mappingID, provider)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
}

@ModelNode
class JvmESettingDescriptor<ECLASS extends EObject> extends XtextESettingDescriptor<ECLASS> {

	new() {
	}

	new(String uri, String fqn, EReference reference, int index, String mappingConfigID, String mappingID, XtextDomainObjectProvider provider) {
		super(uri, fqn, reference, index, mappingConfigID, mappingID, provider)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
}

@ModelNode('handleIdentifier')
class JavaElementDescriptor extends JvmEObjectDescriptor<JvmIdentifiableElement> {

	@FxProperty(readOnly) String handleIdentifier

	new() {
	}

	new(String uri, String fqn, String javaElementHandle, String mappingConfigID, String mappingID, JvmDomainObjectProvider provider) {
		super(uri, fqn, mappingConfigID, mappingID, provider)
		handleIdentifierProperty.set(javaElementHandle)
	}
	
	override protected getResourceServiceProvider() {
		 IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI('dummy.___xbase'))
	}
	
	override <T> withDomainObject((JvmIdentifiableElement)=>T lambda) {
		val javaElement = JavaCore.create(handleIdentifier)
		val domainUtil = (provider as JvmDomainObjectProvider).getJvmDomainUtil(URI.createURI(uri))
		val jvmElement = domainUtil.getJvmElement(javaElement)
		lambda.apply(jvmElement)
	}

	override openInEditor(boolean isSelect) {
		val javaElement = JavaCore.create(handleIdentifier)
		JavaUI.openInEditor(javaElement, true, isSelect)
	}
}
