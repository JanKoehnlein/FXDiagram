package de.fxdiagram.core.export

import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRootDiagram
import de.fxdiagram.core.debug.Debug
import java.io.File
import java.io.IOException
import java.util.List
import java.util.logging.Level
import java.util.logging.Logger
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Paint
import javafx.scene.shape.Shape
import javafx.scene.text.Text
import javafx.scene.transform.Transform
import javax.imageio.ImageIO

class SvgExporter {
	
	static val Logger LOG = Logger.getLogger(SvgExporter.canonicalName)
	
	int currentID
	
	int imageCounter 
	
	List<String> defs
	
	def toSvg(XRootDiagram diagram) {
		defs = newArrayList
		currentID = 0
		val bounds = diagram.boundsInParent
		'''
			<?xml version="1.0" standalone="no"?>
			<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" 
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
			<svg viewBox="«bounds.minX» «bounds.minY» «bounds.width» «bounds.height»"
				xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1">
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
	
	def toSvgElement(Object o) {
		switch o {
			SvgExportable: o.toSvgElement(this)
			Text: o.textToSvgElement
			Shape: o.shapeToSvgElement
			Parent: o.parentToSvgElement
			default: '''
				<!-- «o.class.name» not exportable -->
			'''
		}
	}
	
	def CharSequence textToSvgElement(Text it) '''
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
	
	def CharSequence shapeToSvgElement(Shape it) '''
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
	 
	def CharSequence parentToSvgElement(Parent it) '''
		«IF !childrenUnmodifiable.filter[visible].empty»
			«FOR child: childrenUnmodifiable.filter[visible]»
				«child.toSvgElement»
			«ENDFOR»
		«ENDIF»
	'''
	
	def CharSequence toSvgImage(XNode node, Image image) {
	 	val fileName = (node.key ?: 'image' + nextImageNumber) + '.png'
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), 'png', new File(fileName));
    	} catch (IOException e) {
    		LOG.log(Level.SEVERE, "Error exporting " + class.name + " to SVG", e)
    	}
    	'''
    		<!-- «node.class.name» -->
    		<image «toSvgString(node.localToSceneTransform)» width="«node.layoutBounds.width»" height="«node.layoutBounds.height»" xlink:href="«fileName»"/>
    	'''
	}

	def nextImageNumber() {
		imageCounter = imageCounter + 1
		imageCounter 
	}

	public def toSvgAttribute(Object value, String name, Object defaultValue) {
		value.toSvgAttribute(name, defaultValue, '')
	}
	
	public def toSvgAttribute(Object value, String name, Object defaultValue, String unit) {
		if(value.toSvgString != defaultValue.toString)
			'''«name»="«value.toSvgString»«unit»" '''
		else 
			''
	}
	
	public def toSvgString(Shape shape) {
		ShapeConverterExtensions.toSvgString(shape)	
	}
	
	public def toSvgString(Transform it) {
		'''«toSvgAttribute('''matrix(«mxx»,«myx»,«mxy»,«myy»,«tx»,«ty»)''', "transform", 'matrix(1.0,0.0,0.0,1.0,0.0,0.0)')»'''
	} 
	
	public def CharSequence toSvgString(Paint paint) {
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
				"none"
		}
	}
	
	public def toSvgString(CycleMethod it) {
		switch it {
			case CycleMethod.NO_CYCLE: 'pad'
			case CycleMethod.REFLECT: 'reflect'
			case CycleMethod.REPEAT: 'repeat'
		}
	}
	
	public def toSvgString(Object it) {
		toString.toLowerCase
	}
}