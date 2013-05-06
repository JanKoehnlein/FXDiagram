package de.itemis.javafx.diagram.transform

import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.effect.PerspectiveTransform
import javafx.scene.transform.Transform

import static extension de.itemis.javafx.diagram.transform.TransformExtensions.*
import java.util.List

class PerspectiveExtensions {
	static def mapPerspective(Bounds source, Transform transform, double screenDistance) {
		val points = #[
			new Point3D(source.minX, source.minY, 0),
			new Point3D(source.maxX, source.minY, 0),
			new Point3D(source.maxX, source.maxY, 0),
			new Point3D(source.minX, source.maxY, 0)
		]
		val mappedPoints = points.map[(transform * it).mapPerspective(screenDistance)]
		if(mappedPoints.clockwise) {
			return new PerspectiveTransform(
				mappedPoints.get(0).x, mappedPoints.get(0).y, 
				mappedPoints.get(1).x, mappedPoints.get(1).y, 
				mappedPoints.get(2).x, mappedPoints.get(2).y, 
				mappedPoints.get(3).x, mappedPoints.get(3).y
			)	
		} else {
			return null
		}
	}
	
	static def isClockwise(List<Point2D> mappedPoints) {
		if(mappedPoints.length < 3) 
			return false
		return (mappedPoints.get(1).x - mappedPoints.get(0).x) * (mappedPoints.get(1).y + mappedPoints.get(0).y)
			+ (mappedPoints.get(2).x - mappedPoints.get(1).x) * (mappedPoints.get(2).y + mappedPoints.get(1).y) <= 0
	}
	
	static def mapPerspective(Point3D point, double screenDistance) {
		new Point2D(screenDistance * point.x / point.z, screenDistance * point.y / point.z )
	}
}