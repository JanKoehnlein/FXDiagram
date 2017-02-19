package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.behavior.DiagramNavigationBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import de.fxdiagram.core.extensions.CoreExtensions
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.extensions.InitializingMapListener
import de.fxdiagram.core.layout.LayoutParameters
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.viewport.ViewportTransform
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.transform.Transform

import static java.lang.Math.*
import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import javafx.geometry.Point2D

/**
 * A diagram with {@link XNode}s and {@link XConnection}s.
 * 
 * Being added to the {@link #nodes} or {@link #connections}, {@link XShapes} will get 
 * automatically be activated.
 * 
 * To find the {@link XDiagram} of an {@link XShape}, use the {@link CoreExtensions}.
 * 
 * Diagrams can be nested ({@link parentDiagram}).  
 * An optional {@link contentsInitializer} allows lazy population of the diagram.
 * Just like an {@link XShape}, an {@link XDiagram} can be customized with composable 
 * {@link Behavior}s.
 *  
 * A {@link viewportTransform} stores the current viewport of the diagram.
 */
@Logging
@ModelNode('nodes', 'connections', 'parentDiagram', 'domainObjectDescriptor', 'layoutParameters')
class XDiagram extends Group implements XActivatable, XDomainObjectOwner {
	
	@FxProperty ObservableList<XNode> nodes = observableArrayList
	@FxProperty ObservableList<XConnection> connections = observableArrayList

	@FxProperty ObservableMap<Node, Pos> fixedButtons = observableMap(newHashMap)

	@FxProperty(readOnly=true) boolean isActive
	@FxProperty boolean layoutOnActivate
	@FxProperty LayoutParameters layoutParameters = new LayoutParameters
	@FxProperty boolean isRootDiagram = true
	@FxProperty XDiagram parentDiagram

	@FxProperty Paint backgroundPaint = Color.WHITE
	@FxProperty Paint foregroundPaint = Color.BLACK
	@FxProperty Paint connectionPaint = Color.gray(0.2)
	@FxProperty(readOnly=true) DomainObjectDescriptor domainObjectDescriptor

	@FxProperty boolean gridEnabled = false
	@FxProperty double gridSize = 10

	Group nodeLayer = new Group
	Group buttonLayer = new Group;
	
	(XDiagram)=>void contentsInitializer

	AuxiliaryLinesSupport auxiliaryLinesSupport
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	ViewportTransform viewportTransform
	
	boolean needsCentering = true
	
	new(DomainObjectDescriptor descriptor) {
		this.domainObjectDescriptorProperty.set(descriptor)
		children += nodeLayer
		children += buttonLayer
		viewportTransform = new ViewportTransform
		transforms.setAll(viewportTransform.transform)
		transforms.addListener([ListChangeListener.Change<? extends Transform> change | 
			throw new IllegalStateException("Illegal attempt to change the transforms of an XDiagram")
		])
	}
	
	new() {
		this(null)	
	}
	
	def getViewportTransform() {
		viewportTransform
	}
	
	override activate() {
		if(!isActive) {
			try {
				isActiveProperty.set(true);
				doActivate
			} catch (Exception exc) {
				LOG.severe(exc.message)
				exc.printStackTrace
			}
		}
	}
	
