package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor
import de.fxdiagram.core.command.AnimationCommand
import de.fxdiagram.core.command.SequentialAnimationCommand
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.NodeReconcileBehavior
import javafx.beans.property.BooleanProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.ColorPicker
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import org.eclipse.xtend.lib.annotations.Accessors

import static javafx.geometry.Side.*
import static javafx.scene.input.MouseButton.*

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import static extension de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*
import static extension de.fxdiagram.mapping.reconcile.MappingLabelListener.*
import static extension de.fxdiagram.mapping.shapes.BaseShapeInitializer.*

@ModelNode('showPackage', 'showAttributes', 'showMethods', 'bgColor')
class BaseClassNode<T> extends FlipNode implements INodeWithLazyMappings {

	public static val CLASS_NAME = 'className'
	public static val FILE_NAME = 'fileName'
	public static val PACKAGE = 'package'
	public static val ATTRIBUTE = 'attribute'
	public static val OPERATION = 'operation'

	@FxProperty boolean showPackage = false
	@FxProperty boolean showAttributes = true
	@FxProperty boolean showMethods = true
	@FxProperty Color bgColor

	protected CheckBox packageBox
	protected CheckBox attributesBox
	protected CheckBox methodsBox

	VBox contentArea
	VBox packageArea
	VBox attributeCompartment
	VBox methodCompartment
	VBox nameArea
	VBox fileArea

	@Accessors(PUBLIC_GETTER) Inflator inflator

	new() {
		initializeLazily
	}

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	def getDefaultBgPaint() {
		Color.web('#ffe6cc')
	}

	override createNode() {
		val pane = super.createNode
		bgColor = defaultBgPaint
		front = new RectangleBorderPane => [
			tooltip = 'Right-click to configure'
			backgroundPaintProperty.bind(bgColorProperty)
			children += contentArea = new VBox => [
				padding = new Insets(10, 20, 10, 20)
				children += nameArea = new VBox => [
					alignment = Pos.CENTER
				]
			]
		]
		back = new RectangleBorderPane => [
			tooltip = 'Right-click to show node'
			backgroundPaintProperty.bind(bgColorProperty)
			children += new VBox => [
				padding = new Insets(10, 20, 10, 20)
				spacing = 5
				children += fileArea = new VBox
				children += packageBox = new CheckBox('Package')
				children += attributesBox = new CheckBox('Attributes')
				children += methodsBox = new CheckBox('Methods')
				children += new ColorPicker => [
					valueProperty.bindBidirectional(bgColorProperty)
				]
			]
		]
		inflator = new Inflator(this, contentArea)
		packageArea = new VBox => [
			alignment = Pos.CENTER
			addInflatable(showPackageProperty, packageBox, 0, inflator)
		]
		attributeCompartment = new VBox => [ c |
			c.padding = new Insets(10, 0, 0, 0)
			c.addInflatable(showAttributesProperty, attributesBox, contentArea.children.size, inflator)
		]
		methodCompartment = new VBox => [ c |
			c.padding = new Insets(10, 0, 0, 0)
			c.addInflatable(showMethodsProperty, methodsBox, contentArea.children.size, inflator)
		]
		labelsProperty.addMappingLabelListener(
			CLASS_NAME -> nameArea,
			ATTRIBUTE -> attributeCompartment,
			OPERATION -> methodCompartment,
			FILE_NAME -> fileArea,
			PACKAGE -> packageArea
			)
		pane
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 6, 6)
	}

	override doActivate() {
		super.doActivate()
		addBehavior(new ReconcileBehavior(this))
		showPackageProperty.bindCheckbox(packageBox, packageArea, [0], inflator)
		showAttributesProperty.bindCheckbox(attributesBox, attributeCompartment, [if(showPackage) 2 else 1], inflator)
		showMethodsProperty.bindCheckbox(methodsBox, methodCompartment, [contentArea.children.size], inflator)
		inflator.inflateAnimation.play
		addLazyBehavior(domainObjectDescriptor)
	}

	override getAutoLayoutDimension() {
		inflator.inflatedSize
	}

	protected def addInflatable(VBox compartment, BooleanProperty showProperty, CheckBox box, int index,
		Inflator inflator) {
		if (showProperty.get)
			inflator.addInflatable(compartment, index)
	}

	protected def bindCheckbox(BooleanProperty property, CheckBox box, VBox compartment, ()=>int index,
		Inflator inflator) {
		box.selectedProperty.bindBidirectional(property)
		property.addListener [ p, o, show |
			if (contentArea.children.contains(compartment)) {
				if (!show)
					inflator.removeInflatable(compartment)
			} else {
				if (show)
					inflator.addInflatable(compartment, index.apply)
			}
		]
	}

	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}

	override registerOnClick() {
		addEventHandler(MouseEvent.MOUSE_CLICKED) [
			if (button == SECONDARY) {
				if (front != null && back != null) {
					flip(isHorizontal(it))
					consume
				}
			}
		]
	}

	override getButtonSides(ConnectionMapping<?> mapping) {
		#[TOP, BOTTOM, LEFT, RIGHT]
	}
	
	static class ReconcileBehavior<T> extends NodeReconcileBehavior<T> {
	
		new(BaseClassNode<T> host) {
			super(host)
		}
		
		override BaseClassNode<T> getHost() {
			super.host as BaseClassNode<T> 
		}
		
		override protected reconcile(AbstractMapping<T> mapping, T domainObject, UpdateAcceptor acceptor) {
			val fakeAcceptor = new UpdateAcceptor {
				
				override delete(XShape shape, XDiagram diagram) {
					acceptor.delete(shape, diagram)
				}
				
				override add(XShape shape, XDiagram diagram) {
					acceptor.add(shape, diagram)
				}
				
				override morph(AnimationCommand command) {
					acceptor.morph(new SequentialAnimationCommand => [
						it += host.inflator.deflateCommand
						it += command
						it += host.inflator.inflateCommand
					])
				}
			} 
			super.reconcile(mapping, domainObject, fakeAcceptor)
		}
	}
}

