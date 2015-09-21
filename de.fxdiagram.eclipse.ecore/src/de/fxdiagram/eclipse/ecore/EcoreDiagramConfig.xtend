package de.fxdiagram.eclipse.ecore

import de.fxdiagram.core.anchors.DiamondArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
import de.fxdiagram.eclipse.xtext.ESetting
import de.fxdiagram.mapping.ConnectionLabelMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.MappingAcceptor
import de.fxdiagram.mapping.NodeHeadingMapping
import de.fxdiagram.mapping.NodeLabelMapping
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.shapes.BaseClassNode
import de.fxdiagram.mapping.shapes.BaseConnection
import de.fxdiagram.mapping.shapes.BaseDiagramNode
import javafx.scene.paint.Color
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EOperation
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EcorePackage

import static de.fxdiagram.mapping.shapes.BaseClassNode.*

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*

class EcoreDiagramConfig extends AbstractEclipseDiagramConfig {
	
	val ePackageDiagram = new DiagramMapping<EPackage>(this, 'ePackageDiagram', 'EPackage diagram') {
		override calls() {
			eClassNode.nodeForEach[EClassifiers.filter(EClass)]
			ePackageNode.nodeForEach[ESubpackages]
			eagerly(eSuperTypeConnection, eReferenceConnection)
		}
	}

	val ePackageNode = new NodeMapping<EPackage>(this, 'ePackageNode', 'EPackage node') {
		override createNode(IMappedElementDescriptor<EPackage> descriptor) {
			new BaseDiagramNode(descriptor)
		}

		override calls() {
			ePackageNodeName.labelFor[it]
			ePackageDiagram.nestedDiagramFor[it].onOpen
		}
	}

	val ePackageNodeName = new NodeHeadingMapping<EPackage>(this, BaseDiagramNode.NODE_HEADING) {
		override getText(EPackage element) {
			element.name
		}
	}

	val eClassNode = new NodeMapping<EClass>(this, 'eClassNode', 'EClass') {
		override createNode(IMappedElementDescriptor<EClass> descriptor) {
			new BaseClassNode(descriptor)
		}

		override calls() {
			eClassName.labelFor[it]
			eAttribute.labelForEach[EAttributes]
			eOperation.labelForEach[EOperations]
			eReferenceConnection.outConnectionForEach[EReferences].asButton[getArrowButton("Add EReference")]
			eSuperTypeConnection.outConnectionForEach[ subType |
				val superTypes = newArrayList
				subType.ESuperTypes.forEach[ superType, i |
					if(subType != superType)
						superTypes += new ESetting(subType, EcorePackage.Literals.ECLASS__ESUPER_TYPES, i)
				]
				superTypes
			].asButton[getTriangleButton("Add ESuperClass")]
		}
	}

	val eClassName = new NodeHeadingMapping<EClass>(this, CLASS_NAME) {
		override getText(EClass it) {
			name
		}
	}

	val eAttribute = new NodeLabelMapping<EAttribute>(this, ATTRIBUTE) {
		override getText(EAttribute it) {
			'''«name»: «EType.name»'''
		}
	}

	val eOperation = new NodeLabelMapping<EOperation>(this, OPERATION) {
		override getText(EOperation it) {
			name + '() : ' + (EType?.name ?: 'void') 
		}
	}

	val eReferenceConnection = new ConnectionMapping<EReference>(this, 'eReferenceConnection', 'EReference') {
		override createConnection(IMappedElementDescriptor<EReference> descriptor) {
			new BaseConnection(descriptor) => [ conn |
				descriptor.withDomainObject[
					if(containment)
						conn.targetArrowHead = new DiamondArrowHead(conn, false)
					else if(container)
						conn.sourceArrowHead = new DiamondArrowHead(conn, true)
					else	
						conn.targetArrowHead = new LineArrowHead(conn, false)
					null
				]
			]
		}

		override calls() {
			eReferenceName.labelFor[it]
			eClassNode.target[EType as EClass]
		}
	}
	
	val eReferenceName = new ConnectionLabelMapping<EReference>(this, 'eReferenceName') {
		override getText(EReference it) {
			name
		}
	}

	val eSuperTypeConnection = new ConnectionMapping<ESetting<EClass>>(this, 'eSuperTypeConnection', 'ESuperType') {
		override createConnection(IMappedElementDescriptor<ESetting<EClass>> descriptor) {
			new BaseConnection(descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false)
			]
		}

		override calls() {
			eClassNode.target[target as EClass]
		}
	}

	override protected <ARG> entryCalls(ARG domainArgument, extension MappingAcceptor<ARG> acceptor) {
		switch domainArgument {
			EClass:
				add(eClassNode)
			EPackage: {
				add(ePackageNode)
				add(ePackageDiagram)
			}
			EReference:
				add(eReferenceConnection)
		}
	}

	override protected createDomainObjectProvider() {
		new EcoreDomainObjectProvider
	}
}