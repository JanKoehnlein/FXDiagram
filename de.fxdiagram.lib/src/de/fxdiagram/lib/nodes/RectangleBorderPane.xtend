package de.fxdiagram.lib.nodes

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import javafx.geometry.Insets
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Paint
import javafx.scene.paint.Stop

import static extension de.fxdiagram.core.css.JavaToCss.*

class RectangleBorderPane extends StackPane implements SvgExportable {

	@FxProperty double borderWidth = 1.2
	
	@FxProperty double borderRadius = 12.0

	@FxProperty Insets borderInsets = new Insets(1, 1, 1, 1)

	@FxProperty Paint borderPaint = Color.GRAY

	@FxProperty Paint backgroundPaint = DEFAULT_BACKGROUND
	
	@FxProperty double backgroundRadius = 12.0

	public static val DEFAULT_BACKGROUND = new LinearGradient(
		0, 0, 1, 1, 
		true, CycleMethod.NO_CYCLE,
		#[
			new Stop(0, Color.gray(0.6)), 
			new Stop(1, Color.gray(0.9))
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
			-fx-border-color: «getBorderPaint.toCss»;
			-fx-border-width: «getBorderWidth»;
			-fx-border-radius: «getBorderRadius»;
			-fx-background-color: «getBackgroundPaint.toCss»;
			-fx-background-radius: «getBackgroundRadius»;
			-fx-background-insets: «getBorderInsets.toCss»;
		'''
	}
	
	override toSvgElement(extension SvgExporter exporter) '''
		<!-- «class.name» -->
		«this.parentToSvgElement('''
			<rect
				«toSvgString(localToSceneTransform)»
				width="«width»" height="«height»"
				rx="«getBorderRadius»" ry="«getBorderRadius»"
				fill="«getBackgroundPaint.toSvgString»"
				stroke="«getBorderPaint.toSvgString»"
				strokeWidth="«getBorderWidth»"
				«opacity.toSvgAttribute("opacity", 1.0)»
			/>
		''')»
	'''
}
