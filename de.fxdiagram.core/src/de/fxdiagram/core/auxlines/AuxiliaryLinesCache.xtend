package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.extensions.InitializingListListener
import java.util.Map
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Bounds
import javafx.geometry.Orientation

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

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
		diagram.nodes.addInitializingListener(nodesListener)
		diagram.connections.addInitializingListener(connectionsListener)
	}

	def getAuxiliaryLines(XNode node) {
		val boundsInDiagram = node.localToDiagram(node.snapBounds)
		leftMap.getByPosition(boundsInDiagram.minX).atLeastTwo
			+ centerXMap.getByPosition(0.5 * (boundsInDiagram.minX + boundsInDiagram.maxX)).atLeastTwo
			+ rightMap.getByPosition(boundsInDiagram.maxX).atLeastTwo
			+ topMap.getByPosition(boundsInDiagram.minY).atLeastTwo
			+ centerYMap.getByPosition(0.5 * (boundsInDiagram.minY + boundsInDiagram.maxY)).atLeastTwo
			+ bottomMap.getByPosition(boundsInDiagram.maxY).atLeastTwo
	}
	
	def getAuxiliaryLines(XControlPoint point) {
		val centerInDiagram = point.localToDiagram(point.boundsInLocal.center)
		centerXMap.getByPosition(centerInDiagram.x).atLeastTwo
			+ centerYMap.getByPosition(centerInDiagram.y).atLeastTwo
	}

	protected def atLeastTwo(Iterable<AuxiliaryLine> lines) {
		if(lines.size < 2)
			emptyList
		else 
			lines
	}

	protected def watchNode(XNode node) {
		val ChangeListener<Number> scalarListener = [ 
			ObservableValue<? extends Number> scalar, Number oldValue, Number newValue | 
			updateNode(node)
		]
		val ChangeListener<Bounds> boundsListener = [ 
			ObservableValue<? extends Bounds> scalar, Bounds oldValue, Bounds newValue | 
			updateNode(node)
		]
		node.layoutXProperty.addListener(scalarListener)
		node.layoutYProperty.addListener(scalarListener)
		node.boundsInLocalProperty.addListener(boundsListener)
		shape2scalarListener.put(node, scalarListener)
		node2boundsListener.put(node, boundsListener)
		updateNode(node)
	}
	
	protected def updateNode(XNode node) {
		val boundsInDiagram = node.localToDiagram(node.snapBounds)
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
		shape2scalarListener.put(controlPoint, scalarListener)
		updateControlPoint(controlPoint)
	}
	
	protected def updateControlPoint(XControlPoint point) {
		val boundsInDiagram = point.localToDiagram(point.boundsInLocal)
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
	
	protected def unwatchControlPoint(XControlPoint point) {
		centerXMap.removeByShape(point)
		centerYMap.removeByShape(point)
		val scalarListener = shape2scalarListener.remove(point)
		point.layoutXProperty.removeListener(scalarListener)
		point.layoutYProperty.removeListener(scalarListener)
	}
	

}
