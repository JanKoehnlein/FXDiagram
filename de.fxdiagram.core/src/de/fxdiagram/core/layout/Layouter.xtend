package de.fxdiagram.core.layout

import de.cau.cs.kieler.core.alg.BasicProgressMonitor
import de.cau.cs.kieler.core.kgraph.KEdge
import de.cau.cs.kieler.core.kgraph.KGraphElement
import de.cau.cs.kieler.core.kgraph.KGraphFactory
import de.cau.cs.kieler.core.kgraph.KNode
import de.cau.cs.kieler.kiml.AbstractLayoutProvider
import de.cau.cs.kieler.kiml.graphviz.layouter.GraphvizLayoutProvider
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout
import de.cau.cs.kieler.kiml.options.EdgeLabelPlacement
import de.cau.cs.kieler.kiml.options.EdgeRouting
import de.cau.cs.kieler.kiml.options.LayoutOptions
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.command.LazyCommand
import de.fxdiagram.core.command.MoveCommand
import de.fxdiagram.core.command.ParallelAnimationCommand
import java.util.Map
import javafx.geometry.BoundingBox
import javafx.geometry.Point2D
import javafx.util.Duration

import static de.fxdiagram.core.XConnection.Kind.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

class Layouter {

	extension KLayoutDataFactory = KLayoutDataFactory.eINSTANCE

	extension KGraphFactory = KGraphFactory.eINSTANCE

	Map<LayoutType, AbstractLayoutProvider> layoutProviders = newHashMap

	new() {
		// pre-initialize
		getLayoutProvider(LayoutType.DOT)
	}

	def LazyCommand createLayoutCommand(LayoutType type, XDiagram diagram, Duration duration) {
		createLayoutCommand(type, diagram, duration, null)
	}

	def LazyCommand createLayoutCommand(LayoutType type, XDiagram diagram, Duration duration, XShape fixed) {
		[|
			val cache = calculateLayout(type, diagram)
			return composeCommand(cache, duration, fixed, diagram)
		]
	}

	def layout(LayoutType type, XDiagram diagram, XShape fixed) {
		val cache = calculateLayout(type, diagram)
		applyLayout(cache, fixed, diagram)
	}

	protected def calculateLayout(LayoutType type, XDiagram diagram) {
		val provider = getLayoutProvider(type)
		val cache = <Object, KGraphElement>newHashMap
		diagram.layout
		val kRoot = diagram.toKRootNode(cache)
		provider.doLayout(kRoot, new BasicProgressMonitor())
		return cache
	}

	protected def AbstractLayoutProvider getLayoutProvider(LayoutType type) {
		var layoutProvider = layoutProviders.get(type)
		if (layoutProvider == null) {
			layoutProvider = new GraphvizLayoutProvider => [
				initialize(type.toString)
			]
			layoutProviders.put(type, layoutProvider)
		}
		return layoutProvider
	}

	protected def applyLayout(Map<Object, KGraphElement> map, XShape fixed, XDiagram diagram) {
		val delta = map.getDelta(fixed, diagram)
		for (entry : map.entrySet) {
			val xElement = entry.key
			val kElement = entry.value
			switch xElement {
				XNode: {
					xElement.getBehavior(MoveBehavior)?.setIsManuallyPlaced(false)
					val shapeLayout = kElement.data.filter(KShapeLayout).head
					val correction = if (kElement.isTopLevel) {
							delta
						} else {
							val insets = (kElement.eContainer as KNode).getData(KShapeLayout).insets
							new Point2D(-insets?.left, -insets?.top)
						}
					xElement.layoutX = shapeLayout.xpos - correction.x
					xElement.layoutY = shapeLayout.ypos - correction.y
				}
				XConnection: {
					xElement.labels.forEach[place(true)]
					val edgeLayout = kElement.data.filter(KEdgeLayout).head
					val layoutPoints = edgeLayout.createVectorChain.map[new Point2D(x, y)]
					val newKind = switch (edgeLayout.getProperty(LayoutOptions.EDGE_ROUTING)) {
						case EdgeRouting.SPLINES: {
							if ((layoutPoints.size - 1) % 3 == 0)
								CUBIC_CURVE
							else if ((layoutPoints.size - 1) % 2 == 0)
								QUAD_CURVE
							else
								POLYLINE
						}
						default:
							POLYLINE
					}
					xElement.kind = newKind
					if (xElement.controlPoints.size < layoutPoints.size)
						xElement.connectionRouter.growToSize(layoutPoints.size)
					else
						xElement.connectionRouter.shrinkToSize(layoutPoints.size)
					xElement.connectionRouter.splineShapeKeeperEnabled = false
					val kSource = (kElement as KEdge).source
					val correction = if (kSource.isTopLevel) {
							delta
						} else {
							val insets = (kSource.eContainer as KNode).getData(KShapeLayout).insets
							new Point2D(delta.x - insets?.left, delta.y - insets?.top)							
						}
					val xLayoutPoints = layoutPoints.layoutPointsInRoot(kSource).toList.map[it - correction]
					for (i : 1 ..< layoutPoints.size - 1) {
						val controlPoint = xElement.controlPoints.get(i)
						controlPoint.layoutX = xLayoutPoints.get(i).x
						controlPoint.layoutY = xLayoutPoints.get(i).y
					}
				}
			}
		}
	}

