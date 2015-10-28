package de.fxdiagram.lib.nodes

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import javafx.geometry.Insets
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Paint
import javafx.scene.paint.Stop

import static extension de.fxdiagram.core.css.JavaToCss.*

/** 
 * In Java 7 it was not possible to inspect the CSS styles of a node, so we needed this
 * class to have simple box with some content that can be exported to SVG.
 * Still widely used in the examples. 
 */
class RectangleBorderPane2 extends Pane implements SvgExportable {

	@FxProperty double borderWidth = 1.2
	
	@FxProperty double borderRadius = 6.0

	@FxProperty Insets borderInsets = new Insets(0.9, 0.9, 0.9, 0.9)

	@FxProperty Paint borderPaint = Color.GRAY

	@FxProperty Paint backgroundPaint = DEFAULT_BACKGROUND
	
	@FxProperty double backgroundRadius = 6.0

	public static val DEFAULT_BACKGROUND = new LinearGradient(
		0, 0, 1, 1, 
		true, CycleMethod.NO_CYCLE,
		#[
			new Stop(0, Color.gray(0.7)), 
			new Stop(1, Color.gray(1))
		]) 
	
	new() {
		setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE)
		borderWidthProperty.addListener[ prop, oldVal, newVal | updateStyle]
		borderRadiusProperty.addListener[ prop, oldVal, newVal | updateStyle]
		borderInsetsProperty.addListener[ prop, oldVal, newVal | updateStyle]
		borderPaintProperty.addListener[ prop, oldVal, newVal | updateStyle]
		backgroundPaintProperty.addListener[ prop, oldVal, newVal | updateStyle]
		backgroundRadiusProperty.addListener[ prop, oldVal, newVal | updateStyle]
		updateStyle
	}
	
	protected def updateStyle() {
		style = '''
			-fx-border-color: «borderPaint.toCss»;
			-fx-border-width: «borderWidth»;
			-fx-border-radius: «borderRadius»;
			-fx-background-color: «backgroundPaint.toCss»;
			-fx-background-radius: «backgroundRadius»;
			-fx-background-insets: «borderInsets.toCss»;
		'''
	}
	
	override toSvgElement(extension SvgExporter exporter) '''
		<!-- «class.name» -->
		«this.parentToSvgElement('''
			<rect
				«toSvgString(localToSceneTransform)»
				width="«width»" height="«height»"
				rx="«borderRadius»" ry="«borderRadius»"
				fill="«backgroundPaint.toSvgString»"
				stroke="«borderPaint.toSvgString»"
				stroke-width="«borderWidth»"
				«opacity.toSvgAttribute("opacity", 1.0)»
			/>
		''')»
	'''
}
