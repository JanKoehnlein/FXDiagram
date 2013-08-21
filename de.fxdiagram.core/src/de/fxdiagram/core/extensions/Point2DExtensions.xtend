package de.fxdiagram.core.extensions

import javafx.geometry.Point2D

class Point2DExtensions {
	
	@Pure
	static def linear(double fromX, double fromY, double toX, double toY, double lambda) {
		new Point2D(
			linear(fromX, toX, lambda), 
			linear(fromY, toY, lambda))
	}

	@Pure
	static def linear(Point2D from, Point2D to, double lambda) {
		new Point2D(
			linear(from.x, to.x, lambda), 
			linear(from.y, to.y, lambda))
	}

	@Pure
	static def center(Point2D from, Point2D to, double lambda) {
		new Point2D(
			0.5 * (from.x + to.x), 
			0.5 * (from.y + to.y))
	}
	
	@Pure
	static def operator_plus(Point2D left, Point2D right) {
		new Point2D(left.x + right.x, left.y + right.y)
	}

	@Pure
	static def operator_minus(Point2D left, Point2D right) {
		new Point2D(left.x - right.x, left.y - right.y)
	}
	
	@Pure
	static def linear(double x, double y, double lambda) {
		x + (y-x) * lambda
	}
	
	@Pure
	static def isClockwise(Point2D one, Point2D two, Point2D three) {
		isClockwise(one.x, one.y, two.x, two.y, three.x, three.y)	
	}
	
	@Pure
	static def isClockwise(double x0, double y0, double x1, double y1, double x2, double y2) {
		(x1 - x0) * (y1 + y0) + (x2 - x1) * (y2 + y1) + (x0 - x2) * (y0 + y2) > 0
	}
	
	
}