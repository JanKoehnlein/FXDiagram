package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.StrokeType

@ModelNode('fill')
class CircleArrowHead extends ArrowHead {
	
	@FxProperty Paint fill
	
	new(XConnection connection, double radius, 
		Paint stroke, Paint fill, 
		boolean isSource) {
		super(connection, radius, radius, stroke, isSource)
		if(fill != null)
			this.fill = fill
		heightProperty.bindBidirectional(widthProperty)
	}
	
	new(XConnection connection, boolean isSource) {
		this(connection, 4, null, null, isSource)
	}

	override createNode() {
		if(fill == null)
			fillProperty.bind(connection.strokeProperty)
		new Circle => [
			radius = this.width
			layoutX = -radius
			layoutY = 0
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