	def void doActivate() {
		rootDiagramProperty.addListener [ p, o, n |
			if(n) { 
				connections.forEach[
					nodeLayer.children.safeAdd(it)
					nodeLayer.children.safeAdd(sourceArrowHead)
					nodeLayer.children.safeAdd(targetArrowHead)
					nodeLayer.children.safeAdd(labels)
				]
			} else {
             	connections.forEach[
					nodeLayer.children -= it
					nodeLayer.children -= sourceArrowHead
					nodeLayer.children -= targetArrowHead
					nodeLayer.children -= labels
				]
			}				
		]
		contentsInitializer?.apply(this)
		nodes.addInitializingListener(new InitializingListListener<XNode>() => [
			add = [
				initializeGraphics
				getNodeLayer.children += it
				if(XDiagram.this.isActive)
					it.activate
			]
			remove = [
				getNodeLayer.children.safeDelete(it)
			]
		])
		connections.addInitializingListener(new InitializingListListener<XConnection>() => [
			add = [
				val clChildren = connectionLayer.children
				clChildren.safeAdd(it)
				initializeGraphics
				clChildren.safeAdd(sourceArrowHead)
				clChildren.safeAdd(targetArrowHead)
				labels.forEach [
					clChildren.safeAdd(it)
				]
				if(XDiagram.this.isActive)
					it.activate
			]
			remove = [
				val clChildren = connectionLayer.children
				clChildren.safeDelete(it)
				clChildren.safeDelete(sourceArrowHead)
				clChildren.safeDelete(targetArrowHead)
				labels.forEach [
					clChildren.safeDelete(it)
				]
			]
		])
		if(layoutOnActivate && layoutParameters != null) {
			nodes.forEach [
				node.autosize
			]
			connections.forEach [
				node
				labels.forEach[
					node.autosize
				]
			]
			new Layouter().layout(layoutParameters, this, null)
			layoutOnActivate = false
		}
		auxiliaryLinesSupport = if(!isRootDiagram && parentDiagram?.auxiliaryLinesSupport != null)
				parentDiagram.auxiliaryLinesSupport
			else 
				new AuxiliaryLinesSupport(this)
		if(getBehavior(NavigationBehavior) == null) 
			addBehavior(new DiagramNavigationBehavior(this))
		behaviors.addInitializingListener(new InitializingMapListener => [
			put = [ key, Behavior value | value.activate() ]
		])
	}
	
	def void centerDiagram(boolean useForce) {
		if(needsCentering || useForce) {
			layout
			if(layoutBounds.width * layoutBounds.height > 1) {
				val scale = min(1, min(scene.width / layoutBounds.width, scene.height / layoutBounds.height))
				viewportTransform.scale = scale
				val centerInScene = localToScene(boundsInLocal).center
				viewportTransform.setTranslate(0.5 * scene.width - centerInScene.x, 0.5 * scene.height - centerInScene.y)
			}
		}
		needsCentering = false
	}
		
	def <T extends Behavior> T getBehavior(Class<T> key) {
		behaviors.get(key) as T
	}
	
	def addBehavior(Behavior behavior) {
		behaviors.put(behavior.behaviorKey, behavior)
	}
	
	def removeBehavior(String key) {
		behaviors.remove(key)
	}
	
	protected def addArrowHead(Property<? extends Node> property, 
		ChangeListener<? super Node> listener) {
		if(property.value != null && !connectionLayer.children.contains(property.value))
			connectionLayer.children += property.value
		property.addListener(listener)
	} 
	
	def getAuxiliaryLinesSupport() {
		auxiliaryLinesSupport
	}
	
	def Iterable<XShape> getAllShapes() {
		allChildren.filter(XShape)
	}

	def setContentsInitializer((XDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}
	
	def getNodeLayer() {
		nodeLayer
	}
	
	def Group getConnectionLayer() {
		if(!isRootDiagram && parentDiagram?.connectionLayer != null)
			return parentDiagram.connectionLayer
		return nodeLayer		
	}
	
	def getButtonLayer() {
		if(!isRootDiagram && parentDiagram?.buttonLayer != null)
			return parentDiagram.buttonLayer
		buttonLayer
	}
	
	def Point2D getSnappedPosition(Point2D newPositionInDiagram) {
		if(gridEnabled)
			newPositionInDiagram.snapToGrid(gridSize)
		else 
			newPositionInDiagram
	}
	
	def Point2D getSnappedPosition(Point2D newPositionInDiagram, XShape shape, boolean useGrid, boolean useAuxLines) {
		if(useGrid) {
			val hostCenterDelta = shape.localToParent(shape.layoutBounds.center) - new Point2D(shape.layoutX, shape.layoutY)
			val newCenterPosition = newPositionInDiagram + hostCenterDelta
			val snappedCenter = newCenterPosition.snapToGrid(gridSize)			
			val correction = snappedCenter - newCenterPosition
			return newPositionInDiagram + correction
		} else if(useAuxLines) {
			return auxiliaryLinesSupport.getSnappedPosition(shape, newPositionInDiagram)
		}
		return newPositionInDiagram		
	}
	
	def Point2D getSnappedPosition(Point2D newPositionInDiagram, XShape shape) {
		getSnappedPosition(newPositionInDiagram, shape, gridEnabled, true)
	}
}