package de.fxdiagram.core.extensions

import javafx.geometry.Point2D

import static java.lang.Math.*

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
	static def operator_multiply(Point2D left, double right) {
		new Point2D(left.x * right, left.y * right)
	}
	
	@Pure
	static def operator_multiply(double left, Point2D right) {
		new Point2D(left * right.x, left * right.y)
	}
	
	@Pure
	static def operator_divide(Point2D left, double right) {
		new Point2D(left.x / right, left.y / right)
	}
	
	@Pure
	static def operator_divide(double left, Point2D right) {
		new Point2D(left / right.x, left / right.y)
	}
	
	@Pure
	static def norm(Point2D it) {
		sqrt(x*x + y*y)
	}
	
	@Pure
	static def norm(double x, double y) {
		sqrt(x*x + y*y)
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
	
	public static val EPSILON_DEGREES = 5
	static val COS_EPSILON = cos(EPSILON_DEGREES * PI/180)

	@Pure
	static def areOnSameLine(Point2D x0, Point2D x1, Point2D x2) {
		return areOnSameLine(x0.x, x0.y, x1.x, x1.y, x2.x, x2.y)
	}
		
	@Pure
	static def areOnSameLine(double x0x, double x0y, double x1x, double x1y, double x2x, double x2y) {
		val d0x = x0x-x1x
		val d0y = x0y-x1y
		val norm0 = norm(d0x, d0y)
		if(norm0 < 1)
			return true
		val d1x = x1x-x2x
		val d1y = x1y-x2y
		val norm1 = norm(d1x, d1y)
		if(norm1 < 1)
			return true
		val cosTheta = (d0x*d1x + d0y*d1y) / norm0 / norm1
		return abs(cosTheta) > COS_EPSILON 
	}
}