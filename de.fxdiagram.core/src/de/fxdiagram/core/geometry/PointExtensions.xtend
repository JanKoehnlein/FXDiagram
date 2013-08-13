package de.fxdiagram.core.geometry

import javafx.geometry.Point2D

class Point2DExtensions {
	
	static def linear(double fromX, double fromY, double toX, double toY, double lambda) {
		new Point2D(
			linear(fromX, toX, lambda), 
			linear(fromY, toY, lambda))
	}

	static def linear(Point2D from, Point2D to, double lambda) {
		new Point2D(
			linear(from.x, to.x, lambda), 
			linear(from.y, to.y, lambda))
	}

	static def center(Point2D from, Point2D to, double lambda) {
		new Point2D(
			0.5 * (from.x + to.x), 
			0.5 * (from.y + to.y))
	}
	
	static def operator_plus(Point2D left, Point2D right) {
		new Point2D(left.x + right.x, left.y + right.y)
	}

	static def operator_minus(Point2D left, Point2D right) {
		new Point2D(left.x - right.x, left.y - right.y)
	}
	
	@Pure
	static def linear(double x, double y, double lambda) {
		x + (y-x) * lambda
	}
	
}