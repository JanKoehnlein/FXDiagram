package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import javafx.scene.Group
import javafx.scene.shape.Polyline
import javafx.scene.shape.StrokeType
import javafx.beans.property.Property
import javafx.scene.paint.Paint

class LineArrowHead extends ArrowHead {
	
	double width 
	
	new(XConnection connection, double width, double height, Property<Paint> strokeProperty, boolean isSource) {
		super(connection, new Group => [
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
		], isSource)
		this.width = width
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 7, 10, connection.strokeProperty, isSource)
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
	
}