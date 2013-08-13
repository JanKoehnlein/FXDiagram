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
import de.fxdiagram.core.XAbstractDiagram
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XNode
import java.util.Map
import javafx.animation.Animation
import javafx.util.Duration

import static de.fxdiagram.core.XConnectionKind.*
import static java.lang.Math.*

class Layouter { 

	extension KLayoutDataFactory = KLayoutDataFactory.eINSTANCE
	
	extension KGraphFactory = KGraphFactory.eINSTANCE
	
	extension LayoutTransitionFactory = new LayoutTransitionFactory
	
	new() {
		// pre-initialize
		layoutProvider.dispose
	}
	
	def layout(XAbstractDiagram diagram, Duration duration) {
		val cache = <Object, KGraphElement> newHashMap
		val kRoot = diagram.toKRootNode(cache)
		val provider = getLayoutProvider()
		try {
			provider.doLayout(kRoot, new BasicProgressMonitor())
			apply(cache, duration)
		} finally {
			provider.dispose
		}
	}

	def AbstractLayoutProvider getLayoutProvider() {
		new LoggingTransformationService
		new GraphvizLayoutProvider => [
			initialize("DOT")
		]
	}
	
	def apply(Map<Object,KGraphElement> map, Duration duration) {
		val animations = <Animation>newArrayList
		for(entry: map.entrySet) {
			val xElement = entry.key
			val kElement = entry.value
			switch xElement { 
//				XConnectionLabel: { 
//					val shapeLayout = kElement.data.filter(KShapeLayout).head
//					animations += createTransition(xElement,
//						shapeLayout.xpos, shapeLayout.ypos, 
//						LayoutTransitionStyle.CURVE_XFIRST, duration
//					)
//				}
				XNode: { 
					val shapeLayout = kElement.data.filter(KShapeLayout).head
					animations += createTransition(xElement, shapeLayout.xpos, shapeLayout.ypos, LayoutTransitionStyle.CURVE_XFIRST, duration)
				}
				XConnection: {
					val edgeLayout = kElement.data.filter(KEdgeLayout).head
					val layoutPoints = edgeLayout.createVectorChain
					switch(edgeLayout.getProperty(LayoutOptions.EDGE_ROUTING)) {
						case EdgeRouting.SPLINES: {
							if((layoutPoints.size - 1) % 3 == 0) 
								xElement.kind = CUBIC_CURVE
							else if((layoutPoints.size - 1) % 2 == 0) 
								xElement.kind = QUAD_CURVE
							else 
								xElement.kind = POLYLINE
						}
						default:
							xElement.kind = POLYLINE
					}
					val controlPoints = xElement.controlPoints
					xElement.connectionRouter.growToSize(layoutPoints.size)
					for(i: 1..<controlPoints.size-1) {
						val layoutPoint = layoutPoints.get(min(layoutPoints.size-1, i))
						val currentControlPoint = controlPoints.get(i)
						val transition = createTransition(currentControlPoint, layoutPoint.x, layoutPoint.y, LayoutTransitionStyle.CURVE_XFIRST, duration)
						if (i == 1) {
							val unbind = transition.onFinished
							transition.onFinished = [
								unbind.handle(it)
								xElement.connectionRouter.shrinkToSize(layoutPoints.size)
							] 		
						}				
						animations += transition
					}
				}
			}
		}
		animations.forEach[play]
	}
	
	protected def toKRootNode(XAbstractDiagram it, Map<Object, KGraphElement> cache) {
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
			if(label != null) {
				label.toKLabel(cache).parent = kEdge
			}
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

