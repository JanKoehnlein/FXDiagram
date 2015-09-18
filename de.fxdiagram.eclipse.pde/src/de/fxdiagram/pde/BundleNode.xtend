package de.fxdiagram.pde

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.buttons.RapidButton
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.mapping.shapes.BaseNode
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.Side
import javafx.scene.input.MouseButton
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import org.eclipse.osgi.service.resolver.BundleDescription

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static de.fxdiagram.mapping.reconcile.MappingLabelListener.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@ModelNode('inflated')
class BundleNode extends BaseNode<BundleDescription> implements INodeWithLazyMappings {

	public static val BUNDLE_SYMBOLIC_NAME = 'bundleSymbolicName'
	public static val BUNDLE_VERSION = 'bundleVersion'
	public static val BUNDLE_NAME = 'bundleName'
	public static val BUNDLE_PROVIDER = 'bundleProvider'
	public static val BUNDLE_EXECUTION_ENVIRONMENT = 'bundleExecutionEnvironment'

	@FxProperty boolean inflated = false

	Inflator detailsInflator
	
	new(BundleDescriptor descriptor) {
		super(descriptor)
		placementHint = Side.BOTTOM
	}
	
	override BundleDescriptor getDomainObjectDescriptor() {
		super.domainObjectDescriptor as BundleDescriptor
	}
	
	override createNode() {
		val titleArea = new VBox => [
			alignment = Pos.CENTER
		]
		val contentArea = new VBox => [
			padding = new Insets(10, 20, 10, 20)
			children += titleArea
		]
		val pane = new RectangleBorderPane => [
			tooltip = 'Right-click to toggle details'
			children += contentArea 
			val backgroundStops = 
				if(domainObjectDescriptor.withPlugin[isFragmentModel]) 
					#[
						new Stop(0, Color.rgb(255, 193, 210)), 
						new Stop(1, Color.rgb(255, 215, 215))
					]
			else
					#[
						new Stop(0, Color.rgb(158, 188, 227)), 
						new Stop(1, Color.rgb(220, 230, 255))
					]
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				backgroundStops)
		]
		detailsInflator = new Inflator(this, contentArea)
		val detailsArea = new VBox => [
			padding = new Insets(10,0,0,0)
			alignment = Pos.CENTER
		]
		detailsInflator.addInflatable(detailsArea, 1)
		
		addMappingLabelListener(labels, 
			BUNDLE_SYMBOLIC_NAME -> titleArea,
			BUNDLE_VERSION -> detailsArea,
			BUNDLE_NAME -> detailsArea,
			BUNDLE_PROVIDER -> detailsArea,
			BUNDLE_EXECUTION_ENVIRONMENT -> detailsArea)
		pane
	}
	
	override doActivate() {
		super.doActivate()
		onMouseClicked = [
			if(button == MouseButton.SECONDARY) 
				toggleInflated
		]
		val dependencyPathAction = new AddDependencyPathAction(false)
		val rapidButtonBehavior = getBehavior(LazyConnectionMappingBehavior) 
		rapidButtonBehavior.add(new RapidButton(this, TOP, getFilledTriangle(TOP, 'Add dependency path'), dependencyPathAction))
		rapidButtonBehavior.add(new RapidButton(this, BOTTOM, getFilledTriangle(BOTTOM, 'Add dependency path'), dependencyPathAction))
		
		val inverseDependencyPathAction = new AddDependencyPathAction(true)
		rapidButtonBehavior.add(new RapidButton(this, TOP, getFilledTriangle(BOTTOM, 'Add inverse dependency path'), inverseDependencyPathAction))
		rapidButtonBehavior.add(new RapidButton(this, BOTTOM, getFilledTriangle(TOP, 'Add inverse dependency path'), inverseDependencyPathAction))
	}
	
	protected def toggleInflated() {
		if(!inflated) {
			inflated = true
			detailsInflator.inflateAnimation.play
		} else {
			inflated = false
			detailsInflator.deflateAnimation.play
		}
	}
	
	override getButtonSides(ConnectionMapping<?> mapping) {
		#[ LEFT, RIGHT ]
	}
	
}