package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.MappingAcceptor
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.shapes.BaseConnection
import de.fxdiagram.mapping.shapes.BaseDiagramNode
import javafx.scene.paint.Color
import org.eclipse.emf.ecore.EObject
import org.eclipse.jdt.core.IType
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*

class JvmClassDiagramConfig extends AbstractXtextDiagramConfig {

	@Inject extension JvmDomainUtil
	
	@Inject extension IResourceServiceProvider.Registry

	val typeNode = new NodeMapping<JvmDeclaredType>(this, 'typeNode', 'Type') {
		override createNode(IMappedElementDescriptor<JvmDeclaredType> descriptor) {
			new JvmTypeNode(descriptor as JvmEObjectDescriptor<JvmDeclaredType>)
		}

		override calls() {
			referenceConnection.outConnectionForEach [
				references
			].asButton[getArrowButton("Add reference")]
			superTypeConnection.outConnectionForEach [ JvmDeclaredType it | 
				superTypes.filter[type instanceof JvmDeclaredType]
			].asButton[getTriangleButton("Add supertype")]
		}
	}

	val referenceConnection = new ConnectionMapping<JvmField>(this, 'referenceConnection', 'Reference') {
		override createConnection(IMappedElementDescriptor<JvmField> descriptor) {
			new BaseConnection(descriptor) => [
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

	val superTypeConnection = new ConnectionMapping<JvmTypeReference>(this, 'superTypeConnection', 'Supertype') {
		override createConnection(IMappedElementDescriptor<JvmTypeReference> descriptor) {
			new BaseConnection(descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false)
				// TODO set strokeDashOffset for interfaces			
			]
		}

		override calls() {
			typeNode.target[(type as JvmDeclaredType).originalJvmType]
		}
	}
	
	val packageDiagram = new DiagramMapping<PackageDeclaration>(this, 'packageDiagram', 'Package diagram') {
		override calls() {
			typeNode.nodeForEach[elements.filter(Entity).map[primaryJvmElement].filter(JvmDeclaredType)]
			packageNode.nodeForEach[elements.filter(PackageDeclaration)]
			eagerly(superTypeConnection, referenceConnection)
		}
	}
	
	val packageNode = new NodeMapping<PackageDeclaration>(this, 'packageNode', 'Package node') {
		override createNode(IMappedElementDescriptor<PackageDeclaration> descriptor) {
			 new BaseDiagramNode(descriptor) 	
		}
		
		override calls() {
			packageDiagram.nestedDiagramFor[ it ].onOpen	
		}
	}

	protected def getPrimaryJvmElement(EObject element) {
		getResourceServiceProvider(element.eResource.URI)
			?.get(IJvmModelAssociations)
			?.getPrimaryJvmElement(element)
	}
	
	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			JvmDeclaredType, IType:
				acceptor.add(typeNode)
		 	PackageDeclaration:
		 		acceptor.add(packageNode)
		}
	}
	
	override protected createDomainObjectProvider() {
		new JvmDomainObjectProvider
	}
	
}
