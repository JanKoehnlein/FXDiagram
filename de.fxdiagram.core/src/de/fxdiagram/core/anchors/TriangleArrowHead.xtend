package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import javafx.scene.shape.Polygon
import javafx.scene.shape.StrokeType
import javafx.scene.paint.Paint

class TriangleArrowHead extends ArrowHead {
	
	new(XConnection connection, double width, double height, Paint fill, boolean isSource) {
		super(connection, new Polygon => [
			points.setAll(#[0.0, -0.5 * height, width, 0.0, 0.0, 0.5 * height])
			it.fill = fill
			strokeProperty.bind(connection.strokeProperty)
			strokeWidthProperty.bind(connection.strokeWidthProperty)
			strokeType = StrokeType.CENTERED
		], isSource)
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 5, 10, connection.stroke, isSource)
	}
	
	override getLineCut() {
		5 + connection.strokeWidth
	}
	
}