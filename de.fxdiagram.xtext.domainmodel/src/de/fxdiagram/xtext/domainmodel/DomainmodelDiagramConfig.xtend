package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.shapes.BaseDiagramNode
import javax.inject.Inject
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration
import org.eclipse.xtext.example.domainmodel.domainmodel.Property

class DomainmodelDiagramConfig extends AbstractDiagramConfig {
	
	@Inject extension DomainModelUtil domainModelUtil
	 
	val packageDiagram = new DiagramMapping<PackageDeclaration>(this, 'packageDiagram') {
		override calls() {
			entityNode.nodeForEach[elements.filter(Entity)]
			packageNode.nodeForEach[elements.filter(PackageDeclaration)]
		}
	}
	
	val packageNode = new NodeMapping<PackageDeclaration>(this, 'packageNode') {
		override createNode(XtextDomainObjectDescriptor<PackageDeclaration> descriptor) {
			 new BaseDiagramNode(descriptor) 	
		}
		
		override calls() {
			packageDiagram.nestedDiagramFor[ it ]			
		}
			
	}

	val entityNode = new NodeMapping<Entity>(this, 'entityNode') {
		override createNode(XtextDomainObjectDescriptor<Entity> descriptor) {
			new EntityNode(descriptor, domainModelUtil) 
		}
		
		override calls() {
			propertyConnection.outConnectionForEach [
				features
					.filter(Property)
					.filter[domainModelUtil.getReferencedEntity(type) != null]
			].makeLazy
//			superTypeConnection.outConnectionForEach [
//				val superEntity = domainModelUtil.getReferencedEntity(superType)
//				if(superEntity == null) 
//					emptyList 
//				else 
//					#[superEntity] 
//			].makeLazy
		}
	} 

	val propertyConnection = new ConnectionMapping<Property>(this, 'propertyConnection') {
		override createConnection(XtextDomainObjectDescriptor<Property> descriptor) {
			new XConnection(descriptor) => [
				targetArrowHead = new LineArrowHead(it, false)
				new XConnectionLabel(it) => [ label |
					label.text.text = descriptor.withDomainObject[name]
				]		
			]
		}
		
		override calls() {
			entityNode.target [domainModelUtil.getReferencedEntity(type)]
		}
	}
	
//	val superTypeConnection = new ConnectionMapping<Entity>(this, 'superTypeConnection') {
//		override createConnection(XtextDomainObjectDescriptor<Entity> descriptor) {
//			new XConnection(descriptor) => [
//				targetArrowHead = new TriangleArrowHead(it, false)
//			]
//		}
//		
//		override calls() {
//			entityNode.target [domainModelUtil.getReferencedEntity(superType)]
//		}
//	}

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