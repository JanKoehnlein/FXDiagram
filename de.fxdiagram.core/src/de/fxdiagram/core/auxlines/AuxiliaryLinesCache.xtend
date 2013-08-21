package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import java.util.Map
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.geometry.Bounds
import javafx.geometry.Orientation

import static extension de.fxdiagram.core.Extensions.*

class AuxiliaryLinesCache {

	ListChangeListener<XNode> nodesListener
	Map<XNode, ChangeListener<Number>> node2scalarListener = newHashMap
	Map<XNode, ChangeListener<Bounds>> node2boundsListener = newHashMap
	
	AuxiliaryLineMap<Bounds> leftMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> centerXMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> rightMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> topMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> centerYMap = new AuxiliaryLineMap
	AuxiliaryLineMap<Bounds> bottomMap = new AuxiliaryLineMap

	new(XDiagram diagram) {
		nodesListener = [
			while (next) {
				if (wasAdded)
					addedSubList.forEach [
						watchNode
					]
				if (wasRemoved)
					removed.forEach [
						unwatchNode
					]
			}
		]
		diagram.nodes.addListener(nodesListener)
		diagram.getNodes.forEach[watchNode]
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

	protected def atLeastTwo(Iterable<AuxiliaryLine> lines) {
		if(lines.size < 2)
			emptyList
		else 
			lines
	}

	def watchNode(XNode node) {
		val ChangeListener<Number> scalarListener = [ 
			ObservableValue<Number> scalar, Number oldValue, Number newValue | 
			updateNode(node)
		]
		val ChangeListener<Bounds> boundsListener = [ 
			ObservableValue<Bounds> scalar, Bounds oldValue, Bounds newValue | 
			updateNode(node)
		]
		node.layoutXProperty.addListener(scalarListener)
		node.layoutYProperty.addListener(scalarListener)
		node.boundsInLocalProperty.addListener(boundsListener)
		node2scalarListener.put(node, scalarListener)
		node2boundsListener.put(node, boundsListener)
		updateNode(node)
	}
	
	def updateNode(XNode node) {
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

	def unwatchNode(XNode node) {
		leftMap.removeByNode(node)
		centerXMap.removeByNode(node)
		rightMap.removeByNode(node)
		topMap.removeByNode(node)
		centerYMap.removeByNode(node)
		bottomMap.removeByNode(node)
		val boundsListener = node2boundsListener.remove(node)
		node.boundsInLocalProperty.removeListener(boundsListener)
		val scalarListener = node2scalarListener.remove(node)
		node.layoutXProperty.removeListener(scalarListener)
		node.layoutYProperty.removeListener(scalarListener)
	}

}
