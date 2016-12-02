package de.fxdiagram.core.anchors

import javafx.geometry.Point2D
import javafx.geometry.Side

interface ManhattanAnchors {
	
	def Point2D getManhattanAnchor(double x, double y, Side side)

	def Point2D getDefaultAnchor(Side side)

	def Point2D getDefaultSnapAnchor(Side side)
}