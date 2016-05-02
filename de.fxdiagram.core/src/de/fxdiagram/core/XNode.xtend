package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.anchors.Anchors
import de.fxdiagram.core.anchors.RectangleAnchors
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.StringDescriptor
import javafx.collections.ObservableList
import javafx.geometry.Side
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import javafx.scene.Group
import javafx.scene.Node
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * A node in an {@link XDiagram} that can be connected to other {@link XNode}s via 
 * {@link XConnections}.
 * 
 * Clients should inherit from this class and usually adapt 
 * <ul>
 * <li>{@link #createNode} to customize the node's initial apperance</li>   
 * <li>{@link #initializeGraphics} to customize the node's appearance after activation</li>   
 * <li>{@link #doActivate} to register event listeners and initialize behaviors</li>
 * </ul>
 * and pass the nderlying semantic element as {@link #domainObject} in the super constructor.
 * 
 * An {@link XNode} knows its {@link outgoingConnections} and {incomingConnections}. These 
 * properties are automatically kept in sync with their counterparts {@link XConnection#source} 
 * and {@link XConnection#target}.
 * 
 * The {@link anchors} describe how {@link XConnection}s should connect graphically with 
 * the node.
 */
@Logging
@ModelNode('layoutX', 'layoutY', 'width', 'height', 'labels', 'placementHint')
class XNode extends XDomainObjectShape {

	@FxProperty double width
	@FxProperty double height
	@FxProperty ObservableList<XConnection> incomingConnections = observableArrayList
	@FxProperty ObservableList<XConnection> outgoingConnections = observableArrayList
	@FxProperty ObservableList<XLabel> labels = observableArrayList
	@FxProperty Side placementHint = Side.BOTTOM
	
	@Accessors(PUBLIC_GETTER)
	Group placementGroup = new Group
	
	Effect mouseOverEffect
	Effect selectionEffect
	Effect originalEffect

	Anchors anchors
	
 	new(DomainObjectDescriptor domainObject) {
 		super(domainObject)
 	}
 	
 	new(String name) {
 		this(new StringDescriptor(name))
 	}
 	
 	def getName() {
		val name = domainObjectDescriptor?.name
 		if(name == null)
 			LOG.severe("XNode's name is null")
 		name
 	}
 		
	protected def createMouseOverEffect() {
		new InnerShadow
	}

	protected def createSelectionEffect() {
		new DropShadow() => [
			offsetX = 4.0
			offsetY = 4.0
		]
	}

	protected def Anchors createAnchors() {
		new RectangleAnchors(this)
	}

	override getNode() {
		val node = super.getNode
		mouseOverEffect = createMouseOverEffect
		selectionEffect = createSelectionEffect
		anchors = createAnchors
		node
	}
	
	override protected addNodeAsChild(Node newNode) {
		children += placementGroup => [
			children += newNode
		]
	}
	
	protected override createNode() {
		null
	}
	
	override initializeGraphics() {
		super.initializeGraphics()
	}

	override doActivate() {
		addBehavior(new MoveBehavior(this))
		onMouseEntered = [
			originalEffect = node.effect
			node.effect = mouseOverEffect ?: originalEffect
		]
		onMouseExited = [
			node.effect = originalEffect
		]
		labels.addInitializingListener(new InitializingListListener => [
			add = [ XLabel it | it.activate ]
		])
		if(node instanceof XActivatable)
			(node as XActivatable).activate
	}
	
	override selectionFeedback(boolean isSelected) {
		if (isSelected) {
			effect = selectionEffect
			if(!scaleXProperty.bound) {
				scaleX = 1.05
				scaleY = 1.05
			}
			toFront 
		} else {
			effect = null
			if(!scaleXProperty.bound) {
				scaleX = 1.0
				scaleY = 1.0
			}
		}
	}
	
	override toFront() {
		super.toFront()
		(outgoingConnections + incomingConnections).forEach[toFront]
	}
	
	override getSnapBounds() {
		node.boundsInParent
			.translate(placementGroup.layoutX, placementGroup.layoutY)
			.scale(1 / scaleX, 1 / scaleY)
	}

	def getAnchors() {
		getNode
		anchors
	}

	override minWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.minWidth(height)
	}

	override minHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.minHeight(width)
	}

	override prefWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.prefWidth(height)
	}

	override prefHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.prefHeight(width)
	}

	override maxWidth(double height) {
		if (widthProperty != null)
			widthProperty.get
		else
			super.maxWidth(height)
	}

	override maxHeight(double width) {
		if (heightProperty != null)
			heightProperty.get
		else
			super.maxHeight(width)
	}
}
