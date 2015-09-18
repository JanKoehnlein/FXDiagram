package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig
import de.fxdiagram.mapping.ConnectionLabelMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.NodeHeadingMapping
import de.fxdiagram.mapping.NodeLabelMapping
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.execution.MappingAcceptor
import de.fxdiagram.mapping.shapes.BaseConnection
import de.fxdiagram.mapping.shapes.BaseDiagramNode
import javafx.scene.paint.Color
import javax.inject.Inject
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.Operation
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration
import org.eclipse.xtext.example.domainmodel.domainmodel.Property

import static de.fxdiagram.xtext.domainmodel.EntityNode.*

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*

class DomainmodelDiagramConfig extends AbstractXtextDiagramConfig {

	@Inject extension DomainModelUtil domainModelUtil

	val packageDiagram = new DiagramMapping<PackageDeclaration>(this, 'packageDiagram', 'Package diagram') {
		override calls() {
			entityNode.nodeForEach[elements.filter(Entity)]
			packageNode.nodeForEach[elements.filter(PackageDeclaration)]
			eagerly(superTypeConnection, propertyConnection)
		}
	}

	val packageNode = new NodeMapping<PackageDeclaration>(this, 'packageNode', 'Package node') {
		override createNode(IMappedElementDescriptor<PackageDeclaration> descriptor) {
			new BaseDiagramNode(descriptor)
		}

		override calls() {
			packageNodeName.labelFor[it]
			packageDiagram.nestedDiagramFor[it].onOpen
		}
	}

	val packageNodeName = new NodeHeadingMapping<PackageDeclaration>(this, BaseDiagramNode.NODE_HEADING) {
		override getText(PackageDeclaration element) {
			element.name.split('\\.').last
		}
	}

	val entityNode = new NodeMapping<Entity>(this, 'entityNode', 'Entity') {
		override createNode(IMappedElementDescriptor<Entity> descriptor) {
			new EntityNode(descriptor)
		}

		override calls() {
			entityName.labelFor[it]
			attribute.labelForEach[features.filter(Property).filter[type.referencedEntity == null]]
			operation.labelForEach[features.filter(Operation)]
			propertyConnection.outConnectionForEach [
				features.filter(Property).filter[domainModelUtil.getReferencedEntity(type) != null]
			].asButton[getArrowButton("Add property")]
			superTypeConnection.outConnectionForEach [
				if (superType.referencedEntity != null)
					#[superType]
				else
					#[]
			].asButton[getTriangleButton("Add superclass")]
		}
	}

	val entityName = new NodeHeadingMapping<Entity>(this, ENTITY_NAME) {
		override getText(Entity it) {
			name
		}
	}

	val attribute = new NodeLabelMapping<Property>(this, ATTRIBUTE) {
		override getText(Property it) {
			'''«name»: «type.simpleName»'''
		}
	}

	val operation = new NodeLabelMapping<Operation>(this, OPERATION) {
		override getText(Operation it) {
			name + '()'
		}
	}

	val propertyConnection = new ConnectionMapping<Property>(this, 'propertyConnection', 'Property') {
		override createConnection(IMappedElementDescriptor<Property> descriptor) {
			new BaseConnection(descriptor) => [
				targetArrowHead = new LineArrowHead(it, false)
			]
		}

		override calls() {
			propertyName.labelFor[it]
			entityNode.target[type.referencedEntity]
		}
	}
	
	val propertyName = new ConnectionLabelMapping<Property>(this, 'propertyName') {
		override getText(Property it) {
			name
		}
	}

	val superTypeConnection = new ConnectionMapping<JvmTypeReference>(this, 'superTypeConnection', 'Supertype') {
		override createConnection(IMappedElementDescriptor<JvmTypeReference> descriptor) {
			new BaseConnection(descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false)
			]
		}

		override calls() {
			entityNode.target[referencedEntity]
		}
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			Entity:
				add(entityNode)
			PackageDeclaration: {
				add(packageNode)
				add(packageDiagram)
			}
			Property:
				add(propertyConnection)
		}
	}
}