	protected def composeCommand(Map<Object, KGraphElement> map, Duration duration, XShape fixed, XDiagram diagram) {
		val composite = new ParallelAnimationCommand
		val delta = map.getDelta(fixed, diagram)
		for (entry : map.entrySet) {
			val xElement = entry.key
			val kElement = entry.value
			switch xElement {
				XNode: {
					xElement.getBehavior(MoveBehavior).isManuallyPlaced = false
					val shapeLayout = kElement.data.filter(KShapeLayout).head
					val correction = if (kElement.isTopLevel) {
							delta					
						} else {
							val insets = (kElement.eContainer as KNode).getData(KShapeLayout).insets
							new Point2D(-insets?.left, -insets?.top)
						}
					composite += new MoveCommand(
						xElement,
						shapeLayout.xpos - correction.x,
						shapeLayout.ypos - correction.y
					) => [
						executeDuration = duration
					]
				}
				XConnection: {
					xElement.labels.forEach[place(true)]
					val edgeLayout = kElement.data.filter(KEdgeLayout).head
					val layoutPoints = edgeLayout.createVectorChain.map[new Point2D(x, y)]
					val newKind = switch (edgeLayout.getProperty(LayoutOptions.EDGE_ROUTING)) {
						case EdgeRouting.SPLINES: {
							if ((layoutPoints.size - 1) % 3 == 0)
								CUBIC_CURVE
							else if ((layoutPoints.size - 1) % 2 == 0)
								QUAD_CURVE
							else
								POLYLINE
						}
						default:
							POLYLINE
					}
					xElement.connectionRouter.splineShapeKeeperEnabled = false
					val kSource = (kElement as KEdge).source
					val correction = if (kSource.isTopLevel) {
							delta
						} else {
							val insets = (kSource.eContainer as KNode).getData(KShapeLayout).insets
							new Point2D(delta.x - insets?.left, delta.y - insets?.top)
						} 
					val xLayoutPoints = layoutPoints.layoutPointsInRoot(kSource).toList.map[it - correction]
					composite += new ConnectionMorphCommand(xElement, newKind, xLayoutPoints) => [
						executeDuration = duration
					]
				}
			}
		}
		return composite
	}

	protected def boolean isTopLevel(KGraphElement kElement) {
		switch kElement {
			KNode:
				kElement?.eContainer?.eContainer == null
			KEdge:
				kElement.source.isTopLevel
			default:
				false
		}
	}

	protected def Iterable<Point2D> layoutPointsInRoot(Iterable<Point2D> layoutPoints, KNode refKNode) {
		val parentKNode = refKNode.eContainer
		if (parentKNode instanceof KNode) {
			val pos = parentKNode.data.filter(KShapeLayout).head
			layoutPointsInRoot(layoutPoints.map[new Point2D(pos.xpos + x, pos.ypos + y)], parentKNode)
		} else {
			layoutPoints
		}
	}

	protected def getDelta(Map<Object, KGraphElement> map, XShape xFixed, XDiagram diagram) {
		if (xFixed == null) {
			val newCenter = map.values.filter(KNode).map [
				val layout = data.filter(KShapeLayout).head
				new BoundingBox(layout.xpos, layout.ypos, layout.width, layout.height)
			].reduce[$0 + $1]?.center
			if (newCenter != null) {
				val scene = diagram.scene
				if (scene == null) {
					return newCenter
				} else {
					val currentCenter = diagram.sceneToLocal(0.5 * scene.width, 0.5 * scene.height)
					return newCenter - currentCenter
				}
			}
		}
		val kFixed = map.get(xFixed)
		switch kFixed {
			KNode: {
				val shapeLayout = kFixed.data.filter(KShapeLayout).head
				return new Point2D(shapeLayout.xpos - xFixed.layoutBounds.minX - xFixed.layoutX,
					shapeLayout.ypos - xFixed.layoutBounds.minY - xFixed.layoutY)
			}
			KEdge: {
				val edgeLayout = kFixed.data.filter(KEdgeLayout).head
				val edgeCenter = edgeLayout.createVectorChain.map [
					new BoundingBox(x, y, 0, 0)
				].reduce[$0 + $1]?.center
				if (edgeCenter != null)
					return edgeCenter
			}
		}
		return new Point2D(0, 0)
	}

