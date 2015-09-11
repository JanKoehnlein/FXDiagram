package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.lib.nodes.ClassModel
import de.fxdiagram.mapping.shapes.BaseClassNode
import org.eclipse.xtext.common.types.JvmDeclaredType
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode
class JvmTypeNode extends BaseClassNode<JvmDeclaredType> {

	@Inject extension JvmDomainUtil 
	
	new(JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
		super(descriptor)
	}
	
	override inferClassModel() {
		domainObjectDescriptor.withDomainObject[ type |
			new ClassModel => [
				fileName = (domainObjectDescriptor as JvmEObjectDescriptor<JvmDeclaredType>)
					.elementID.URI.trimFragment.lastSegment
				namespace = type.packageName
				name = type.simpleName
				attributes += type.attributes.map [
					simpleName + ': ' + type.simpleName
				]
				operations += type.methods.map [
					simpleName + '(): ' + returnType.simpleName
				]
			]
		]
	}
	
}