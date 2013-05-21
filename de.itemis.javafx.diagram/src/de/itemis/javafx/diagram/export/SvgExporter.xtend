package de.itemis.javafx.diagram.export

import de.itemis.javafx.diagram.XRootDiagram
import java.util.List
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Paint
import javafx.scene.shape.Shape
import javafx.scene.text.Text
import javafx.scene.transform.Transform

class SvgExporter {
	
	int currentID
	
	List<String> defs
	
	def toSvg(XRootDiagram diagram) {
		defs = newArrayList
		currentID = 0
		val bounds = diagram.boundsInLocal
		'''
			<?xml version="1.0" standalone="no"?>
			<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" 
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
			<svg viewBox="«bounds.minX» «bounds.minY» «bounds.width» «bounds.height»"
				xmlns="http://www.w3.org/2000/svg" version="1.1">
				«FOR child: diagram.childrenUnmodifiable.filter[visible]»
					«child.toSvgElement»
				«ENDFOR»
				<defs>
					«FOR defElement: defs»
						«defElement»
					«ENDFOR»
				</defs>
			</svg>
		'''
	}
	
	protected def dispatch CharSequence toSvgElement(Text it) '''
		<!-- «class.name» -->
		<path d="«toSvgString»"
			fill="«fill.toSvgString»"
			«opacity.toSvgAttribute("opacity", 1.0)»
			«strokeDashOffset.toSvgAttribute("stroke-dahsoffset", '0.0', 'em')»
			«strokeLineCap.toSvgAttribute("stroke-linecap", "butt")»
			«strokeLineJoin.toSvgAttribute("stroke-linejoin", "miter")»
			«strokeMiterLimit.toSvgAttribute("stroke-miterLimit", 4.0)»
			stroke-width="0.0"
		/>
	'''
	
	protected def dispatch CharSequence toSvgElement(Shape it) '''
		<!-- «class.name» -->
		<path d="«toSvgString»"
			«toSvgString(localToSceneTransform)»
			fill="«fill.toSvgString»"
			«opacity.toSvgAttribute("opacity", 1.0)»
			stroke="«stroke.toSvgString»"
			«strokeDashOffset.toSvgAttribute("stroke-dahsoffset", '0.0', 'em')»
			«strokeLineCap.toSvgAttribute("stroke-linecap", "butt")»
			«strokeLineJoin.toSvgAttribute("stroke-linejoin", "miter")»
			«strokeMiterLimit.toSvgAttribute("stroke-miterLimit", 4.0)»
			«strokeWidth.toSvgAttribute("stroke-width", 1.0)»
		/>
	'''
	// TODO: clip, cursor, smooth, strokeType
	 
	protected def dispatch CharSequence toSvgElement(Parent it) '''
		«IF !childrenUnmodifiable.filter[visible].empty»
			«FOR child: childrenUnmodifiable.filter[visible]»
				«child.toSvgElement»
			«ENDFOR»
		«ENDIF»
	'''

	protected def dispatch CharSequence toSvgElement(Node node) '''
		<!-- «node.class.name» not yet implemented -->
	'''
	
	protected def toSvgAttribute(Object value, String name, Object defaultValue) {
		value.toSvgAttribute(name, defaultValue, '')
	}
	
	protected def toSvgAttribute(Object value, String name, Object defaultValue, String unit) {
		if(value.toSvgString != defaultValue.toString)
			'''«name»="«value.toSvgString»«unit»" '''
		else 
			''
	}
	
	protected def toSvgString(Shape shape) {
		ShapeConverterExtensions::toSvgString(shape)	
	}
	
	protected def toSvgString(Transform it) {
		'''«toSvgAttribute('''matrix(«mxx»,«myx»,«mxy»,«myy»,«tx»,«ty»)''', "transform", 'matrix(1.0,0.0,0.0,1.0,0.0,0.0)')»'''
	} 
	
	protected def CharSequence toSvgString(Paint paint) {
		switch paint {
			Color:
				'''rgb(«(255*paint.red) as int»,«(255*paint.green) as int»,«(255*paint.blue) as int»)''' 
			LinearGradient: {
				currentID = currentID + 1
				defs += '''
					<linearGradient id="Gradient«currentID»"
						gradientUnits="«IF paint.proportional»objectBoundingBox«ELSE»userSpaceOnUse«ENDIF»"
						«paint.startX.toSvgAttribute("x1", 0.0)»
						«paint.startY.toSvgAttribute("y1", 0.0)»
						«paint.endX.toSvgAttribute("x2", 1.0)»
						«paint.endY.toSvgAttribute("y2", 1.0)»
						«paint.cycleMethod.toSvgAttribute("spreadMethod", 'pad')»>
						«FOR stop: paint.stops»
							<stop offset="«stop.offset * 100»%" stop-color="«stop.color.toSvgString»" />
						«ENDFOR»
					</linearGradient>
				'''
				'''url(#Gradient«currentID»)'''
			}
			//	TODO: RadialGradient, ImagePattern
			default: 
				"gray"
		}
	}
	
	protected def toSvgString(CycleMethod it) {
		switch it {
			case CycleMethod::NO_CYCLE: 'pad'
			case CycleMethod::REFLECT: 'reflect'
			case CycleMethod::REPEAT: 'repeat'
		}
	}
	
	protected def toSvgString(Object it) {
		toString.toLowerCase
	}
}