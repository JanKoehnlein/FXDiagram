package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import javafx.beans.property.Property
import javafx.scene.paint.Paint
import javafx.scene.shape.Polygon
import javafx.scene.shape.StrokeType

@ModelNode('width', 'height', 'stroke', 'fill')
class TriangleArrowHead extends ArrowHead {
	
	@FxProperty double width 
	@FxProperty double height 
	@FxProperty Paint stroke
	@FxProperty Paint fill
	
	new(XConnection connection, double width, double height, 
		Property<Paint> strokeProperty, Property<Paint> fillProperty, 
		boolean isSource) {
		this.connection = connection
		this.isSource = isSource
		this.width = width
		this.height = height
		this.strokeProperty.bind(strokeProperty)
		this.fillProperty.bind(fillProperty)
		activatePreview
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 5, 10, connection.strokeProperty, connection.strokeProperty, isSource)
	}

	override doActivatePreview() {
		node = new Polygon => [
			points.setAll(#[0.0, -0.5 * height, width, 0.0, 0.0, 0.5 * height])
			it.fillProperty.bind(fillProperty)
			it.strokeProperty.bind(strokeProperty)
			strokeWidthProperty.bind(connection.strokeWidthProperty)
			strokeType = StrokeType.CENTERED
		]
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
}