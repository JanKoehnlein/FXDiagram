package de.fxdiagram.examples.java

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRapidButton
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

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class JavaTypeNode extends XNode {
	
	Class<?> javaType
	
	val name = new Text
	val propertyCompartment = new VBox
	val operationCompartment = new VBox
	
	JavaTypeModel model
	
	new(Class<?> javaType) {
		super(javaType.simpleName)
		this.javaType = javaType
		name.text = javaType.simpleName
		model = new JavaTypeModel(javaType)
		node = new RectangleBorderPane => [
			children += new VBox => [
				children += name => [
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
					VBox.setMargin(it, new Insets(12, 12, 12, 12))
				]
				children += new Separator 
				children += propertyCompartment => [
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
	
	def populateComprtments() {
		model.properties.limit.forEach [
			property |
			propertyCompartment.children += new Text => [
				text = '''«property.name»: «property.type.simpleName»''' 
			]
		]
		model.constructors.forEach [
			constructor |
			operationCompartment.children += new Text => [
				text = '''«javaType.simpleName»(«constructor.parameterTypes.map[simpleName].join(', ')»)''' 
			]
		]
		model.operations.limit.forEach [
			method |
			operationCompartment.children += new Text => [
				text = '''«method.name»(«method.parameterTypes.map[simpleName].join(', ')»): «method.returnType.simpleName»''' 
			]
		]
	}
	
	protected def <T> limit(List<T> list) {
		if(list.empty)
			list
		else if(isActive)
			list
		else 
			list.subList(0, Math.min(list.size, 4))
	}
	
	def getJavaType() {
		javaType
	}
	
	def getJavaTypeModel() {
		model
	}
	
	override activate() {
		super.activate
		populateComprtments
		new JavaTypeRapidButtonBehavior(this).activate
	}
}

class JavaTypeRapidButtonBehavior extends AbstractBehavior<JavaTypeNode> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected doActivate() {
		val model = host.javaTypeModel
		val choosableSupertypes = model.superTypes
		if(!choosableSupertypes.empty) {
			val addSuperTypeAction = [
				XRapidButton button |
				addSuperType(button, choosableSupertypes)
			]
			host.diagram.buttons += #[
				new XRapidButton(host, 0.5, 0, getTriangleButton(TOP, 'Discover supertypes'), addSuperTypeAction),
				new XRapidButton(host, 0.5, 1, getTriangleButton(BOTTOM, 'Discover supertypes'), addSuperTypeAction)
			] 			
		}
		val choosableReferences = model.references
		if(!choosableReferences.empty) {
			val addReferencesAction = [
				XRapidButton button |
				addReference(button, choosableReferences)
			]
			host.diagram.buttons += #[
				new XRapidButton(host, 0, 0.5, getArrowButton(LEFT, 'Discover properties'), addReferencesAction),
				new XRapidButton(host, 1, 0.5, getArrowButton(RIGHT, 'Discover properties'), addReferencesAction)
			]
		}
	}
	
	protected def getSuperTypeKey(XNode host, XNode superType) {
		(host as JavaTypeNode).javaType.simpleName + ' extends ' + (superType as JavaTypeNode).javaType.simpleName
	}
	
	protected def addSuperType(XRapidButton button, Iterable<Class<?>> superTypes) {
		val chooser = new CoverFlowChooser(host, button.getChooserPosition)
		superTypes.forEach[
			superType | 
			chooser.addChoice(new JavaTypeNode(superType))
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

	protected def addReference(XRapidButton button, Iterable<Property> references) {
		val chooser = new CarusselChooser(host, button.getChooserPosition)
		references.forEach[
			reference | 
			chooser.addChoice(new JavaTypeNode(reference.type),  reference)
		] 
		chooser.connectionProvider = [
			host, choice, choiceInfo |
			val reference = choiceInfo as Property 
			new XConnection(host, choice, reference) => [
				targetArrowHead = new LineArrowHead(it, 7, 10, 
					it.strokeProperty, false)
				new XConnectionLabel(it) => [
					text.text = reference.name
				]
			]
		]
		host.root.currentTool = chooser
	}
	
}