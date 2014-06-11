package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import javafx.scene.paint.Paint
import javafx.scene.shape.Polygon
import javafx.scene.shape.StrokeType

@ModelNode(#['fill'])
class DiamondArrowHead extends ArrowHead {
	
	@FxProperty Paint fill
	
	new(XConnection connection, double width, double height, 
		Paint stroke, Paint fill, boolean isSource) {
		super(connection, width, height, stroke, isSource)
		this.fillProperty.bind(fillProperty)
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 10, 10, null, null, isSource)
	}

	override createNode() {
		if(fill == null)
			fillProperty.bind(connection.strokeProperty)
		new Polygon => [
			points.setAll(#[
				0.0, 0.0,
				0.5 * width, -0.5 * height, 
				width, 0.0, 
				0.5 * width, 0.5 * height
			])
			it.fillProperty.bind(this.fillProperty)
			it.strokeProperty.bind(this.strokeProperty)
			strokeWidthProperty.bind(connection.strokeWidthProperty)
			strokeType = StrokeType.CENTERED
		]
	}
	
	override getLineCut() {
		width + connection.strokeWidth
	}
}