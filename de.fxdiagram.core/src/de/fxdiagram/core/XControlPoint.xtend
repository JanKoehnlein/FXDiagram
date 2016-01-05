package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.behavior.ControlPointMoveBehavior
import de.fxdiagram.core.images.Magnet
import java.util.List
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.transform.Affine

import static de.fxdiagram.core.XControlPoint.Type.*
import static de.fxdiagram.core.extensions.Point2DExtensions.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import static extension java.lang.Math.*

/**
 * A point belonging to an {@link XConnection}.
 * 
 * Its {@link Type} denotes 
 * <ul>
 * <li>{@link Type#ANCHOR} an endpoint touching one of the connectied nodes,</li>
 * <li>{@link Type#INTERPOLATED} a point on the curve,</li>  
 * <li>{@link Type#CONTROL_POINT} a point next to the curve controling its shape, e.g. for spline curves.</li> 
 * <li> 
 */
@ModelNode('layoutX', 'layoutY', 'type')
class XControlPoint extends XShape {

	@FxProperty Type type = CONTROL_POINT

	protected override createNode() {
		switch type {
			case ANCHOR: {
				return new Circle => [
					radius = 3
					stroke = Color.BLUE
					fill = Color.WHITE
				]
			}
			case CONTROL_POINT: {
				return new Magnet
			}
			case INTERPOLATED: {
				return new Circle => [
					radius = 5
					stroke = Color.RED
					fill = Color.WHITE
				]
			}
			case DANGLING: {
				return new Circle => [
					radius = 5
					stroke = Color.RED
					fill = Color.RED
					opacity = 0.5
				]
			}
			default:
				return null
		}
	} 
	
	override protected doActivate() {
		typeProperty.addListener [
			p, o, n |
			children.remove(node)
			nodeProperty.set(null)
			getNode
			selectionFeedback(selected)
		]
		if (type != ANCHOR)
			addBehavior(new ControlPointMoveBehavior(this))
	}

	override selectionFeedback(boolean isSelected) {
		if (isSelected)
			switch type {
				case CONTROL_POINT:
					node.effect = new DropShadow
				case INTERPOLATED,
				case DANGLING:
					(node as Circle).fill = Color.RED
			}
		else {
			switch type {
				case CONTROL_POINT:
					node.effect = null
				case INTERPOLATED,
				case DANGLING:
					(node as Circle).fill = Color.WHITE
			}
		}
	}

	override isSelectable() {
		type != ANCHOR && super.isSelectable()
	}	

	def update(List<XControlPoint> siblings) {
		if (type == CONTROL_POINT) {
			val index = siblings.indexOf(this)
			if (index > 0 && index < siblings.size - 1) {
				val predecessor = siblings.get(index - 1)
				val successor = siblings.get(index + 1)
				val dx = successor.layoutX - predecessor.layoutX
				val dy = successor.layoutY - predecessor.layoutY
				var angle = atan2(dy, dx).toDegrees
				if (isClockwise(layoutX, layoutY, 
					successor.layoutX, successor.layoutY,
					predecessor.layoutX, predecessor.layoutY)) 
					angle = angle + 180
				val trafo = new Affine
				trafo.translate(-0.5 * node.layoutBounds.width, -node.layoutBounds.height - 5)
				trafo.rotate(angle)
				transforms.setAll(trafo)
			}
		} else {
			transforms.clear
		}
	}

	enum Type {
		ANCHOR,
		INTERPOLATED,
		CONTROL_POINT,
		DANGLING
	}
}