	protected def toKRootNode(XDiagram it, Map<Object, KGraphElement> cache) {
		val kRoot = createKNode
		val shapeLayout = createKShapeLayout
		shapeLayout.insets = createKInsets
		shapeLayout.setProperty(LayoutOptions.DEBUG_MODE, true)
		shapeLayout.setProperty(LayoutOptions.LAYOUT_HIERARCHY, true)
//		shapeLayout.setProperty(LayoutOptions.BORDER_SPACING, 20f)
		kRoot.data += shapeLayout
		cache.put(it, kRoot)
		nodes.forEach [
			kRoot.children += toKNode(cache)
		]
		var spacing = max(max(60.0, transformConnections(connections, cache)), transformNestedConnections(nodes, cache))
		shapeLayout.setProperty(LayoutOptions.SPACING, spacing as float)
		kRoot
	}

	protected def double transformConnections(Iterable<XConnection> connections, Map<Object, KGraphElement> cache) {
		var spacing = 0.0
		for (it : connections) {
			toKEdge(cache)
			var minLength = 0.0
			for (label : labels)
				minLength += label.boundsInLocal.width
			if (sourceArrowHead != null)
				minLength += sourceArrowHead.width
			if (targetArrowHead != null)
				minLength += targetArrowHead.width
			spacing = max(spacing, minLength)
		}
		spacing
	}

	protected def double transformNestedConnections(Iterable<XNode> nodes, Map<Object, KGraphElement> cache) {
		nodes.filter(XDiagramContainer).filter[isInnerDiagramActive].map [
			max(transformConnections(innerDiagram.connections, cache),
				transformNestedConnections(innerDiagram.nodes, cache))
		].fold(0.0, [max($0, $1)])
	}

	protected def KNode toKNode(XNode xNode, Map<Object, KGraphElement> cache) {
		val kNode = createKNode
		val shapeLayout = createKShapeLayout
		val autoLayoutDimension = xNode.autoLayoutDimension
		shapeLayout.setSize(
			autoLayoutDimension.width as float,
			autoLayoutDimension.height as float
		)
		kNode.data += shapeLayout
		cache.put(xNode, kNode)
		if (xNode instanceof XDiagramContainer) {
			if (xNode.isInnerDiagramActive) {
				shapeLayout.insets = createKInsets => [
					left = xNode.insets?.left as float
					right = 0 // xNode.insets?.right as float
					top = xNode.insets?.top as float
					bottom = 0 // xNode.insets?.bottom as float
				]
				xNode.innerDiagram.nodes.forEach [
					kNode.children += toKNode(cache)
				]
			}
		}
		kNode
	}

	protected def toKEdge(XConnection it, Map<Object, KGraphElement> cache) {
		val kSource = cache.get(source)
		val kTarget = cache.get(target)
		if (kSource instanceof KNode && kTarget instanceof KNode) {
			val kEdge = createKEdge;
			(kSource as KNode).outgoingEdges += kEdge;
			(kTarget as KNode).incomingEdges += kEdge;
			val edgeLayout = createKEdgeLayout
			edgeLayout.sourcePoint = createKPoint
			edgeLayout.targetPoint = createKPoint
			kEdge.data += edgeLayout
			cache.put(it, kEdge)
			// labels.forEach[ toKLabel(cache).parent = kEdge ]
			kEdge
		} else {
			null
		}
	}

	protected def toKLabel(XConnectionLabel it, Map<Object, KGraphElement> cache) {
		val kLabel = createKLabel
		kLabel.text = it?.text?.text ?: ''
		val shapeLayout = createKShapeLayout
		shapeLayout.setSize(
			layoutBounds.width as float,
			layoutBounds.height as float
		)
		// HACK: enlarge font size to increase label distance
		shapeLayout.setProperty(LayoutOptions.FONT_SIZE, 12)
		shapeLayout.setProperty(LayoutOptions.EDGE_LABEL_PLACEMENT, EdgeLabelPlacement.CENTER)
		kLabel.data += shapeLayout
		cache.put(it, kLabel)
		kLabel
	}
}

enum LayoutType {
	DOT,
	NEATO
}

