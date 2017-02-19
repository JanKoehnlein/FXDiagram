package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.extensions.InitializingListListener
import java.util.Map
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Bounds
import javafx.geometry.Orientation
import javafx.geometry.Point2D

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import javafx.geometry.BoundingBox

class AuxiliaryLinesCache {

	InitializingListListener<XNode> nodesListener
	InitializingListListener<XConnection> connectionsListener
	InitializingListListener<XControlPoint> controlPointsListener
	
	Map<XShape, ChangeListener<Number>> shape2scalarListener = newHashMap
	Map<XNode, ChangeListener<Bounds>> node2boundsListener = newHashMap
	
	AuxiliaryLineMap<Bounds> leftMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> centerXMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> rightMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> topMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> centerYMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> bottomMap = new AuxiliaryLineMap
	
	double magnetDist = 10

	new(XDiagram diagram) {
		nodesListener = new InitializingListListener<XNode>() => [
			add = [ watchNode ]
			remove = [unwatchNode]	
		]
		connectionsListener = new InitializingListListener<XConnection>() => [
			add = [ watchConnection ]
			remove = [ unwatchConnection ]
		]
		controlPointsListener = new InitializingListListener<XControlPoint>() => [
			add = [ watchControlPoint ]
			remove = [ unwatchControlPoint ]
		]
		watchDiagram(diagram)
	}

	def getAuxiliaryLines(XNode node) {
		val boundsInDiagram = node.localToRootDiagram(node.snapBounds)
		leftMap.getByPosition(boundsInDiagram.minX).atLeastTwo
			+ centerXMap.getByPosition(0.5 * (boundsInDiagram.minX + boundsInDiagram.maxX)).atLeastTwo
			+ rightMap.getByPosition(boundsInDiagram.maxX).atLeastTwo
			+ topMap.getByPosition(boundsInDiagram.minY).atLeastTwo
			+ centerYMap.getByPosition(0.5 * (boundsInDiagram.minY + boundsInDiagram.maxY)).atLeastTwo
			+ bottomMap.getByPosition(boundsInDiagram.maxY).atLeastTwo
	}
	
	def getAuxiliaryLines(XControlPoint point) {
		val centerInDiagram = point.localToRootDiagram(point.boundsInLocal.center)
		centerXMap.getByPosition(centerInDiagram.x).atLeastTwo
			+ centerYMap.getByPosition(centerInDiagram.y).atLeastTwo
	}

	protected def atLeastTwo(Iterable<AuxiliaryLine> lines) {
		if(lines.size < 2)
			emptyList
		else 
			lines
	}
	
	def getSnappedPosition(XNode node, Point2D newPositionInDiagram) {
		val boundsInDiagram = node.localToDiagram(node.snapBounds)
		val newBoundsInDiagram = new BoundingBox(
			newPositionInDiagram.x - node.layoutX + boundsInDiagram.minX,
			newPositionInDiagram.y - node.layoutY + boundsInDiagram.minY,
			boundsInDiagram.width,
			boundsInDiagram.height
		) 
		val boundsInRootDiagram = node.diagram.localToRootDiagram(newBoundsInDiagram)
		val excluded = <XShape>newHashSet
		excluded += node
		node.outgoingConnections.forEach[
			if(kind == XConnection.Kind.RECTILINEAR && controlPoints.size > 2)
				excluded += controlPoints.get(1)
		]
		node.incomingConnections.forEach[
			if(kind == XConnection.Kind.RECTILINEAR && controlPoints.size > 2)
				excluded += controlPoints.get(controlPoints.size - 2)
		]
		var dx = magnetDist
		dx = centerXMap.getNearestLineDelta(0.5 * (boundsInRootDiagram.minX + boundsInRootDiagram.maxX), dx, excluded)
		dx = leftMap.getNearestLineDelta(boundsInRootDiagram.minX, dx, excluded)
		dx = rightMap.getNearestLineDelta(boundsInRootDiagram.maxX, dx, excluded)

		var dy = magnetDist
		dy = centerYMap.getNearestLineDelta(0.5 * (boundsInRootDiagram.minY + boundsInRootDiagram.maxY), dy, excluded)
		dy = topMap.getNearestLineDelta(boundsInRootDiagram.minY, dy, excluded)
		dy = bottomMap.getNearestLineDelta(boundsInRootDiagram.maxY, dy, excluded)
		return toLocal(dx, dy, newPositionInDiagram, node)
	}
	
