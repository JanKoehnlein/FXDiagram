package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.geometry.VPos
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.text.Font
import javafx.scene.transform.Affine

import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import static extension java.lang.Math.*

/**
 * A label on an {@link XConnection}.
 * 
 * The {@link text} property denotes the actual text.
 *   
 * The label placed next to the connection the given {@link position} which is in between 
 * 0 (start of the connection) and 1 (end of the connection). It is also rotated such that 
 * it is always tangeltial to the curve of the connection at the given position and upside 
 * up.  
 */
@ModelNode('connection')
class XConnectionLabel extends XLabel {

	@FxProperty(readOnly=true) XConnection connection
	@FxProperty double position = 0.5

	Effect selectionEffect = new DropShadow		

	new(XConnection connection) {
		this.connection = connection
	}

	new(DomainObjectDescriptor domainObjectDescriptor) {
		super(domainObjectDescriptor)
	}

	def setConnection(XConnection connection) {
		if (this.connection != connection) {
			if(this.connection != null) 
				this.connection.labels -= this
			connectionProperty.set(connection)
			if(connection != null && !connection.labels.contains(this)) {
				connection.labels += this
			}
		}
	}

	protected override createNode() {
		text => [
			textOrigin = VPos.TOP
			font = Font.font(font.family, font.size * 0.9)
		]
	}
	
	override selectionFeedback(boolean isSelected) {
		if (isSelected) {
			effect = selectionEffect
			connection.selected = true
			scaleX = 1.05
			scaleY = 1.05
		} else {
			effect = null
			scaleX = 1.0
			scaleY = 1.0
		}
	}

	override doActivate() {
		text.fillProperty.bind(connection.strokeProperty)
		addBehavior(new MoveBehavior(this))
	}

	def void place(boolean force) {
		if(!connection.isActive)
			return
		val moveBehavior = getBehavior(MoveBehavior)
		if(moveBehavior?.isManuallyPlaced && !force) 
			return;
		if(force) 
			moveBehavior?.setIsManuallyPlaced(false)
		val center = connection.at(position)
		val derivative = connection.derivativeAt(position)
		var angle = atan2(derivative.y, derivative.x)
		val labelDx = -boundsInLocal.width / 2
		var labelDy = 1
		if (abs(angle) > PI / 2) {
			if (angle < 0)
				angle = angle + PI
			else if (angle > 0)
				angle = angle - PI
		}
		val transform = new Affine
		transform.translate(labelDx, labelDy)
		transform.rotate(angle.toDegrees)
		layoutX = transform.tx + center.x
		layoutY = transform.ty + center.y
		transform.tx = 0
		transform.ty = 0
		transforms.setAll(transform)
	}
	
	override toString() {
		'XConnectionLabel: ' + text.text
	}
}
