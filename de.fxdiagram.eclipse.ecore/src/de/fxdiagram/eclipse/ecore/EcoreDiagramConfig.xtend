package de.fxdiagram.eclipse.ecore

import de.fxdiagram.core.anchors.DiamondArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig
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

import static de.fxdiagram.mapping.shapes.BaseClassNode.*

import static extension de.fxdiagram.core.extensions.ButtonExtensions.*

class EcoreDiagramConfig extends AbstractEclipseDiagramConfig {
	
	static val EREFERENCE_LABEL_POS = 0.2
	
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
			eReferenceConnection.outConnectionForEach[EReferences.map[new EReferenceWithOpposite(it)]].asButton[getArrowButton("Add EReference")]
			eSuperTypeConnection.outConnectionForEach[ subType |
				val superTypes = newArrayList
				subType.ESuperTypes.forEach[ superType, i |
					if(subType != superType)
						superTypes += new ESuperType(subType, superType)
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

	val eReferenceConnection = new ConnectionMapping<EReferenceWithOpposite>(this, 'eReferenceConnection', 'EReference') {
		override createConnection(IMappedElementDescriptor<EReferenceWithOpposite> descriptor) {
			new BaseConnection(descriptor) => [ conn |
				descriptor.withDomainObject[
					if(to.containment)
						conn.targetArrowHead = new DiamondArrowHead(conn, false)
					else if(to.container)
						conn.sourceArrowHead = new DiamondArrowHead(conn, true)
					else	
						conn.targetArrowHead = new LineArrowHead(conn, false)
					null
				]
			]
		}

		override calls() {
			eReferenceToName.labelFor[to]
			eReferenceFroName.labelFor[fro]
			eClassNode.target[to.EType as EClass]
		}
	}
	
	val eReferenceToName = new ConnectionLabelMapping<EReference>(this, 'eReferenceToName') {
		override createLabel(IMappedElementDescriptor<EReference> descriptor, EReference labelElement) {
			super.createLabel(descriptor, labelElement) => [ 
				position = 1 - EREFERENCE_LABEL_POS
			]
		}
		
		override getText(EReference it) {
			name
		}
	}

	val eReferenceFroName = new ConnectionLabelMapping<EReference>(this, 'eReferenceFroName') {
		override createLabel(IMappedElementDescriptor<EReference> descriptor, EReference labelElement) {
			super.createLabel(descriptor, labelElement) => [ 
				position = EREFERENCE_LABEL_POS
			]
		}
		
		override getText(EReference it) {
			name
		}
	}

	val eSuperTypeConnection = new ConnectionMapping<ESuperType>(this, 'eSuperTypeConnection', 'ESuperType') {
		override createConnection(IMappedElementDescriptor<ESuperType> descriptor) {
			new BaseConnection(descriptor) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, null, Color.WHITE, false)
			]
		}

		override calls() {
			eClassNode.target[superType]
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