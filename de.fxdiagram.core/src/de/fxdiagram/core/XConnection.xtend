package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.anchors.ArrowHead
import de.fxdiagram.core.anchors.ConnectionRouter
import de.fxdiagram.core.anchors.TriangleArrowHead
import de.fxdiagram.core.behavior.AddControlPointBehavior
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.StringDescriptor
import java.util.List
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.BoundingBox
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.MouseEvent
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
@ModelNode('source', 'target', 'kind', 'controlPoints', 'labels', 'sourceArrowHead', 'targetArrowHead', 'stroke')
class XConnection extends XDomainObjectShape {
	
	@FxProperty XNode source
	@FxProperty XNode target
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
	
	boolean isGraphicsInitialized 

	new() {
		addOppositeListeners
		connectionRouter = new ConnectionRouter(this)
	}

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
		addOppositeListeners
		targetArrowHead = new TriangleArrowHead(this, false)
		connectionRouter = new ConnectionRouter(this)
	}
	
	new(XNode source, XNode target, DomainObjectDescriptor domainObject) {
		this(domainObject)
		this.sourceProperty.set(source)
		this.targetProperty.set(target)
	}
	
	new(XNode source, XNode target) {
		this(source, target, new StringDescriptor(source.name + '->' + target.name))
	}
	
	/**
	 * Adds listeners that keep bi-directional references in sync
	 */
	protected def addOppositeListeners() {
		sourceProperty.addListener [ p, oldSource, newSource |
			oldSource?.outgoingConnections?.remove(this)
			if(newSource != null && !newSource.outgoingConnections.contains(this))
				newSource.outgoingConnections.add(this)
			needsLayout = true
		]
		targetProperty.addListener [ p, oldTarget, newTarget |
			oldTarget?.incomingConnections?.remove(this)
			if(newTarget != null && !newTarget.incomingConnections.contains(this))
				newTarget.incomingConnections.add(this)
			needsLayout = true
		]
		labelsProperty.addListener(new InitializingListListener<XConnectionLabel> => [
			add = [ connection = this ]
			remove = [ connection = null ]
		])
	}
	
	override getNode() {
		if(nodeProperty.get == null) {
			val newNode = createNode
			if(newNode != null) {
				nodeProperty.set(newNode)
				children.add(0, newNode)
			}
		}
		nodeProperty.get
	}
	
	protected override createNode() {		
		val node = shapeGroup 
		children += controlPointGroup => [
			visible = false
		]
		connectionRouter.calculatePoints
		node
	}
	
	override initializeGraphics() {
		if(isGraphicsInitialized)
			return;
		super.initializeGraphics()
		val arrowHeadListener = new InitializingListener<ArrowHead> => [
			set = [ 
				if(!this.diagram.connectionLayer.children.contains(it)) {
					it.initializeGraphics
					this.diagram.connectionLayer.children += it
				}
			]
			unset = [ this.diagram.connectionLayer.children -= it ]
		]
		val labelListener = new InitializingListListener<XConnectionLabel> => [
			add = [ 
				if(!this.diagram.connectionLayer.children.contains(it)) {
					it.activate 
					this.diagram.connectionLayer.children += it
				}
			]
			remove = [ this.diagram.connectionLayer.children -= it ]
		]
		labelsProperty.addInitializingListener(labelListener)
		sourceArrowHeadProperty.addInitializingListener(arrowHeadListener)
		targetArrowHeadProperty.addInitializingListener(arrowHeadListener)
		isGraphicsInitialized = true
	}
	
	override doActivate() {
		if(stroke == null) 
			stroke = diagram.connectionPaint
		controlPointListener = [ prop, oldVal, newVal |
			updateShapes
		]
		val ChangeListener<Boolean> controlPointSelectionListener = [  prop, oldVal, newVal |
			if(!newVal && !controlPoints.exists[selected]) 
				hideControlPoints
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
				selectedProperty.addListener(controlPointSelectionListener)
			]
			remove = [
				layoutXProperty.removeListener(controlPointListener)
				layoutYProperty.removeListener(controlPointListener)
				selectedProperty.removeListener(controlPointSelectionListener)
			]		
		])
		labels.forEach[activate]
		connectionRouter.activate
		addBehavior(new AddControlPointBehavior(this))
		updateShapes
	}
	
	override select(MouseEvent it) {
		val wasSelected = selected
		super.select(it)
		val mousePos = sceneToLocal(sceneX, sceneY)
		var boolean controlPointPicked = false
		for(controlPoint: controlPoints) {
			if(controlPoint.boundsInParent.contains(mousePos)) {
				controlPoint.selected = true
				controlPoint.getBehavior(MoveBehavior)?.startDrag(screenX, screenY)
				controlPointPicked = true
			}
		}
		if(!controlPointPicked && (kind==POLYLINE || wasSelected)) {
			getBehavior(AddControlPointBehavior)
				?.addControlPoint(sceneToLocal(sceneX, sceneY))
		}
	}
	
	override selectionFeedback(boolean isSelected) {
		if(isSelected) {
			toFront
			showControlPoints			
		} else if(!controlPoints.exists[selected]) {
			hideControlPoints
		}
	}
	
	def showControlPoints() {
		controlPointGroup.visible = true
	}

	def hideControlPoints() {
		controlPointGroup.visible = false
	}
	
	override toFront() {
		super.toFront()
		sourceArrowHead?.toFront
		targetArrowHead?.toFront
		labels.forEach[toFront]	
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
						curves += new CubicCurve 
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
						curves += new QuadCurve
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
			kind = POLYLINE
			val polyline = shapeGroup.children.filter(Polyline).head 
				?: new Polyline
			polyline.points.setAll(controlPoints.map[#[layoutX, layoutY]].flatten)
			shapes = #[polyline]
		}
		controlPointGroup.children.setAll(controlPoints)
	}
	
	def protected setShapes(List<? extends Shape> shapes) {
		shapeGroup.children.setAll(shapes)
		if(diagram == null)
			return
		val strokeBoundsInRoot = source.localToRootDiagram(new BoundingBox(0, 0, this.strokeWidth, this.strokeWidth))
		val strokeInRoot = 0.5 * (strokeBoundsInRoot.width + strokeBoundsInRoot.height)
		val strokeScale = strokeWidth / strokeInRoot 
		shapes.forEach [ shape |
			shape.fill = null
			shape.strokeLineCap = StrokeLineCap.ROUND
			shape.strokeWidthProperty.bind(strokeWidthProperty * strokeScale)
			shape.opacityProperty.bind(this.opacityProperty)
			shape.strokeDashArray.setAll(this.strokeDashArray)
			shape.strokeProperty.bind(this.strokeProperty)
			shape.strokeDashOffset = this.strokeDashOffset
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
			LOG.severe(exc.class.simpleName + " in XConnection.layoutChildren() " + exc.message)
			exc.printStackTrace
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
				val index = segment as int * 2
				linear(
					line.points.get(index), line.points.get(index + 1), 
					line.points.get(index + 2), line.points.get(index + 3), 
					segment - index / 2)
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
						numSegments - 1 
					else
					 	t * numSegments
				val index = segment as int * 2
				new Point2D(line.points.get(index + 2) - line.points.get(index), 
					line.points.get(index + 3) - line.points.get(index + 1))
			}
		}
	}
	
	enum Kind {
		POLYLINE, QUAD_CURVE, CUBIC_CURVE 
	}
}


