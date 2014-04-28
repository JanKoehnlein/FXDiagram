package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping
import de.fxdiagram.xtext.glue.mapping.DiagramMapping
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import javax.inject.Inject
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.PackageDeclaration
import org.eclipse.xtext.example.domainmodel.domainmodel.Property
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class DomainmodelDiagramConfig extends AbstractDiagramConfig {
	
	@Inject extension IJvmModelAssociations
	 
	new() {
		val packageDiagram = new DiagramMapping(PackageDeclaration)
		val entityNode = new NodeMapping(Entity)
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
		addMapping(packageDiagram => [
			nodeForEach(entityNode, [elements.filter(Entity)])
		])
		addMapping(entityNode => [
			outConnectionForEach(propertyConnection, [
				features
					.filter(Property)
					.filter[referencedEntity != null]
				]).makeLazy
		])
		addMapping(propertyConnection => [
			target(entityNode, [referencedEntity])
		])
	}
	
	def getReferencedEntity(Property it) {
		val sourceType = type.type.primarySourceElement
		if(sourceType instanceof Entity) 
			return sourceType
		else 
			null
	}
}