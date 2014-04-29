package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import javafx.beans.property.Property
import javafx.scene.Group
import javafx.scene.paint.Paint
import javafx.scene.shape.Polyline
import javafx.scene.shape.StrokeType
import de.fxdiagram.annotations.properties.FxProperty

@ModelNode(#['width', 'height', 'stroke'])
class LineArrowHead extends ArrowHead {
	
	@FxProperty double width 
	@FxProperty double height 
	@FxProperty Paint stroke
	
	new(XConnection connection, double width, double height, 
		Property<Paint> strokeProperty, boolean isSource) {
		this.connection = connection
		this.isSource = isSource
		this.width = width
		this.height = height
		this.strokeProperty.bind(strokeProperty)
		activatePreview()
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 7, 10, connection.strokeProperty, isSource)
	}
	
	override doActivatePreview() {
		node = new Group => [
			children += new Polyline => [
				points.setAll(#[0.0, -0.5 * height, width, 0.0, 0.0, 0.5 * height])
				it.strokeProperty.bind(strokeProperty)
				strokeWidthProperty.bind(connection.strokeWidthProperty)
				strokeType = StrokeType.CENTERED
			]
			children += new Polyline => [
				points.setAll(#[0.0, 0.0, width - connection.strokeWidth, 0.0])
				it.strokeProperty.bind(strokeProperty)
				strokeWidthProperty.bind(connection.strokeWidthProperty)
				strokeType = StrokeType.CENTERED
			]
		]
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
	
}