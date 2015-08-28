package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import javafx.scene.Group
import javafx.scene.paint.Paint
import javafx.scene.shape.Polyline
import javafx.scene.shape.StrokeType

@ModelNode
class LineArrowHead extends ArrowHead {
	
	new(XConnection connection, double width, double height, 
		Paint stroke, boolean isSource) {
		super(connection, width, height, stroke, isSource)
	}

	new(XConnection connection, boolean isSource) {
		this(connection, 7, 10, null, isSource)
	}

	override createNode() {
		new Group => [
			children += new Polyline => [
				points.setAll(#[0.0, -0.5 * height, width, 0.0, 0.0, 0.5 * height])
				it.strokeProperty.bind(this.strokeProperty)
				strokeWidthProperty.bind(connection.strokeWidthProperty)
				opacityProperty.bind(connection.opacityProperty)
				strokeType = StrokeType.CENTERED
			]
			children += new Polyline => [
				points.setAll(#[0.0, 0.0, width - connection.strokeWidth, 0.0])
				it.strokeProperty.bind(this.strokeProperty)
				strokeWidthProperty.bind(connection.strokeWidthProperty)
				opacityProperty.bind(connection.opacityProperty)
				strokeType = StrokeType.CENTERED
			]
		]
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
	
}