	def getSnappedPosition(XControlPoint point, Point2D newPositionInDiagram) {
		val centerInDiagram = point.parent.localToRootDiagram(newPositionInDiagram)
		val dx = centerXMap.getNearestLineDelta(centerInDiagram.x, magnetDist, #{point})
		val dy = centerYMap.getNearestLineDelta(centerInDiagram.y, magnetDist, #{point})
		return toLocal(dx, dy, newPositionInDiagram, point)
	}

	protected def toLocal(double dx, double dy, Point2D newPointInDiagram, XShape shape) {
		val deltaX = if(dx >= magnetDist)
				0
			else 
				dx
		val deltaY = if(dy >= magnetDist)
				0
			else 
				dy		
		val delta = new Point2D(deltaX, deltaY)
		if(delta.norm < EPSILON)
			return newPointInDiagram
		val deltaLocal = shape.parent.sceneToLocal(shape.getRootDiagram.localToScene(delta))
		return new Point2D(newPointInDiagram.x + deltaLocal.x, newPointInDiagram.y + deltaLocal.y)
	}
	
	protected def watchDiagram(XDiagram diagram) {
		diagram.nodes.addInitializingListener(nodesListener)
		diagram.connections.addInitializingListener(connectionsListener)
	}
	
	protected def updateDiagram(XDiagram diagram) {
		diagram.nodes.forEach[updateNode]
		diagram.connections.map[controlPoints].flatten.forEach[updateControlPoint]
	}
	
	protected def unwatchDiagram(XDiagram diagram) {
		diagram.nodes.removeInitializingListener(nodesListener)
		diagram.connections.removeInitializingListener(connectionsListener)
	}

	protected def watchNode(XNode node) {
		val ChangeListener<Number> scalarListener = [ 
			ObservableValue<? extends Number> scalar, Number oldValue, Number newValue | 
			updateNode(node)
			if(node instanceof XDiagramContainer) {
				if(node.isInnerDiagramActive) 
					updateDiagram(node.innerDiagram)
			}
		]
		val ChangeListener<Bounds> boundsListener = [ 
			ObservableValue<? extends Bounds> scalar, Bounds oldValue, Bounds newValue | 
			updateNode(node)
			if(node instanceof XDiagramContainer) {
				if(node.isInnerDiagramActive) 
					updateDiagram(node.innerDiagram)
			}
		]
		node.layoutXProperty.addListener(scalarListener)
		node.layoutYProperty.addListener(scalarListener)
		node.boundsInLocalProperty.addListener(boundsListener)
		shape2scalarListener.put(node, scalarListener)
		node2boundsListener.put(node, boundsListener)
		updateNode(node)
		if(node instanceof XDiagramContainer) {
			if(node.isInnerDiagramActive) 
				watchDiagram(node.innerDiagram)
		}
	}
	
	protected def updateNode(XNode node) {
		val boundsInDiagram = node.localToRootDiagram(node.snapBounds)
		leftMap.add(
			new NodeLine(
				boundsInDiagram.minX,
				Orientation.VERTICAL,
				node,
				boundsInDiagram))
		centerXMap.add(
			new NodeLine(
				0.5 * ( boundsInDiagram.minX + boundsInDiagram.maxX),
				Orientation.VERTICAL,
				node,
				boundsInDiagram))
		rightMap.add(
			new NodeLine(
				boundsInDiagram.maxX,
				Orientation.VERTICAL,
				node,
				boundsInDiagram))
		topMap.add(
			new NodeLine(
				boundsInDiagram.minY,
				Orientation.HORIZONTAL,
				node,
				boundsInDiagram))
		centerYMap.add(
			new NodeLine(
				0.5 * ( boundsInDiagram.minY + boundsInDiagram.maxY),
				Orientation.HORIZONTAL,
				node,
				boundsInDiagram))
		bottomMap.add(
			new NodeLine(
				boundsInDiagram.maxY,
				Orientation.HORIZONTAL,
				node,
				boundsInDiagram))
	}

	protected def unwatchNode(XNode node) {
		if(node instanceof XDiagramContainer) {
			if(node.isInnerDiagramActive) 
				unwatchDiagram(node.innerDiagram)
		}
		leftMap.removeByShape(node)
		centerXMap.removeByShape(node)
		rightMap.removeByShape(node)
		topMap.removeByShape(node)
		centerYMap.removeByShape(node)
		bottomMap.removeByShape(node)
		val boundsListener = node2boundsListener.remove(node)
		node.boundsInLocalProperty.removeListener(boundsListener)
		val scalarListener = shape2scalarListener.remove(node)
		node.layoutXProperty.removeListener(scalarListener)
		node.layoutYProperty.removeListener(scalarListener)
	}
	
	protected def watchConnection(XConnection connection) {
		connection.controlPoints.addInitializingListener(controlPointsListener)
	}
	
	protected def unwatchConnection(XConnection connection) {
		connection.controlPoints.removeInitializingListener(controlPointsListener)
	}
	
	protected def watchControlPoint(XControlPoint controlPoint) {
		val ChangeListener<Number> scalarListener = [ 
			ObservableValue<? extends Number> scalar, Number oldValue, Number newValue | 
			updateControlPoint(controlPoint)
		]
		controlPoint.layoutXProperty.addListener(scalarListener)
		controlPoint.layoutYProperty.addListener(scalarListener)
		controlPoint.typeProperty.addListener [ p, o, n |
			if(n == XControlPoint.Type.ANCHOR)
				centerXMap.removeByShape(controlPoint)
			else 
				updateControlPoint(controlPoint)
		]
		shape2scalarListener.put(controlPoint, scalarListener)
		updateControlPoint(controlPoint)
	}
	
	protected def updateControlPoint(XControlPoint point) {
		if(point.type != XControlPoint.Type.ANCHOR) {
			val boundsInDiagram = point.localToRootDiagram(point.boundsInLocal)
			centerXMap.add(
				new NodeLine(
					boundsInDiagram.center.x,
					Orientation.VERTICAL,
					point,
					boundsInDiagram))
			centerYMap.add(
				new NodeLine(
					boundsInDiagram.center.y,
					Orientation.HORIZONTAL,
					point,
					boundsInDiagram))
		}
	}
	
	protected def unwatchControlPoint(XControlPoint point) {
		centerXMap.removeByShape(point)
		centerYMap.removeByShape(point)
		val scalarListener = shape2scalarListener.remove(point)
		if(scalarListener != null) {
			point.layoutXProperty.removeListener(scalarListener)
			point.layoutYProperty.removeListener(scalarListener)
		}
	}
}
