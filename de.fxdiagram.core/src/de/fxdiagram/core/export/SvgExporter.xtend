package de.fxdiagram.core.export

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XDiagram
import java.io.File
import java.io.IOException
import java.util.List
import java.util.logging.Level
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Paint
import javafx.scene.shape.Shape
import javafx.scene.text.Text
import javafx.scene.transform.Transform
import javax.imageio.ImageIO
import javafx.scene.transform.Translate
import java.util.Map
import de.fxdiagram.core.export.SvgLink

/**
 * Exports a given diagram to SVG.
 * 
 * SVG was chosen because it can be converted to a number of different other scalable 
 * vector formats with third-party open-source tools and because it is easy to 
 * generate and inspect as text.
 * 
 * The class will produce scalable graphics for all {@link Shape}s and {@link Text} 
 * elements. For {@link ImageView} and {@link MediaView}, the respective content will be 
 * copied without transformation, and thus be as scalable as the original. Custom SVG 
 * export can be provided by implementing {@link SvgExportable}. For all remaining nodes, 
 * snapshots will be taken and embedded as images (i.e. not scalable).
 * 
 * In Java 7, the CSS styling of nodes was not inspectable. This is why we are currently
 * still bound to this strategy. By fully migrating to Java 8, we should extend that. 
 */
@Logging
class SvgExporter {
	
	static val PT_TO_PX = 2.0/3.0
	 
	int currentID
	
	int imageCounter 
	
	List<String> defs
	
	File baseDir
	
	SvgLinkProvider linkProvider
	
	Map<Node, SvgLink> linkCache = newHashMap
	
