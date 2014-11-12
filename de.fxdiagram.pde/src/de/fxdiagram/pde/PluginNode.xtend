package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.xtext.glue.shapes.BaseNode
import javafx.animation.ParallelTransition
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.input.MouseButton
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.pde.core.plugin.IPluginModelBase

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import de.fxdiagram.xtext.glue.shapes.INodeWithLazyMappings
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping

@ModelNode('inflated')
class PluginNode extends BaseNode<IPluginModelBase> implements INodeWithLazyMappings {

	@FxProperty boolean inflated = false

	Pane contentArea
	
	VBox titleArea
	Text nameLabel
	Text versionLabel
	
	VBox detailsArea
	
	Inflator titleInflator
	
	Inflator detailsInflator
	
	new(PluginDescriptor descriptor) {
		super(descriptor)
	}
	
	override PluginDescriptor getDomainObject() {
		super.getDomainObject() as PluginDescriptor
	}
	
	override createNode() {
		new RectangleBorderPane => [
			tooltip = 'Right-click to toggle details'
			children += contentArea = new VBox => [
				padding = new Insets(10, 20, 10, 20)
				children += titleArea = new VBox => [
					alignment = Pos.CENTER
					children += nameLabel = new Text => [
						textOrigin = VPos.TOP
						text = domainObject.symbolicName
						val isSingleton = domainObject.withDomainObject[
							bundleDescription.isSingleton
						]
						if(isSingleton) 
							font = Font.font(font.family, FontWeight.BOLD, FontPosture.ITALIC, font.size * 1.1)
						else 
							font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
					]
				]
			]
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				#[
					new Stop(0, Color.rgb(158, 188, 227)), 
					new Stop(1, Color.rgb(220, 230, 255))
				])
		]
	}
	
	override doActivate() {
		super.doActivate()
		titleInflator = new Inflator(this, titleArea)
		titleInflator.addInflatable(new VBox => [
			alignment = Pos.CENTER
			children += versionLabel = new Text => [
				textOrigin = VPos.TOP
				text = domainObject.version
				font = Font.font(font.family, font.size * 0.8)
			]
		], 1)
		detailsInflator = new Inflator(this, contentArea)
		detailsArea = new VBox => [
			padding = new Insets(10,0,0,0)
			alignment = Pos.CENTER
			children += new Text => [
				textOrigin = VPos.TOP
				text = domainObject.withDomainObject[
					pluginBase.getResourceString(pluginBase.name)
				]
			]
			children += new Text => [
				textOrigin = VPos.TOP
				text = domainObject.withDomainObject[
					pluginBase.getResourceString(pluginBase.providerName)
				]
			]
			children += new Text => [
				textOrigin = VPos.TOP
				text = domainObject.withDomainObject[
					bundleDescription.executionEnvironments.join(', ')
				]
			]
		]
		detailsInflator.addInflatable(detailsArea, 1)
		onMouseClicked = [
			if(button == MouseButton.SECONDARY) 
				toggleInflated
		]
		val importPathAction = new AddImportPathAction(true)
		val rapidButtonBehavior = getBehavior(LazyConnectionMappingBehavior) 
		rapidButtonBehavior.add(new RapidButton(this, TOP, getFilledTriangle(TOP, 'Add import path'), importPathAction))
		rapidButtonBehavior.add(new RapidButton(this, BOTTOM, getFilledTriangle(BOTTOM, 'Add import path'), importPathAction))
		
		val dependentPathAction = new AddImportPathAction(false)
		rapidButtonBehavior.add(new RapidButton(this, TOP, getFilledTriangle(BOTTOM, 'Add imported path'), dependentPathAction))
		rapidButtonBehavior.add(new RapidButton(this, BOTTOM, getFilledTriangle(TOP, 'Add imported path'), dependentPathAction))
	}
	
	protected def toggleInflated() {
		if(!inflated) {
			inflated = true
			new ParallelTransition => [
				children += titleInflator.inflateAnimation
				children += detailsInflator.inflateAnimation
				play
			]		
		} else {
			inflated = false
			new ParallelTransition => [
				children += titleInflator.deflateAnimation
				children += detailsInflator.deflateAnimation
				play
			]
		}
	}
	
	override getButtonSides(ConnectionMapping<?> mapping) {
		#[ LEFT, RIGHT ]
	}
	
}