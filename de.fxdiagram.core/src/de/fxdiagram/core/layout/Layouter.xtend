package de.fxdiagram.core.layout

import de.cau.cs.kieler.core.alg.BasicProgressMonitor
import de.cau.cs.kieler.core.kgraph.KGraphElement
import de.cau.cs.kieler.core.kgraph.KGraphFactory
import de.cau.cs.kieler.core.kgraph.KNode
import de.cau.cs.kieler.kiml.graphviz.layouter.GraphvizLayoutProvider
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout
import de.cau.cs.kieler.kiml.options.LayoutOptions
import de.fxdiagram.core.XAbstractDiagram
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import java.util.Map
import javafx.animation.Animation
import javafx.util.Duration

import static java.lang.Math.*

class Layouter { 

	extension KLayoutDataFactory = KLayoutDataFactory.eINSTANCE
	
	extension KGraphFactory = KGraphFactory.eINSTANCE
	
	extension LayoutTransitionFactory = new LayoutTransitionFactory

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

	def getLayoutProvider() {
		new LoggingTransformationService
		new GraphvizLayoutProvider => [
			initialize("DOT")
		]
//		new OgdfLayoutProvider => [
//			initialize("SUGIYAMA")
//		]
	}
	
	def apply(Map<Object,KGraphElement> map, Duration duration) {
		val animations = <Animation>newArrayList
		for(entry: map.entrySet) {
			val xElement = entry.key
			val kElement = entry.value
			switch xElement { 
				XNode: { 
					val shapeLayout = kElement.data.filter(KShapeLayout).head
						animations += createTransition(xElement, shapeLayout.xpos, shapeLayout.ypos, true, duration)
				}
				XConnection: {
					val edgeLayout = kElement.data.filter(KEdgeLayout).head
					val layoutPoints = edgeLayout.createVectorChain
					val controlPoints = xElement.controlPoints
					val nodeDiff = layoutPoints.size - controlPoints.size
					if(nodeDiff > 0) {
						val delta = 1.0 / (nodeDiff + 1)
						val first = controlPoints.head
						val last = controlPoints.last
						for(i: 1..nodeDiff) 
							controlPoints.add(controlPoints.size - 1,
								new XControlPoint => [
									val lambda = delta * i
									layoutX = (1-lambda) * first.layoutX + (lambda) * last.layoutX 
									layoutY = (1-lambda) * first.layoutY + (lambda) * last.layoutY
								]
							)
					}	
					for(i: 1..<controlPoints.size-1) {
						val layoutPoint = layoutPoints.get(min(layoutPoints.size-1, i))
						val currentControlPoint = controlPoints.get(i)
						val transition = createTransition(currentControlPoint, layoutPoint.x, layoutPoint.y, false, duration)
						if (i >= layoutPoints.size)
							transition.onFinished = [
								controlPoints.remove(currentControlPoint)
							] 						
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
			kEdge	
		} else {
			null
		}
	}
	
	protected def toKLabel(XConnectionLabel it, Map<Object, KGraphElement> cache) {
		val kLabel = createKLabel
		kLabel.text = it?.text ?: ""
		val shapeLayout = createKShapeLayout
		kLabel.data += shapeLayout
		cache.put(it, kLabel)
		kLabel
	}
}

