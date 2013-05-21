package de.itemis.javafx.diagram.export

import java.util.List
import javafx.scene.shape.Arc
import javafx.scene.shape.ArcTo
import javafx.scene.shape.ArcType
import javafx.scene.shape.Circle
import javafx.scene.shape.ClosePath
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.CubicCurveTo
import javafx.scene.shape.Ellipse
import javafx.scene.shape.HLineTo
import javafx.scene.shape.Line
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.scene.shape.Polygon
import javafx.scene.shape.Polyline
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.QuadCurveTo
import javafx.scene.shape.Rectangle
import javafx.scene.shape.SVGPath
import javafx.scene.shape.Shape
import javafx.scene.shape.VLineTo
import javafx.scene.text.Text

import static java.lang.Math.*

/** 
 * Copied from the <a href="https://github.com/JFXtras">JFXtras</a> project and converted to Xtend.
 */
class ShapeConverterExtensions {
	
	static val KAPPA = 0.5522847498307935

	def static toSvgPath(Shape shape) {
		new SVGPath => [content = toSvgString(shape)]
	}

	def static toSvgString(Shape shape) {
		internalToSvgString(shape).replaceAll("\\s+", " ").trim
	}

	protected static def dispatch String internalToSvgString(SVGPath svgPath) {
		svgPath.content
	}

	protected static def dispatch String internalToSvgString(Text text) {
		val path = (Shape::subtract(text, new Rectangle(0, 0))) as Path
		path.toSvgString
	}

	protected static def dispatch String internalToSvgString(Line line) '''
		M «line.startX» «line.startY» L «line.endX» «line.endY»
	'''

	protected static def dispatch String internalToSvgString(Arc arc) {
		val centerX = arc.centerX
		val centerY = arc.centerY
		val radiusX = arc.radiusX
		val radiusY = arc.radiusY
		val startAngle = toRadians(arc.startAngle)
		val length = arc.length
		val alpha = toRadians(arc.length + startAngle)
		val phiOffset = toRadians(-90)
		val startX = centerX 
			+ cos(phiOffset) * radiusX * cos(startAngle) 
			+ sin(-phiOffset) * radiusY * sin(startAngle)
		val startY = centerY 
			+ sin(phiOffset) * radiusX * cos(startAngle) 
			+ cos(phiOffset) * radiusY * sin(startAngle)
		val endX = centerX 
			+ cos(phiOffset) * radiusX * cos(alpha) 
			+ sin(-phiOffset) * radiusY * sin(alpha)
		val endY = centerY 
			+ sin(phiOffset) * radiusX * cos(alpha) 
			+ cos(phiOffset) * radiusY * sin(alpha)
		val xAxisRot = 0
		val largeArc = if((length > 180)) 1 else 0
		val sweep = if((length > 0)) 1 else 0
		'''
			M «centerX» «centerY» 
			«IF (ArcType::ROUND == arc.type)»
				h «startX - centerX» v «startY - centerY»
			«ENDIF»
			A «radiusX» «radiusY» «xAxisRot» «largeArc» «sweep» «endX» «endY» 
			«IF (ArcType::CHORD == arc.type || ArcType::ROUND == arc.type)»
				Z
		  	«ENDIF»
		'''
	}

	protected static def dispatch String internalToSvgString(QuadCurve quadCurve) '''
		M «quadCurve.startX» «quadCurve.startY» 
		Q «quadCurve.controlX» «quadCurve.controlY» 
		«quadCurve.endX» «quadCurve.endY»
	'''

	protected static def dispatch String internalToSvgString(CubicCurve cubicCurve) '''
		M «cubicCurve.startX» «cubicCurve.startY» 
		C «cubicCurve.controlX1» «cubicCurve.controlY1» 
		«cubicCurve.controlX2» «cubicCurve.controlY2» 
		«cubicCurve.endX» «cubicCurve.endY»
	'''

