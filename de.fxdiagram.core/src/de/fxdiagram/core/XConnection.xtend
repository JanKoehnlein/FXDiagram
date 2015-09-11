package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.anchors.ArrowHead
import de.fxdiagram.core.anchors.ConnectionRouter
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.StringDescriptor
import java.util.List
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.BoundingBox
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Polyline
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Shape
import javafx.scene.shape.StrokeLineCap

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.extensions.Point2DExtensions.*
import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.BezierExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DoubleExpressionExtensions.*
/**
 * A line connecting two {@link XNode}s.
 * 
 * A {@link XConnection} is always directed, i.e. it has a dedicated {@link #source} and {@link #target}.
 * These properties are automatically kept in sync with their counterparts {@link XNode#outgoingConnections}
 * and {@link XNode#incomingConnections}.
 * 
 * Independent of {@link #source} and {@link #target}, it can have {@link ArrowHead}s at the respective ends. 
 * It may also have a {@link XconnectionLabel}. The actual shape of a connection is determined by its 
 * {@link ConnectionRouter} and its {@link Kind}.
 * 
 * A connection can refer to a {@link #domainObject} as its underlying semantic element.
 * 
 * Clients usually don't extend this class, but configure its label and appearance properties. 
 */
@Logging
@ModelNode('source', 'target', 'kind', 'controlPoints', 'labels', 'sourceArrowHead', 'targetArrowHead')
class XConnection extends XDomainObjectShape {
	
	@FxProperty(readOnly=true) XNode source
	@FxProperty(readOnly=true) XNode target
	@FxProperty ObservableList<XConnectionLabel> labels = observableArrayList
	@FxProperty ArrowHead sourceArrowHead
	@FxProperty ArrowHead targetArrowHead
	@FxProperty Kind kind = POLYLINE
	@FxProperty(readOnly=true) ObservableList<XControlPoint> controlPoints = FXCollections.observableArrayList
	
	@FxProperty double strokeWidth = 2.0
	@FxProperty Paint stroke
	@FxProperty double strokeDashOffset = 0.0
	@FxProperty ObservableList<Double> strokeDashArray = observableArrayList 

	Group controlPointGroup = new Group
	Group shapeGroup = new Group

	ChangeListener<Number> controlPointListener
	
	@FxProperty ConnectionRouter connectionRouter

