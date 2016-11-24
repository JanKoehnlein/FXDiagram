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
	PointsOnEdge sourceRect
	PointsOnEdge targetRect

	@Accessors boolean reroutingEnabled = false

	new(XConnection connection) {
		this.connection = connection
	}

	def calculatePoints() {
		if(connection.controlPoints.size == 0 && sourceRect != null)
			return
		val newSourceRect = new PointsOnEdge(connection.source)
		val newTargetRect = new PointsOnEdge(connection.target)
		if (sourceRect != null && targetRect != null 
			&& connection.controlPoints.exists[manuallyPlaced || getBehavior(MoveBehavior)?.hasMoved]) {
			sourceRect = newSourceRect
			targetRect = newTargetRect
			partiallyRerouteIfNecessary(sourceRect, connection.controlPoints.head, true) 
			partiallyRerouteIfNecessary(targetRect, connection.controlPoints.last, false) 
			return 
		} else {
			sourceRect = newSourceRect
			targetRect = newTargetRect
			val newControlPoints = defaultPoints
			connection.controlPoints.setAll(newControlPoints)
			reroutingEnabled = true
		}
	}
	
	def getDefaultPoints() {
		val connectionDir = getConnectionDirection
		val points = doRecalculatePoints(connectionDir.key, connectionDir.value)
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
	
	protected def partiallyRerouteIfNecessary(PointsOnEdge connected, XControlPoint anchor, boolean isSource) {
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
				setAnchorPoint(connected, anchor, new Point2D(referencePoint.layoutX, connected.get(lastSide).y), isSource, lastSide, referencePoint)
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
				setAnchorPoint(connected, anchor, new Point2D(connected.get(lastSide).x, referencePoint.layoutY), isSource, lastSide, referencePoint)
			}
		}
	}
	
	protected def switchSide(PointsOnEdge connected, XControlPoint anchor, boolean isSource, Side newSide) {
		val referencePoint = if(isSource) 
			connection.controlPoints.get(1)
		else
			connection.controlPoints.get(connection.controlPoints.size -2)
		setAnchorPoint(connected, anchor, connected.get(newSide), isSource, newSide, referencePoint)
	}
	
	protected def addCorner(PointsOnEdge connected, XControlPoint anchor, boolean isSource, Side newSide) {
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
	
	protected def removeCorner(PointsOnEdge connected, XControlPoint anchor, boolean isSource, Side newSide) {
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

	protected def setAnchorPoint(PointsOnEdge connected, XControlPoint anchor, Point2D newAnchorPoint, boolean isSource, Side newSide, XControlPoint referencePoint) {
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
	
	protected def doRecalculatePoints(Side sourceSide, Side targetSide) {
		val points = newArrayList
		val Point2D startPoint = sourceRect.get(sourceSide)
		var Point2D endPoint = targetRect.get(targetSide)
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
						endPoint = targetRect.get(RIGHT)
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
						endPoint = targetRect.get(TOP)
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
		var sourcePoint = sourceRect.get(RIGHT)
		var targetPoint = targetRect.get(LEFT)
		if ((targetPoint.x - sourcePoint.x) > STANDARD_DISTANCE)
			return RIGHT -> LEFT

		sourcePoint = sourceRect.get(LEFT)
		targetPoint = targetRect.get(RIGHT)
		if ((sourcePoint.x - targetPoint.x) > STANDARD_DISTANCE)
			return LEFT -> RIGHT

		sourcePoint = sourceRect.get(TOP)
		targetPoint = targetRect.get(BOTTOM)
		if ((sourcePoint.y - targetPoint.y) > STANDARD_DISTANCE)
			return TOP -> BOTTOM

		sourcePoint = sourceRect.get(BOTTOM)
		targetPoint = targetRect.get(TOP)
		if ((targetPoint.y - sourcePoint.y) > STANDARD_DISTANCE)
			return BOTTOM -> TOP

		// One additional point
		sourcePoint = sourceRect.get(RIGHT)
		targetPoint = targetRect.get(TOP)
		if (((targetPoint.x - sourcePoint.x) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.y - sourcePoint.y) > STANDARD_DISTANCE))
			return RIGHT -> TOP

		targetPoint = targetRect.get(BOTTOM)
		if (((targetPoint.x - sourcePoint.x) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.y - targetPoint.y) > STANDARD_DISTANCE))
			return RIGHT -> BOTTOM

		sourcePoint = sourceRect.get(LEFT)
		targetPoint = targetRect.get(BOTTOM)
		if (((sourcePoint.x - targetPoint.x) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.y - targetPoint.y) > STANDARD_DISTANCE))
			return LEFT -> BOTTOM

		targetPoint = targetRect.get(TOP)
		if (((sourcePoint.x - targetPoint.x) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.y - sourcePoint.y) > STANDARD_DISTANCE))
			return LEFT -> TOP

		sourcePoint = sourceRect.get(TOP)
		targetPoint = targetRect.get(RIGHT)
		if (((sourcePoint.y - targetPoint.y) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.x - targetPoint.x) > STANDARD_DISTANCE))
			return TOP -> RIGHT

		targetPoint = targetRect.get(LEFT)
		if (((sourcePoint.y - targetPoint.y) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.x - sourcePoint.x) > STANDARD_DISTANCE))
			return TOP -> LEFT

		sourcePoint = sourceRect.get(BOTTOM)
		targetPoint = targetRect.get(RIGHT)
		if (((targetPoint.y - sourcePoint.y) > 0.5 * STANDARD_DISTANCE) && ((sourcePoint.x - targetPoint.x) > STANDARD_DISTANCE))
			return BOTTOM -> RIGHT

		targetPoint = targetRect.get(LEFT)
		if (((targetPoint.y - sourcePoint.y) > 0.5 * STANDARD_DISTANCE) && ((targetPoint.x - sourcePoint.x) > STANDARD_DISTANCE))
			return BOTTOM -> LEFT

		// Two points
		// priority NN >> EE >> NE >> NW >> SE >> SW
		sourcePoint = sourceRect.get(TOP)
		targetPoint = targetRect.get(TOP)
		if ((!targetRect.contains(sourcePoint)) && (!sourceRect.contains(targetPoint))) {
			if ((sourcePoint.y - targetPoint.y) < 0) {
				if (Math.abs(sourcePoint.x - targetPoint.x) > ((sourceRect.width + STANDARD_DISTANCE) / 2))
					return TOP -> TOP
			} else {
				if (Math.abs(sourcePoint.x - targetPoint.x) > (targetRect.width / 2))
					return TOP -> TOP
			}
		}

		sourcePoint = sourceRect.get(RIGHT)
		targetPoint = targetRect.get(RIGHT)
		if ((!targetRect.contains(sourcePoint)) && (!sourceRect.contains(targetPoint))) {
			if ((sourcePoint.x - targetPoint.x) > 0) {
				if (Math.abs(sourcePoint.y - targetPoint.y) > ((sourceRect.height + STANDARD_DISTANCE) / 2))
					return RIGHT -> RIGHT
			} else if (Math.abs(sourcePoint.y - targetPoint.y) > (targetRect.height / 2))
				return RIGHT -> RIGHT
		}

		// Secondly, judge NE NW is available
		sourcePoint = sourceRect.get(TOP)
		targetPoint = targetRect.get(RIGHT)
		if ((!targetRect.contains(sourcePoint)) && (!sourceRect.contains(targetPoint)))
			return TOP -> RIGHT

		targetPoint = targetRect.get(LEFT)
		if ((!targetRect.contains(sourcePoint)) && (!sourceRect.contains(targetPoint)))
			return TOP -> LEFT

		// Finally, judge SE SW is available
		sourcePoint = sourceRect.get(BOTTOM)
		targetPoint = targetRect.get(RIGHT)
		if ((!targetRect.contains(sourcePoint)) && (!sourceRect.contains(targetPoint)))
			return BOTTOM -> RIGHT

		targetPoint = targetRect.get(LEFT)
		if ((!targetRect.contains(sourcePoint)) && (!sourceRect.contains(targetPoint)))
			return BOTTOM -> LEFT

		// Only to return to the
		return RIGHT -> LEFT
	}
}

class PointsOnEdge {
	Map<Side, Point2D> side2point = newHashMap()
	Bounds bounds
	  
	new(XNode host) {
		this.bounds = host.localToRootDiagram(host.node.boundsInLocal)
		val center = this.bounds.center
		side2point.put(TOP, new Point2D(center.x, bounds.minY))
		side2point.put(BOTTOM, new Point2D(center.x, bounds.maxY))
		side2point.put(LEFT, new Point2D(bounds.minX, center.y))
		side2point.put(RIGHT, new Point2D(bounds.maxX, center.y))
	}

	def get(Side side) {
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
