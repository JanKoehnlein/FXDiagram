package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.Map
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.geometry.Side
import org.eclipse.xtend.lib.annotations.Accessors

import static de.fxdiagram.core.XControlPoint.Type.*
import static java.lang.Math.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class ManhattanRouter {

	val STANDARD_DISTANCE = 20

	XConnection connection
	CachedAnchors sourceAnchors
	CachedAnchors targetAnchors

	@Accessors boolean reroutingEnabled = false

	new(XConnection connection) {
		this.connection = connection
	}

	def calculatePoints() {
		if(connection.controlPoints.size == 0 && sourceAnchors != null)
			return
		val newSourceAnchors = new CachedAnchors(connection.source)
		val newTargetAnchors = new CachedAnchors(connection.target)
		if (sourceAnchors != null && targetAnchors != null 
			&& connection.controlPoints.exists[manuallyPlaced || getBehavior(MoveBehavior)?.hasMoved]) {
			sourceAnchors = newSourceAnchors
			targetAnchors = newTargetAnchors
			partiallyRerouteIfNecessary(sourceAnchors, connection.controlPoints.head, true) 
			partiallyRerouteIfNecessary(targetAnchors, connection.controlPoints.last, false) 
			return 
		} else {
			sourceAnchors = newSourceAnchors
			targetAnchors = newTargetAnchors
			val newControlPoints = defaultPoints
			connection.controlPoints.setAll(newControlPoints)
			reroutingEnabled = true
		}
	}
	
	def getDefaultPoints() {
		val connectionDir = getConnectionDirection
		val points = calculateDefaultPoints(connectionDir.key, connectionDir.value)
		if(connection.sourceArrowHead != null)
			points.set(0, connection.sourceArrowHead.correctAnchor(points.get(1).x, points.get(1).y, points.head))
		if(connection.targetArrowHead != null)
			points.set(points.size-1, connection.targetArrowHead.correctAnchor(points.get(points.size - 2).x, points.get(points.size - 2).y, points.last))
		val newControlPoints = newArrayList
		points.forEach [ point, i |
			newControlPoints += new XControlPoint => [
				layoutX = point.x
				layoutY = point.y
				type = if (i==0 || i == points.size - 1)
						ANCHOR
					else  
				 		INTERPOLATED
			]
		]
		newControlPoints.head.side = connectionDir.key
		newControlPoints.last.side = connectionDir.value
		return newControlPoints
	}
	
	protected def partiallyRerouteIfNecessary(CachedAnchors connected, XControlPoint anchor, boolean isSource) {
		val lastSide = anchor.side
		val referencePoint = if(isSource) 
				connection.controlPoints.get(1)
			else
				connection.controlPoints.get(connection.controlPoints.size -2)
		val refPoint2 = if(connection.controlPoints.size > 3) {
				if(isSource) 
					connection.controlPoints.get(2)
				else
					connection.controlPoints.get(connection.controlPoints.size - 3)
			} else {
				null
			} 
		val lineCut = if(isSource) 
				connection.sourceArrowHead?.lineCut
			else
				connection.targetArrowHead?.lineCut
		val doReroute = reroutingEnabled && !referencePoint.layoutXProperty.bound && !referencePoint.layoutYProperty.bound
		switch lastSide {
			case TOP, case BOTTOM: {
				if(doReroute) {
					if(anchor.layoutX < connected.get(LEFT).x) {
						addCorner(connected, anchor, isSource, LEFT)
						return						
					} else if(anchor.layoutX > connected.get(RIGHT).x) {
						addCorner(connected, anchor, isSource, RIGHT)
						return 						
					} else if(refPoint2 != null 
						&& refPoint2.layoutY > connected.get(TOP).y 
						&& refPoint2.layoutY < connected.get(BOTTOM).y) {
						if(refPoint2.layoutX < connected.get(lastSide).x)
							removeCorner(connected, anchor, isSource, LEFT)
						else
							removeCorner(connected, anchor, isSource, RIGHT)
						return
					} else if(lastSide == TOP 
						&& (referencePoint.layoutY > connected.get(BOTTOM).y + lineCut
							|| abs(referencePoint.layoutY - connected.get(TOP).y) < lineCut)) {
						switchSide(connected, anchor, isSource, BOTTOM)
						return
					} else if(lastSide == BOTTOM 
						&& (referencePoint.layoutY < connected.get(TOP).y - lineCut
							|| abs(referencePoint.layoutY - connected.get(BOTTOM).y) < lineCut)) {
						switchSide(connected, anchor, isSource, TOP)
						return								
					}
				} 
			}
			case LEFT, case RIGHT: {
				if(doReroute) {
					if(anchor.layoutY < connected.get(TOP).y) {
						addCorner(connected, anchor, isSource, TOP)
						return
					} else if(anchor.layoutY > connected.get(BOTTOM).y) {
						addCorner(connected, anchor, isSource, BOTTOM)
						return
					} else if(refPoint2 != null
						&& refPoint2.layoutX > connected.get(LEFT).x 
						&& refPoint2.layoutX < connected.get(RIGHT).x) {
							if(refPoint2.layoutY < connected.get(lastSide).y)
								removeCorner(connected, anchor, isSource, TOP)
							else
								removeCorner(connected, anchor, isSource, BOTTOM)
							return
					} else if(lastSide == LEFT 
						&& (referencePoint.layoutX > connected.get(RIGHT).x + lineCut
							|| abs(referencePoint.layoutX - connected.get(LEFT).x) < lineCut)) {
						switchSide(connected, anchor, isSource, RIGHT)
						return								
					} else if(lastSide == RIGHT 
						&& (referencePoint.layoutX < connected.get(LEFT).x - lineCut
							|| abs(referencePoint.layoutX - connected.get(RIGHT).x) < lineCut)) {
						switchSide(connected, anchor, isSource, LEFT)
						return								
					}
				}
			}
		}
		setAnchorPoint(connected, anchor, connected.get(referencePoint, lastSide), isSource, lastSide, referencePoint)
	}
	
	protected def switchSide(CachedAnchors connected, XControlPoint anchor, boolean isSource, Side newSide) {
		val referencePoint = if(isSource) 
			connection.controlPoints.get(1)
		else
			connection.controlPoints.get(connection.controlPoints.size -2)
		setAnchorPoint(connected, anchor, connected.get(newSide), isSource, newSide, referencePoint)
	}
	
	protected def addCorner(CachedAnchors connected, XControlPoint anchor, boolean isSource, Side newSide) {
		val index = if(isSource) 1 else connection.controlPoints.size - 1
		val cpX = anchor.layoutX
		val cpY = anchor.layoutY			
		val newPoint = new XControlPoint => [
			layoutX = if(newSide.vertical) cpX else connected.get(newSide).x
			layoutY = if(newSide.vertical) connected.get(newSide).y else cpY
			type = INTERPOLATED
		]
		connection.controlPoints.add(index, newPoint)
		setAnchorPoint(connected, anchor, connected.get(newSide), isSource, newSide, newPoint)
	}
	
	protected def removeCorner(CachedAnchors connected, XControlPoint anchor, boolean isSource, Side newSide) {
		val referencePoint = if(isSource) { 
				connection.controlPoints.remove(1)
				connection.controlPoints.get(1)		
			} else {
				connection.controlPoints.remove(connection.controlPoints.size - 2)
				connection.controlPoints.get(connection.controlPoints.size - 2)
			}
		val anchorPoint = if(newSide.isHorizontal) 
				new Point2D(referencePoint.layoutX, connected.get(newSide).y)
			else
				new Point2D(connected.get(newSide).x, referencePoint.layoutY)
		setAnchorPoint(connected, anchor, anchorPoint, isSource, newSide, referencePoint)
	}

	protected def setAnchorPoint(CachedAnchors connected, XControlPoint anchor, Point2D newAnchorPoint, boolean isSource, Side newSide, XControlPoint referencePoint) {
		var anchorPoint = newAnchorPoint
	 	if(isSource) {
			if(connection.sourceArrowHead != null)
				anchorPoint = connection.sourceArrowHead.correctAnchor(referencePoint.layoutX, referencePoint.layoutY, newAnchorPoint)
	 	} else {
			if(connection.targetArrowHead != null)
				anchorPoint = connection.targetArrowHead.correctAnchor(referencePoint.layoutX, referencePoint.layoutY, newAnchorPoint)
	 	} 
	 	anchor.side = newSide
	 	anchor.layoutX = anchorPoint.x
	 	anchor.layoutY = anchorPoint.y
	}	
	
	protected def calculateDefaultPoints(Side sourceSide, Side targetSide) {
		val points = newArrayList
		val Point2D startPoint = sourceAnchors.get(sourceSide)
		var Point2D endPoint = targetAnchors.get(targetSide)
		switch sourceSide {
			case RIGHT: {
				points += startPoint
				switch targetSide {
					case BOTTOM: {
						points += new Point2D(endPoint.x, startPoint.y)
					}
					case TOP: {
						points += new Point2D(endPoint.x, startPoint.y)
					}
					case RIGHT: {
						points += new Point2D(Math.max(startPoint.x, endPoint.x) + 1.5 * STANDARD_DISTANCE, startPoint.y)
						points += new Point2D(Math.max(startPoint.x, endPoint.x) + 1.5 * STANDARD_DISTANCE, endPoint.y)
					}
					case LEFT: {
						if (endPoint.y != startPoint.y) {
							points += new Point2D((startPoint.x + endPoint.x) / 2, startPoint.y)
							points += new Point2D((startPoint.x + endPoint.x) / 2, endPoint.y)
						}
					}
				}
			}
			case LEFT: {
				points += startPoint
				switch targetSide {
					case BOTTOM: {
						points += new Point2D(endPoint.x, startPoint.y)
					}
					case TOP: {
						points += new Point2D(endPoint.x, startPoint.y)
					}
					default: {
						endPoint = targetAnchors.get(RIGHT)
						if (endPoint.y != startPoint.y) {
							points += new Point2D((startPoint.x + endPoint.x) / 2, startPoint.y)
							points += new Point2D((startPoint.x + endPoint.x) / 2, endPoint.y)
						}
					}
				}
			}
			case TOP: {
				points += startPoint
				switch targetSide {
					case RIGHT: {
						if ((endPoint.x - startPoint.x) > 0) {
							points += new Point2D(startPoint.x, startPoint.y - STANDARD_DISTANCE)
							points += new Point2D(endPoint.x + 1.5 * STANDARD_DISTANCE, startPoint.y - STANDARD_DISTANCE)
							points += new Point2D(endPoint.x + 1.5 * STANDARD_DISTANCE, endPoint.y)
						} else {
							points += new Point2D(startPoint.x, endPoint.y)
						}
					}
					case LEFT: {
						if ((endPoint.x - startPoint.x) < 0) {
							points += new Point2D(startPoint.x, startPoint.y - STANDARD_DISTANCE)
							points += new Point2D(endPoint.x - 1.5 * STANDARD_DISTANCE, startPoint.y - STANDARD_DISTANCE)
							points += new Point2D(endPoint.x - 1.5 * STANDARD_DISTANCE, endPoint.y)
						} else {
							points += new Point2D(startPoint.x, endPoint.y)
						}
					}
					case TOP: {
						points += new Point2D(startPoint.x, Math.min(startPoint.y, endPoint.y) - 1.5 * STANDARD_DISTANCE)
						points += new Point2D(endPoint.x, Math.min(startPoint.y, endPoint.y) - 1.5 * STANDARD_DISTANCE)
					}
					case BOTTOM: {
						if (endPoint.x != startPoint.x) {
							points += new Point2D(startPoint.x, (startPoint.y + endPoint.y) / 2)
							points += new Point2D(endPoint.x, (startPoint.y + endPoint.y) / 2)
						}
					}
				}
			}
			case BOTTOM: {
				points += startPoint
				switch targetSide {
					case RIGHT: {
						if ((endPoint.x - startPoint.x) > 0) {
							points += new Point2D(startPoint.x, startPoint.y + STANDARD_DISTANCE)
							points += new Point2D(endPoint.x + 1.5 * STANDARD_DISTANCE, startPoint.y + STANDARD_DISTANCE)
							points += new Point2D(endPoint.x + 1.5 * STANDARD_DISTANCE, endPoint.y)
						} else {
							points += new Point2D(startPoint.x, endPoint.y)
						}
					}
					case LEFT: {
						if ((endPoint.x - startPoint.x) < 0) {
							points += new Point2D(startPoint.x, startPoint.y + STANDARD_DISTANCE)
							points += new Point2D(endPoint.x - 1.5 * STANDARD_DISTANCE, startPoint.y + STANDARD_DISTANCE)
							points += new Point2D(endPoint.x - 1.5 * STANDARD_DISTANCE, endPoint.y)
						} else {
							points += new Point2D(startPoint.x, endPoint.y)
						}
					}
					default: {
						endPoint = targetAnchors.get(TOP)
						if (endPoint.x != startPoint.x) {
							points += new Point2D(startPoint.x, (startPoint.y + endPoint.y) / 2)
							points += new Point2D(endPoint.x, (startPoint.y + endPoint.y) / 2)
						}
					}
				}
			}
		}
		points += endPoint
		return points;
	}

	protected def getConnectionDirection() {
		// distance is enough
		var sourcePoint = sourceAnchors.getUnselected(RIGHT)
		var targetPoint = targetAnchors.getUnselected(LEFT)
		if ((targetPoint.x - sourcePoint.x) > STANDARD_DISTANCE)
			return RIGHT -> LEFT

		sourcePoint = sourceAnchors.getUnselected(LEFT)
		targetPoint = targetAnchors.getUnselected(RIGHT)
		if ((sourcePoint.x - targetPoint.x) > STANDARD_DISTANCE)
			return LEFT -> RIGHT

		sourcePoint = sourceAnchors.getUnselected(TOP)
		targetPoint = targetAnchors.getUnselected(BOTTOM)
		if ((sourcePoint.y - targetPoint.y) > STANDARD_DISTANCE)
			return TOP -> BOTTOM

		sourcePoint = sourceAnchors.getUnselected(BOTTOM)
		targetPoint = targetAnchors.getUnselected(TOP)
		if ((targetPoint.y - sourcePoint.y) > STANDARD_DISTANCE)
			return BOTTOM -> TOP

		// One additional point
		sourcePoint = sourceAnchors.getUnselected(RIGHT)
		targetPoint = targetAnchors.getUnselected(TOP)
		if (((targetPoint.x - sourcePoint.x) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.y - sourcePoint.y) > STANDARD_DISTANCE))
			return RIGHT -> TOP

		targetPoint = targetAnchors.getUnselected(BOTTOM)
		if (((targetPoint.x - sourcePoint.x) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.y - targetPoint.y) > STANDARD_DISTANCE))
			return RIGHT -> BOTTOM

		sourcePoint = sourceAnchors.getUnselected(LEFT)
		targetPoint = targetAnchors.getUnselected(BOTTOM)
		if (((sourcePoint.x - targetPoint.x) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.y - targetPoint.y) > STANDARD_DISTANCE))
			return LEFT -> BOTTOM

		targetPoint = targetAnchors.getUnselected(TOP)
		if (((sourcePoint.x - targetPoint.x) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.y - sourcePoint.y) > STANDARD_DISTANCE))
			return LEFT -> TOP

		sourcePoint = sourceAnchors.getUnselected(TOP)
		targetPoint = targetAnchors.getUnselected(RIGHT)
		if (((sourcePoint.y - targetPoint.y) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.x - targetPoint.x) > STANDARD_DISTANCE))
			return TOP -> RIGHT

		targetPoint = targetAnchors.getUnselected(LEFT)
		if (((sourcePoint.y - targetPoint.y) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.x - sourcePoint.x) > STANDARD_DISTANCE))
			return TOP -> LEFT

		sourcePoint = sourceAnchors.getUnselected(BOTTOM)
		targetPoint = targetAnchors.getUnselected(RIGHT)
		if (((targetPoint.y - sourcePoint.y) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.x - targetPoint.x) > STANDARD_DISTANCE))
			return BOTTOM -> RIGHT

		targetPoint = targetAnchors.getUnselected(LEFT)
		if (((targetPoint.y - sourcePoint.y) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.x - sourcePoint.x) > STANDARD_DISTANCE))
			return BOTTOM -> LEFT

		// Two points
		// priority NN >> EE >> NE >> NW >> SE >> SW
		sourcePoint = sourceAnchors.getUnselected(TOP)
		targetPoint = targetAnchors.getUnselected(TOP)
		if ((!targetAnchors.contains(sourcePoint)) && (!sourceAnchors.contains(targetPoint))) {
			if ((sourcePoint.y - targetPoint.y) < 0) {
				if (Math.abs(sourcePoint.x - targetPoint.x) > ((sourceAnchors.width + STANDARD_DISTANCE) / 2))
					return TOP -> TOP
			} else {
				if (Math.abs(sourcePoint.x - targetPoint.x) > (targetAnchors.width / 2))
					return TOP -> TOP
			}
		}

		sourcePoint = sourceAnchors.getUnselected(RIGHT)
		targetPoint = targetAnchors.getUnselected(RIGHT)
		if ((!targetAnchors.contains(sourcePoint)) && (!sourceAnchors.contains(targetPoint))) {
			if ((sourcePoint.x - targetPoint.x) > 0) {
				if (Math.abs(sourcePoint.y - targetPoint.y) > ((sourceAnchors.height + STANDARD_DISTANCE) / 2))
					return RIGHT -> RIGHT
			} else if (Math.abs(sourcePoint.y - targetPoint.y) > (targetAnchors.height / 2))
				return RIGHT -> RIGHT
		}

		// Secondly, judge NE NW is available
		sourcePoint = sourceAnchors.getUnselected(TOP)
		targetPoint = targetAnchors.getUnselected(RIGHT)
		if ((!targetAnchors.contains(sourcePoint)) && (!sourceAnchors.contains(targetPoint)))
			return TOP -> RIGHT

		targetPoint = targetAnchors.getUnselected(LEFT)
		if ((!targetAnchors.contains(sourcePoint)) && (!sourceAnchors.contains(targetPoint)))
			return TOP -> LEFT

		// Finally, judge SE SW is available
		sourcePoint = sourceAnchors.getUnselected(BOTTOM)
		targetPoint = targetAnchors.getUnselected(RIGHT)
		if ((!targetAnchors.contains(sourcePoint)) && (!sourceAnchors.contains(targetPoint)))
			return BOTTOM -> RIGHT

		targetPoint = targetAnchors.getUnselected(LEFT)
		if ((!targetAnchors.contains(sourcePoint)) && (!sourceAnchors.contains(targetPoint)))
			return BOTTOM -> LEFT

		// Only to return to the
		return RIGHT -> LEFT
	}
}

