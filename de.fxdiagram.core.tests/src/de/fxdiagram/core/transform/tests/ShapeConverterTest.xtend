package de.fxdiagram.core.transform.tests

import javafx.scene.shape.Arc
import javafx.scene.shape.Circle
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Line
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import org.junit.Test

import static org.junit.Assert.*

import static extension de.fxdiagram.core.export.ShapeConverterExtensions.*

class ShapeConverterTest {
	
	@Test def testLine() {
		(new Line => [
			startX = 1
			startY = 2
			endX = 3
			endY = 4
		]).mustBecome("M 1.0 2.0 L 3.0 4.0")
	}

	@Test def testCircle() {
		(new Circle => [
			radius = 1
			centerX = 2
			centerY = 2
		]).mustBecome("M 2.0 1.0 C 2.5522847498307932 1.0 3.0 1.4477152501692065 3.0 2.0 C 3.0 2.5522847498307932 2.5522847498307932 3.0 2.0 3.0 C 1.4477152501692065 3.0 1.0 2.5522847498307932 1.0 2.0 C 1.0 1.4477152501692065 1.4477152501692065 1.0 2.0 1.0 Z")
	}
	
	@Test def testArc() {
		(new Arc => [
			radiusX = 1
			radiusY = 2
			centerX = 3
			centerY = 4
			startAngle = 0
			length = 90
		]).mustBecome("M 3.0 4.0 A 1.0 2.0 0 0 1 5.0 4.0")
	}
	
	@Test def testQuadCurve() {
		(new QuadCurve => [
			startX = 1
			startY = 2
			controlX = 3
			controlY = 4
			endX = 5
			endY = 6
		]).mustBecome("M 1.0 2.0 Q 3.0 4.0 5.0 6.0")
	}
	
	@Test def testCubicCurve() {
		(new CubicCurve => [
			startX = 1
			startY = 2
			controlX1 = 3
			controlY1 = 4
			controlX2 = 5
			controlY2 = 6
			endX = 7
			endY = 8
		]).mustBecome("M 1.0 2.0 C 3.0 4.0 5.0 6.0 7.0 8.0")
	}
	
	@Test def testRectangle() {
		(new Rectangle => [
			x = 1
			y = 2
			width = 3
			height = 4
		]).mustBecome("M 1.0 2.0 H 4.0 V 6.0 H 1.0 V 2.0 Z")
	}
	
	@Test def testRoundedRectangle() {
		(new Rectangle => [
			x = 1
			y = 2
			width = 3
			height = 4
			arcWidth = 5
			arcHeight = 6
		]).mustBecome("M 6.0 2.0 L -1.0 2.0 Q 4.0 2.0 4.0 8.0 L 4.0 0.0 Q 4.0 6.0 -1.0 6.0 L 6.0 6.0 Q 1.0 6.0 1.0 0.0 L 1.0 8.0 Q 1.0 2.0 6.0 2.0 Z")
	}
	
	protected def mustBecome(Shape shape, String expectation) {
		assertEquals(expectation, shape.toSvgString)
	}
}