	new() {
		targetArrowHead = new TriangleArrowHead(this, false)
	}

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}
	
	new(XNode source, XNode target, DomainObjectDescriptor domainObject) {
		this(domainObject)
		this.source = source
		this.target = target
	}
	
	new(XNode source, XNode target) {
		this(source, target, new StringDescriptor(source.name + '->' + target.name))
	}
	
	def void setSource(XNode source) {
		if(getSource != null)
			getSource.outgoingConnections.remove(this)
		sourceProperty.set(source)
		if(source != null && !source.outgoingConnections.contains(this))
			source.outgoingConnections.add(this)
	}
	
	def void setTarget(XNode target) {
		if(getTarget != null)
			getTarget.incomingConnections.remove(this)
		targetProperty.set(target)
		if(target != null && !target.incomingConnections.contains(this))
			target.incomingConnections.add(this)
	}
	
	protected override createNode() {		
		val node = shapeGroup 
		children += controlPointGroup => [
			visible = false
		]
		connectionRouter = new ConnectionRouter(this)
		connectionRouter.calculatePoints
		node
	}
	
	override doActivate() {
		if(stroke == null) 
			stroke = diagram.connectionPaint
		controlPointListener = [ prop, oldVal, newVal |
			updateShapes
		]
		controlPoints.addInitializingListener(new InitializingListListener() => [
			change = [
				updateShapes
			]
			add = [ XControlPoint it | // Oops, have to declare 'it'
				val index = controlPoints.indexOf(it)
				if(index == 0 || index == controlPoints.size()-1)
					initializeGraphics  // Oops, have to write 'it'
				else 
					activate
				layoutXProperty.addListener(controlPointListener)
				layoutYProperty.addListener(controlPointListener)
			]
			remove = [
				layoutXProperty.removeListener(controlPointListener)
				layoutYProperty.removeListener(controlPointListener)
			]		
		]);
		labels.forEach[activate]
		connectionRouter.activate
		updateShapes
		parentProperty.addListener [
			property, oldValue, newValue |
			if(newValue == null) {
				source.outgoingConnections.remove(this)
				target.incomingConnections.remove(this)
			}
		]
	}
	
	override selectionFeedback(boolean isSelected) {
		if(isSelected) {
			source.toFront
			target.toFront
		}
		controlPointGroup.visible = isSelected
	}
	
	def void updateShapes() {
		var remainder = -1
		switch kind {
			case CUBIC_CURVE: {
				remainder = (controlPoints.size - 1) % 3
				if(remainder == 0) {
					val numSegments = (controlPoints.size - 1) / 3
					val curves = shapeGroup.children.filter(CubicCurve).toList
					while(curves.size > numSegments) 
						curves.remove(curves.last)
					while(curves.size < numSegments)
						curves += new CubicCurve => [
							fill = null
							stroke = Color.BLACK
						]
					for(i: 0..<numSegments) {
						val curve = curves.get(i)
						val offset = i * 3
						curve.startX = controlPoints.get(offset).layoutX
						curve.startY = controlPoints.get(offset).layoutY
						curve.controlX1 = controlPoints.get(offset+1).layoutX
						curve.controlY1 = controlPoints.get(offset+1).layoutY
						curve.controlX2 = controlPoints.get(offset+2).layoutX
						curve.controlY2 = controlPoints.get(offset+2).layoutY
						curve.endX = controlPoints.get(offset+3).layoutX
						curve.endY = controlPoints.get(offset+3).layoutY
					}
					shapes = curves
				}	
			}
			case QUAD_CURVE: {
				remainder = (controlPoints.size - 1) % 2 
				if(remainder == 0) {
					val numSegments = (controlPoints.size - 1) / 2
					val curves = shapeGroup.children.filter(QuadCurve).toList
					while(curves.size > numSegments) 
						curves.remove(curves.last)
					while(curves.size < numSegments)
						curves += new QuadCurve => [
							fill = null
							stroke = Color.BLACK
						]
					for(i: 0..<numSegments) {
						val curve = curves.get(i)
						val offset = i * 2
						curve.startX = controlPoints.get(offset).layoutX
						curve.startY = controlPoints.get(offset).layoutY
						curve.controlX = controlPoints.get(offset+1).layoutX
						curve.controlY = controlPoints.get(offset+1).layoutY
						curve.endX = controlPoints.get(offset+2).layoutX
						curve.endY = controlPoints.get(offset+2).layoutY
					}
					shapes = curves
				}
			}
		}
		if (remainder != 0) {
			val polyline = shapeGroup.children.filter(Polyline).head 
				?: new Polyline => [ 
					stroke = Color.BLACK
				]
			polyline.points.setAll(controlPoints.map[#[layoutX, layoutY]].flatten)
			shapes = #[polyline]
		}
		controlPointGroup.children.setAll(controlPoints)
	}
	
	def protected setShapes(List<? extends Shape> shapes) {
		shapeGroup.children.setAll(shapes)
		val strokeBoundsInRoot = source.localToRootDiagram(new BoundingBox(0, 0, this.strokeWidth, this.strokeWidth))
		val strokeInRoot = 0.5 * (strokeBoundsInRoot.width + strokeBoundsInRoot.height)
		val strokeScale = strokeWidth / strokeInRoot 
		shapes.forEach [
			stroke = this.stroke
			strokeLineCap = StrokeLineCap.ROUND
			it.strokeWidthProperty.bind(strokeWidthProperty * strokeScale)
			strokeDashArray.setAll(this.strokeDashArray)
			strokeDashOffset = this.strokeDashOffset
		]
	}
	
	override isSelectable() {
		isActive
	}

	override layoutChildren() {
		super.layoutChildren
		try {
			connectionRouter.calculatePoints
			labels.forEach[ place(false) ]	
			sourceArrowHead?.place
			targetArrowHead?.place
		} catch(Exception exc) {
			// TODO fix control flow
			LOG.severe("Exception in XConnection.layoutChildren() " + exc.message)
		}
	}
	
	def at(double t) {
		if(t < 0 || t > 1)
			throw new IllegalArgumentException("Argument must be between 0 and 1") 
		if(t==1) 
			return new Point2D(controlPoints.last.layoutX, controlPoints.last.layoutY)
		switch (kind) {
			case CUBIC_CURVE: {
				val curves = shapeGroup.children.filter(CubicCurve)
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.at(segment - index)
			}
			case QUAD_CURVE: {
				val curves = shapeGroup.children.filter(QuadCurve)
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.at(segment - index)
			}
			case POLYLINE: {
				val line = shapeGroup.children.filter(Polyline).head
				val numSegments = (line.points.size / 2 - 1)
				val segment = t * numSegments
				val index = segment as int
				linear(
					line.points.get(index), line.points.get(index + 1), 
					line.points.get(index + 2), line.points.get(index + 3), 
					segment - index)
			}
		}
	}
	
	def derivativeAt(double t) {
		if(t < 0 || t > 1)
			throw new IllegalArgumentException("Argument must be between 0 and 1")
		switch (kind) {
			case CUBIC_CURVE: {
				val curves = shapeGroup.children.filter(CubicCurve)
				if (t==1) 
					return curves.last.derivativeAt(1)					
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.derivativeAt(segment - index)
			}
			case QUAD_CURVE: {
				val curves = shapeGroup.children.filter(QuadCurve)
				if (t==1) 
					return curves.last.derivativeAt(1)					
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.derivativeAt(segment - index)
			}
			case POLYLINE: {
				val line = shapeGroup.children.filter(Polyline).head
				val numSegments = (line.points.size / 2 - 1)
				val segment = if(t == 1)
						line.points.size -4
					else
					 	t * numSegments
				val index = segment as int
				new Point2D(line.points.get(index + 2) - line.points.get(index), 
					line.points.get(index + 3) - line.points.get(index + 1))
			}
		}
	}
	
	enum Kind {
		POLYLINE, QUAD_CURVE, CUBIC_CURVE 
	}
}


