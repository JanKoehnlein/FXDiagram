package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import javafx.scene.paint.Paint
import javafx.scene.shape.Polyline
import javafx.scene.shape.StrokeType
import javafx.scene.Group

class LineArrowHead extends ArrowHead {
	
	double width 
	
	new(XConnection connection, double width, double height, Paint fill, boolean isSource) {
		super(connection, new Group => [
			children += new Polyline => [
				points.setAll(#[0.0, -0.5 * height, width, 0.0, 0.0, 0.5 * height])
				strokeProperty.bind(connection.strokeProperty)
				strokeWidthProperty.bind(connection.strokeWidthProperty)
				strokeType = StrokeType.CENTERED
			]
			children += new Polyline => [
				points.setAll(#[0.0, 0.0, width - connection.strokeWidth, 0.0])
				strokeProperty.bind(connection.strokeProperty)
				strokeWidthProperty.bind(connection.strokeWidthProperty)
				strokeType = StrokeType.CENTERED
			]
		], isSource)
		this.width = width
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 8, 15, connection.stroke, isSource)
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
	
}