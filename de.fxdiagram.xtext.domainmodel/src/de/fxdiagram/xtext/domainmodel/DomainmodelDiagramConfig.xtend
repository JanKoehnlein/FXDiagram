package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.shapes.BaseDiagramNode
import javax.inject.Inject
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration
import org.eclipse.xtext.example.domainmodel.domainmodel.Property

class DomainmodelDiagramConfig extends AbstractDiagramConfig {
	
	@Inject extension DomainModelUtil domainModelUtil
	 
	new() {
		val packageDiagram = new DiagramMapping(PackageDeclaration)
		val packageNode = new NodeMapping('packageNode', PackageDeclaration) => [
			createNode = [ descriptor | new BaseDiagramNode(descriptor) ]
		]
		val entityNode = new NodeMapping(Entity) => [ 
			createNode = [ descriptor | new EntityNode(descriptor, domainModelUtil) ] 
		]
		val propertyConnection = new ConnectionMapping(Property) => [
			createConnection = [ descriptor |
				new XConnection(descriptor) => [
					targetArrowHead = new LineArrowHead(it, false)
					new XConnectionLabel(it) => [ label |
						label.text.text = descriptor.withDomainObject[name]
					]		
				]
			]
		] 
//		val superTypeConnection = new ConnectionMapping('superType', Entity) => [
//			createConnection = [ descriptor |
//				new XConnection(descriptor) => [
//					targetArrowHead = new TriangleArrowHead(it, false)
//				]
//			]
//		] 
		addMapping(packageDiagram => [
			nodeForEach(entityNode, [elements.filter(Entity)])
			nodeForEach(packageNode, [elements.filter(PackageDeclaration)])
		])
		addMapping(entityNode => [
			outConnectionForEach(propertyConnection, [
				features
					.filter(Property)
					.filter[type.referencedEntity != null]
				]).makeLazy
//			outConnectionForEach(superTypeConnection, [
//				val superEntity = superType?.referencedEntity
//				if(superEntity == null) 
//					emptyList 
//				else 
//					#[superEntity] 
//			]).makeLazy
		])
		addMapping(packageNode => [
			nestedDiagramFor(packageDiagram, [ it ])
		])
		addMapping(propertyConnection => [
			target(entityNode, [type.referencedEntity])
		])
//		addMapping(superTypeConnection => [
//			target(entityNode, [superType.referencedEntity])
//		])
	}
	
}