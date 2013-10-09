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

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class JavaTypeNode extends XNode {
	
	@org.eclipse.xtend.lib.Property String referenceName

	Class<?> javaType
	
	Text name
	VBox propertyCompartment
	VBox operationCompartment
	
	JavaTypeModel model
	
	new() {
		node = new RectangleBorderPane => [
			children += new VBox => [
				children += name = new Text => [
					textOrigin = VPos.TOP
					font = Font.font(getFont.family, FontWeight.BOLD, getFont.size * 1.1)
					VBox.setMargin(it, new Insets(12, 12, 12, 12))
				]
				children += new Separator 
				children += propertyCompartment = new VBox => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				children += new Separator
				children += operationCompartment = new VBox => [
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
				alignment = Pos.CENTER
			]
		]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	def setJavaType(Class<?> javaType) {
		this.javaType = javaType
		name.text = javaType.simpleName
		key = javaType.simpleName
		model = new JavaTypeModel(javaType)
		propertyCompartment.children.clear
		operationCompartment.children.clear
		if(isActive) {
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
		if(javaType != null) {
			super.activate()
			setJavaType(javaType)
			new JavaTypeRapidButtonBehavior(this).activate
		}
	}
	
}

class JavaTypeRapidButtonBehavior extends AbstractBehavior<JavaTypeNode> {
	
	new(JavaTypeNode host) {
		super(host)
	}
	
	override protected doActivate() {
		val model = host.javaTypeModel
		if(!model.superTypes.empty) {
			val addSuperTypeAction = [
				XRapidButton button |
				val chooser = new CoverFlowChooser(host, button.getChooserPosition)
				chooser += model.superTypes.map[
					superType | new JavaTypeNode => [ it.javaType = superType ]
				]
				chooser.connectionFactory = [
					host, choice |
					new XConnection(host, choice) => [
						targetArrowHead = new TriangleArrowHead(it, 10, 15, 
							it.strokeProperty, host.diagram.backgroundPaintProperty, false)
					]
				]
				host.root.currentTool = chooser
			]
			host.diagram.buttons += #[
				new XRapidButton(host, 0.5, 0, 'icons/SuperType.gif', addSuperTypeAction),
				new XRapidButton(host, 0.5, 1, 'icons/SuperType.gif', addSuperTypeAction)
			] 			
		}
		if(!model.references.empty) {
			val addReferencesAction = [
				XRapidButton button |
				val chooser = new CarusselChooser(host, button.getChooserPosition)
				chooser += model.references.map[
					reference | new JavaTypeNode => [ 
						javaType = reference.type
						referenceName = reference.name
					]
				] 
				chooser.connectionFactory = [
					host, choice |
					new XConnection(host, choice) => [
						targetArrowHead = new LineArrowHead(it, 7, 10, 
							it.strokeProperty, false)
						new XConnectionLabel(it) => [
							text.text = (choice as JavaTypeNode).referenceName
						]
					]
				]
				host.root.currentTool = chooser
			]
			host.diagram.buttons += #[
				new XRapidButton(host, 0, 0.5, 'icons/Reference.gif', addReferencesAction),
				new XRapidButton(host, 1, 0.5, 'icons/Reference.gif', addReferencesAction)
			]
		}

	}
	
	
}