	protected static def dispatch String internalToSvgString(Rectangle rectangle) {
		val bounds = rectangle.boundsInLocal
		if (rectangle.arcWidth <=> 0.0 == 0 && rectangle.arcHeight <=> 0.0 == 0) '''
				M «bounds.minX» «bounds.minY» 
				H «bounds.maxX» V «bounds.maxY» 
				H «bounds.minX» V «bounds.minY» Z
		''' else {
			val x = bounds.minX
			val y = bounds.minY
			val width = bounds.width
			val height = bounds.height
			val arcWidth = rectangle.arcWidth
			val arcHeight = rectangle.arcHeight
			val r = x + width
			val b = y + height
			'''
				M «x + arcWidth» «y» 
				L «r - arcWidth» «y» Q «r» «y» «r» «y + arcHeight» 
				L «r» «y + height - arcHeight» Q «r» «b» «r - arcWidth» «b» 
				L «x + arcWidth» «b» Q «x» «b» «x» «b - arcHeight» 
				L «x» «y + arcHeight» Q «x» «y» «x + arcWidth» «y» Z
			'''
		}
	}

	protected static def dispatch String internalToSvgString(Circle circle) {
		val centerX = if(circle.centerX == 0) circle.radius else circle.centerX
		val centerY = if(circle.centerY == 0) circle.radius else circle.centerY
		val radius = circle.radius
		val controlDistance = radius * KAPPA
		'''
			M «centerX» «centerY - radius» 
			C «centerX + controlDistance» «centerY - radius» 
			«centerX + radius» «centerY - controlDistance» 
			«centerX + radius» «centerY» 
			C «centerX + radius» «centerY + controlDistance» 
			«centerX + controlDistance» «centerY + radius» 
			«centerX» «centerY + radius» 
			C «centerX - controlDistance» «centerY + radius» 
			«centerX - radius» «centerY + controlDistance» 
			«centerX - radius» «centerY»  
			C «centerX - radius» «centerY - controlDistance» 
			«centerX - controlDistance» «centerY - radius» 
			«centerX» «centerY - radius» Z
		'''
	}

	protected static def dispatch String internalToSvgString(Ellipse ellipse) {
		val centerX = if(ellipse.centerX == 0) ellipse.radiusX else ellipse.centerX
		val centerY = if(ellipse.centerY == 0) ellipse.radiusY else ellipse.centerY
		val radiusX = ellipse.radiusX
		val radiusY = ellipse.radiusY
		val controlDistanceX = radiusX * KAPPA
		val controlDistanceY = radiusY * KAPPA
		'''
			M «centerX» «centerY - radiusY» 
			C «centerX + controlDistanceX» «centerY - radiusY» 
			«centerX + radiusX» «centerY - controlDistanceY» 
			«centerX + radiusX» «centerY» 
			C «centerX + radiusX» «centerY + controlDistanceY» 
			«centerX + controlDistanceX» «centerY + radiusY» 
			«centerX» «centerY + radiusY» 
			C «centerX - controlDistanceX» «centerY + radiusY» 
			«centerX - radiusX» «centerY + controlDistanceY» 
			«centerX - radiusX» «centerY» 
			C «centerX - radiusX» «centerY - controlDistanceY» 
			«centerX - controlDistanceX» «centerY - radiusY» 
			«centerX» «centerY - radiusY» Z
		'''
	}

	protected static def dispatch String internalToSvgString(Path path) {
		val it = new StringBuilder
		for (element : path.elements) {
			switch element {
				MoveTo: 
					append('''M «element.x» «element.y» ''')
				LineTo:
					append('''L «element.x» «element.y» ''')
				CubicCurveTo:
					append('''C «element.controlX1» «element.controlY1» «
						element.controlX2» «element.controlY2» «
						element.x» «element.y» ''')
				QuadCurveTo:
					append('''Q «element.controlX» «element.controlY» «element.x» «element.y» ''')
				ArcTo:
					append('''A «element.x» «element.y» «element.radiusX» «element.radiusY» ''')
				HLineTo: 
					append('''H «element.x» ''')
				VLineTo:
					append('''V «element.y» ''')
				ClosePath:
					append('Z')
			}
		}
		it.toString
	}

	protected static def dispatch String internalToSvgString(Polygon polygon) {
		pointsToSvgString(polygon.points) + 'Z'
	}

	protected static def dispatch String internalToSvgString(Polyline polyline) {
		pointsToSvgString(polyline.points)
	}
	
	protected def static String pointsToSvgString(List<Double> points) {
		val it = new StringBuilder
		val size = points.size
		if (size % 2 == 0) {
			val coordinates = points
			var i = 0
			while (i < size) {
				append('''
					«IF i==0»
						M
					«ELSE»
						L
					«ENDIF»
					«coordinates.get(i)» «coordinates.get(i + 1)» 
				''')
				i = i + 2
			}
		}
		it.toString
	}
}
