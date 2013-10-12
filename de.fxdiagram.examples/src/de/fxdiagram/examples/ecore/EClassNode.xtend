package de.fxdiagram.examples.ecore

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.anchors.DiamondArrowHead
import de.fxdiagram.core.anchors.LineArrowHead
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.behavior.AbstractBehavior
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.lib.tools.CarusselChooser
import de.fxdiagram.lib.tools.CoverFlowChooser
import java.util.List
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.Separator
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EReference

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class EClassNode extends XNode {
	
	EClass eClass
	
	val name = new Text
	val attributeCompartment = new VBox
	val operationCompartment = new VBox
	
	new(EClass eClass) {
		super(eClass.name)
		this.eClass = eClass
		name.text = eClass.name
		node = new RectangleBorderPane => [
			children += new VBox => [
				children += name => [
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
					VBox.setMargin(it, new Insets(12, 12, 12, 12))
				]
				children += new Separator 
				children += attributeCompartment => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				children += new Separator
				children += operationCompartment => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				alignment = Pos.CENTER
			]
		]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	def populateCompartments() {
		eClass.EAttributes.limit.forEach [
			attribute |
			attributeCompartment.children += new Text => [
				text = '''«attribute.name»: «attribute.EType.name»''' 
			]
		]
		eClass.EOperations.limit.forEach [
			operation |
			operationCompartment.children += new Text => [
				text = '''«operation.name»(«operation.EParameters.map[EType.name].join(', ')»): «operation.EType.name»''' 
			]
		]
	}
	
	protected def <T> limit(List<T> list) {
		if(list.empty)
			list
		else if(getIsActive)
			list
		else 
			list.subList(0, Math.min(list.size, 4))
	}
	
	def getEClass() {
		eClass
	}
	
	override activate() {
		super.activate()
		populateCompartments
		new EClassRapidButtonBehavior(this).activate
	}
	
}

class EClassRapidButtonBehavior extends AbstractBehavior<EClassNode> {
	
	new(EClassNode host) {
		super(host)
	}
	
	override protected doActivate() {
		val eClass = host.EClass
		if(!eClass.ESuperTypes.empty) {
			val addSuperTypeAction = [
				XRapidButton button |
				addSuperType(button, eClass.ESuperTypes)
			]
			host.diagram.getButtons += #[
				new XRapidButton(getHost, 0.5, 0, getTriangleButton(TOP, 'Discover supertypes'), addSuperTypeAction),
				new XRapidButton(getHost, 0.5, 1, getTriangleButton(BOTTOM, 'Discover supertypes'), addSuperTypeAction)
			] 			
		}
		if(!eClass.EReferences.empty) {
			val addReferencesAction = [
				XRapidButton button |
				addReference(button, eClass.EReferences)
			]
			host.diagram.getButtons += #[
				new XRapidButton(host, 0, 0.5, getArrowButton(LEFT, 'Discover referenes'), addReferencesAction),
				new XRapidButton(host, 1, 0.5, getArrowButton(RIGHT, 'Discover references'), addReferencesAction)
			]
		}
	}
	
	protected def getSuperTypeKey(XNode host, XNode superType) {
		(host as EClassNode).EClass.name + " extends " + (superType as EClassNode).EClass.name
	}
	
	protected def addSuperType(XRapidButton button, List<? extends EClass> superTypes) {
		val chooser = new CoverFlowChooser(getHost, button.getChooserPosition)
		superTypes.forEach[
			superType | 
				chooser.addChoice(new EClassNode(superType))
		]
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			new XConnection(host, choice, getSuperTypeKey(host, choice)) => [
				targetArrowHead = new TriangleArrowHead(it, 10, 15, 
					it.strokeProperty, host.diagram.backgroundPaintProperty, false)
			]
		]
		host.root.currentTool = chooser
	}
	
	protected def getReferenceKey(EReference reference) {
		reference.EContainingClass.EPackage.name + '.' + reference.EContainingClass.name + '.' + reference.name
	}
	
	protected def addReference(XRapidButton button, List<? extends EReference> references) {
		val chooser = new CarusselChooser(getHost, button.getChooserPosition)
		references.forEach[
			reference | 
			chooser.addChoice(new EClassNode(reference.EReferenceType), reference)
		] 
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as EReference
			new XConnection(host, choice, reference) => [
				targetArrowHead =  if(reference.containment) 
						new DiamondArrowHead(it, false)	
					else
						new LineArrowHead(it, 7, 10, 
							it.strokeProperty, false)
				new XConnectionLabel(it) => [
					text.text = reference.name + if(reference.many) ' *' else ''
				]
			]
		]
		host.root.currentTool = chooser
	}
}