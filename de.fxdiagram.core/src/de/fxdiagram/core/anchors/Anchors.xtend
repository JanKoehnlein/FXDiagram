package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import javafx.geometry.Point2D

/**
 * Calculates the endpoint of an {@link XConnection} on a {@link XNode}.
 */
interface Anchors {
	
	def Point2D getAnchor(double x, double y)
	
}
