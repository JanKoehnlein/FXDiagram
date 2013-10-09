package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import javafx.beans.property.Property
import javafx.scene.shape.Polygon
import javafx.scene.shape.StrokeType
import javafx.scene.paint.Paint

class TriangleArrowHead extends ArrowHead {
	
	double width 
	
	new(XConnection connection, double width, double height, 
		Property<Paint> strokeProperty, Property<Paint> fillProperty, 
		boolean isSource) {
		super(connection, new Polygon => [
			points.setAll(#[0.0, -0.5 * height, width, 0.0, 0.0, 0.5 * height])
			it.fillProperty.bindBidirectional(fillProperty)
			it.strokeProperty.bind(strokeProperty)
			strokeWidthProperty.bind(connection.strokeWidthProperty)
			strokeType = StrokeType.CENTERED
		], isSource)
		this.width = width
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 5, 10, connection.strokeProperty, connection.strokeProperty, isSource)
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
}