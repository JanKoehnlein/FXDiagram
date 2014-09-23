package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.ESetting
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import javafx.scene.paint.Color
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmTypeReference

import static org.eclipse.xtext.common.types.TypesPackage.Literals.*

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*
import org.eclipse.jdt.core.IType

class JvmClassDiagramConfig extends AbstractDiagramConfig {

	@Inject extension JvmDomainUtil

	val typeNode = new NodeMapping<JvmDeclaredType>(this, 'typeNode') {
		override createNode(AbstractXtextDescriptor<JvmDeclaredType> descriptor) {
			new JvmTypeNode(descriptor as JvmEObjectDescriptor<JvmDeclaredType>)
		}

		override calls() {
			referenceConnection.outConnectionForEach [
				references
			].makeLazy[getArrowButton("Add reference")]
			superTypeConnection.outConnectionForEach [ JvmDeclaredType it | 
				val result = newArrayList
				for(var i=0; i<superTypes.size(); i++) {
					val superType = superTypes.get(i)
					if(superType.type instanceof JvmDeclaredType) {
						result.add(new ESetting(it, JVM_DECLARED_TYPE__SUPER_TYPES, i))	
					}
				}
				result
			].makeLazy[getTriangleButton("Add supertype")]
		}
	}

	val referenceConnection = new ConnectionMapping<JvmField>(this, 'referenceConnection') {
		override createConnection(AbstractXtextDescriptor<JvmField> descriptor) {
			new XConnection(descriptor) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [ label |
					label.text.text = descriptor.withDomainObject[simpleName]
				]
			]
		}

		override calls() {
			typeNode.target[(type.componentType.type as JvmDeclaredType).originalJvmType]
		}
	}

	val superTypeConnection = new ConnectionMapping<ESetting<JvmDeclaredType>>(this, 'superTypeConnection') {
		override createConnection(AbstractXtextDescriptor<ESetting<JvmDeclaredType>> descriptor) {
			new XConnection(descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false)
				// TODO set strokeDashOffset for interfaces			
			]
		}

		override calls() {
			typeNode.target[((target as JvmTypeReference).type as JvmDeclaredType).originalJvmType]
		}
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		if (domainArgument instanceof JvmDeclaredType || domainArgument instanceof IType)
			acceptor.add(typeNode)
	}
	
	override protected createDomainObjectProvider() {
		new JvmDomainObjectProvider
	}
	
}