class CachedAnchors {

	XNode host
	Map<Side, Point2D> side2point = newHashMap()
	Map<Side, Point2D> side2pointUnselected = newHashMap()
	Bounds bounds
	  
	new(XNode host) {
		this.host = host
		this.bounds = host.localToRootDiagram(host.node.boundsInLocal)
		if(!(host?.anchors instanceof ManhattanAnchors)) {
			val center = this.bounds.center
			side2point.put(TOP, new Point2D(center.x, bounds.minY))
			side2point.put(BOTTOM, new Point2D(center.x, bounds.maxY))
			side2point.put(LEFT, new Point2D(bounds.minX, center.y))
			side2point.put(RIGHT, new Point2D(bounds.maxX, center.y))
			
			val snapBounds = host.parent.localToRootDiagram(host.snapBounds)
			val snapCenter = snapBounds.center
			side2pointUnselected.put(TOP, new Point2D(snapCenter.x, snapBounds.minY))
			side2pointUnselected.put(BOTTOM, new Point2D(snapCenter.x, snapBounds.maxY))
			side2pointUnselected.put(LEFT, new Point2D(snapBounds.minX, snapCenter.y))
			side2pointUnselected.put(RIGHT, new Point2D(snapBounds.maxX, snapCenter.y))
		}
	}

	def get(XControlPoint referencePoint, Side side) {
		val anchors = host.anchors
		if(anchors instanceof ManhattanAnchors) 
			anchors.getManhattanAnchor(referencePoint.layoutX, referencePoint.layoutY, side)
		else 
			get(side)
	}
	
	def getUnselected(Side side) {
		val anchors = host.anchors
		if(anchors instanceof ManhattanAnchors)
			anchors.getDefaultSnapAnchor(side)
		else 
			side2pointUnselected.get(side)
	}

	def get(Side side) {
		val anchors = host.anchors
		if(anchors instanceof ManhattanAnchors)
			anchors.getDefaultAnchor(side)
		else
			side2point.get(side)
	}

	def contains(Point2D point) {
		bounds.contains(point)
	}

	def getWidth() {
		bounds.width
	}

	def getHeight() {
		bounds.height
	}

	def getBounds() {
		bounds
	}
}
