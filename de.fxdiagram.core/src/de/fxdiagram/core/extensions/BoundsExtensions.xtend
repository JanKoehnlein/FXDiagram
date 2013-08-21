package de.fxdiagram.core.extensions

import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.geometry.Point2D

import static java.lang.Math.*
import javafx.geometry.Insets

class BoundsExtensions {

	def static operator_plus(Bounds left, Bounds right) {
		val minX = min(left.minX, right.minX)
		val minY = min(left.minY, right.minY)
		val minZ = min(left.minZ, right.minZ)
		val maxX = max(left.maxX, right.maxX)
		val maxY = max(left.maxY, right.maxY)
		val maxZ = max(left.maxZ, right.maxZ)
		new BoundingBox(minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ)
	}
	
	def static operator_plus(Bounds bounds, Insets insets) {
		new BoundingBox(bounds.minX - insets.left, bounds.minY - insets.top,
			bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom
		)
	}

	def static operator_minus(Bounds bounds, Insets insets) {
		new BoundingBox(bounds.minX + insets.left, bounds.minY + insets.top,
			bounds.width - insets.left - insets.right, bounds.height - insets.top - insets.bottom
		)
	}

	def static translate(Bounds bounds, double tx, double ty, double tz) {
		new BoundingBox(bounds.minX + tx, bounds.minY + ty, bounds.minZ + tz, bounds.width, bounds.height, bounds.depth)
	}

	def static translate(Bounds bounds, double tx, double ty) {
		new BoundingBox(bounds.minX + tx, bounds.minY + ty, bounds.width, bounds.height)
	}
	
	def static scale(Bounds it, double scaleX, double scaleY, double scaleZ) {
		new BoundingBox(
			minX + 0.5 * width * (1 - scaleX), 
			minY + 0.5 * height * (1 -scaleY), 
			minZ + 0.5 * depth * (1 - scaleZ), 
			width * scaleX, height * scaleY, depth * scaleZ
		)
	}

	def static scale(Bounds it, double scaleX, double scaleY) {
		new BoundingBox(
			minX + 0.5 * width * (1 - scaleX), 
			minY + 0.5 * height * (1 -scaleY), 
			minZ, 
			width * scaleX, height * scaleY, depth
		)
	}

	def static center(Bounds it) {
		new Point2D(minX + 0.5 * width, minY + 0.5 * height)
	}

}