	def toSvg(XDiagram diagram, File baseDir, SvgLinkProvider linkProvider) {
		this.baseDir = baseDir
		this.linkProvider = linkProvider
		defs = newArrayList
		currentID = 0
		val bounds = diagram.boundsInParent
		'''
			<?xml version="1.0" standalone="no"?>
			<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" 
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
			<svg viewBox="«bounds.minX» «bounds.minY» «bounds.width» «bounds.height»"
				xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1">
				<rect x="«bounds.minX»" y="«bounds.minY»" width="100%" height ="100%" fill="«diagram.backgroundPaint.toSvgString»"/>
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
	
	def toSvg(XDiagram diagram, File baseDir) {
		diagram.toSvg(baseDir, null)
	}
	
	def toSvgElement(Node o) {
		val link = o.doGetSvgLink
		if(link != null && link != SvgLink.NONE) '''
			<a xlink:href="«link.href»"
				«toSvgAttribute(link.title, 'xlink:title', null)»
				«toSvgAttribute(link.targetFrame, 'target', null)»
				«IF link.openInNewWindow»xlink:show="new"«ENDIF»>
				«o.toSvgElement2»
			</a>
		''' else
			o.toSvgElement2
	}
	
	protected def toSvgElement2(Node o) {
		switch o {
			SvgExportable: o.toSvgElement(this)
			Text: o.textToSvgElement
			Shape: o.shapeToSvgElement
			ImageView: o.imageToSvgElement
			MediaView: o.snapshotToSvgElement
			Parent: o.parentToSvgElement('')
			default: '''
				<!-- «o.class.name» not exportable -->
			'''
		}
	}
	
	def CharSequence textToSvgElement(Text it) '''
		<!-- «class.name» -->
		<path d="«toSvgString»"
			«toSvgString(transformForOrigin)»
			fill="«fill.toSvgString»"
			«opacity.toSvgAttribute("opacity", 1.0)»
			«strokeDashOffset.toSvgAttribute("stroke-dahsoffset", '0.0', 'em')»
			«strokeLineCap.toSvgAttribute("stroke-linecap", "butt")»
			«strokeLineJoin.toSvgAttribute("stroke-linejoin", "miter")»
			«strokeMiterLimit.toSvgAttribute("stroke-miterlimit", 4.0)»
			stroke-width="0.0"
		/>
	'''
	
	protected def getTransformForOrigin(Text it) {
		switch textOrigin {
			case TOP:
				new Translate(0, font.size * PT_TO_PX / 2)
			case BOTTOM:
				new Translate(0, -font.size * PT_TO_PX / 2)
			default:
				new Translate(0, 0)
		}
	}
	
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
			«strokeMiterLimit.toSvgAttribute("stroke-miterlimit", 4.0)»
			«strokeWidth.toSvgAttribute("stroke-width", 1.0)»
		/>
	'''
	// TODO: clip, cursor, smooth, strokeType
	 
	def CharSequence imageToSvgElement(ImageView it) {
		val fileName = saveImageFile(image, viewport)
		'''
		<!-- «class.name» -->
		<image «toSvgString(localToSceneTransform)» 
			width="«layoutBounds.width»" 
			height="«layoutBounds.height»" 
			xlink:href="«fileName»"
		'''
	}
	
	def CharSequence snapshotToSvgElement(Node node) {
		val image = new WritableImage(node.layoutBounds.width as int, node.layoutBounds.height as int)
		node.snapshot(null, image)
		val fileName = saveImageFile(image, null)
		'''
    		<!-- «node.class.name» -->
    		<image «toSvgString(node.localToSceneTransform)»
    			width="«node.layoutBounds.width»"
    			height="«node.layoutBounds.height»"
    			xlink:href="«fileName»"
    	'''
	}

	protected def saveImageFile(Image image, Rectangle2D viewport) {
		val fileName = ('image' + nextImageNumber) + '.png'
		try {
			val buffered = SwingFXUtils.fromFXImage(image, null)
			val visible = if(viewport == null)
				buffered 
			else
				buffered.getSubimage(viewport.minX as int, viewport.minY as int, 
					viewport.width as int, viewport.height as int)
			ImageIO.write(visible, 'png', new File(baseDir, fileName));
    	} catch (IOException e) {
    		LOG.log(Level.SEVERE, "Error exporting " + class.name + " to SVG", e)
    	}
    	fileName
	}

	protected def nextImageNumber() {
		imageCounter = imageCounter + 1
		imageCounter 
	}

	def CharSequence parentToSvgElement(Parent it, CharSequence ownSvgCode) {
		val clipPath = toSvgClip
		if(!childrenUnmodifiable.filter[visible].empty) {
			'''
				«IF clipPath != null»
					<g «clipPath»>
				«ENDIF»
					«ownSvgCode»
					«FOR child: childrenUnmodifiable.filter[visible]»
						«child.toSvgElement»
					«ENDFOR»
				«IF clipPath != null»
					</g>
				«ENDIF»
			'''
		} else ownSvgCode
	}
	
	protected def SvgLink doGetSvgLink(Node node) {
		val link = linkCache.get(node)
		if(link != null)
			link 
		else {
			val newLink = linkProvider.getLink(node) ?: SvgLink.NONE
			linkCache.put(node, newLink)
			newLink
		}
	}
	
	
	public def toSvgAttribute(Object value, String name, Object defaultValue) {
		value.toSvgAttribute(name, defaultValue, '')
	}
	
	public def toSvgAttribute(Object value, String name, Object defaultValue, String unit) {
		if(value?.toSvgString != defaultValue?.toString)
			'''«name»="«value.toSvgString»«unit»" '''
		else 
			''
	}
	
	public def toSvgClip(Node node) {
		val clip = node.clip
		if(clip instanceof Shape) {
			currentID = currentID + 1
			val clipPathId = 'clipPath' + currentID
			defs += '''
				<clipPath id="«clipPathId»"
					«toSvgString(node.localToSceneTransform)»>
				 	<path d="«toSvgString(clip as Shape)»"/>
				</clipPath>
			'''
			'''clip-path="url(#«clipPathId»)" '''
		} else null
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
				val gradientId = "Gradient" + currentID
				defs += '''
					<linearGradient id="«gradientId»"
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
				'''url(#«gradientId»)'''
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