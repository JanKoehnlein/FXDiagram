package de.fxdiagram.core.layout

import de.cau.cs.kieler.core.alg.BasicProgressMonitor
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
import de.fxdiagram.core.XNode
import de.fxdiagram.core.command.MoveCommand
import java.util.Map
import javafx.geometry.Point2D
import javafx.util.Duration

import static de.fxdiagram.core.XConnection.Kind.*
import de.fxdiagram.core.command.LazyCommand
import de.fxdiagram.core.command.ParallelAnimationCommand

class Layouter { 

	extension KLayoutDataFactory = KLayoutDataFactory.eINSTANCE
	
	extension KGraphFactory = KGraphFactory.eINSTANCE
	
	new() {
		// pre-initialize
		getLayoutProvider(LayoutType.DOT).dispose
	}
	
	def LazyCommand createLayoutCommand(LayoutType type, XDiagram diagram, Duration duration) {
		[|
			val cache = <Object, KGraphElement> newHashMap
			diagram.layout
			val kRoot = diagram.toKRootNode(cache)
			val provider = getLayoutProvider(type)
			try {
				provider.doLayout(kRoot, new BasicProgressMonitor())
				return composeCommand(cache, duration)
			} finally {
				provider.dispose
			}
		]
	}
	
	protected def AbstractLayoutProvider getLayoutProvider(LayoutType type) {
		new GraphvizLayoutProvider => [
			initialize(type.toString)
		]
	}
	
	protected def composeCommand(Map<Object,KGraphElement> map, Duration duration) {
		val composite = new ParallelAnimationCommand
		for(entry: map.entrySet) {
			val xElement = entry.key
			val kElement = entry.value
			switch xElement { 
				XNode: { 
					val shapeLayout = kElement.data.filter(KShapeLayout).head
					composite += new MoveCommand(xElement, shapeLayout.xpos - xElement.layoutBounds.minX, shapeLayout.ypos - xElement.layoutBounds.minY) => [
						executeDuration = duration
					]
				}
				XConnection: {
					val edgeLayout = kElement.data.filter(KEdgeLayout).head
					val layoutPoints = edgeLayout.createVectorChain
					val newKind = switch(edgeLayout.getProperty(LayoutOptions.EDGE_ROUTING)) {
						case EdgeRouting.SPLINES: {
							if((layoutPoints.size - 1) % 3 == 0) 
								CUBIC_CURVE
							else if((layoutPoints.size - 1) % 2 == 0) 
								QUAD_CURVE
							else 
								POLYLINE
						}
						default:
							POLYLINE
					}
					composite += new ConnectionMorphCommand(xElement, newKind, layoutPoints.map[new Point2D(x,y)]) => [
						executeDuration = duration
					]
				}
			}
		}
		return composite
	}
	
	protected def toKRootNode(XDiagram it, Map<Object, KGraphElement> cache) {
		val kRoot = createKNode
		val shapeLayout = createKShapeLayout
		shapeLayout.insets = createKInsets
		shapeLayout.setProperty(LayoutOptions.SPACING, 60f)
//		shapeLayout.setProperty(LayoutOptions.DEBUG_MODE, true)
		kRoot.data += shapeLayout
		cache.put(it, kRoot)
		nodes.forEach [
			kRoot.children += toKNode(cache)
		]
		connections.forEach [
			toKEdge(cache)
		]
		kRoot
	}
	
	protected def toKNode(XNode it, Map<Object, KGraphElement> cache) {
		val kNode = createKNode
		val shapeLayout = createKShapeLayout
		shapeLayout.setSize(layoutBounds.width as float, layoutBounds.height as float)
		kNode.data += shapeLayout
		cache.put(it, kNode)
		kNode	
	}
	
	protected def toKEdge(XConnection it, Map<Object, KGraphElement> cache) {
		val kSource = cache.get(source)
		val kTarget = cache.get(target)
		if(kSource instanceof KNode && kTarget instanceof KNode) {
			val kEdge = createKEdge;
			(kSource as KNode).outgoingEdges += kEdge;
			(kTarget as KNode).incomingEdges += kEdge;
			val edgeLayout = createKEdgeLayout
			edgeLayout.sourcePoint = createKPoint
			edgeLayout.targetPoint = createKPoint
			kEdge.data += edgeLayout
			cache.put(it, kEdge)
			labels.forEach[ toKLabel(cache).parent = kEdge ]
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
	DOT, NEATO
}

