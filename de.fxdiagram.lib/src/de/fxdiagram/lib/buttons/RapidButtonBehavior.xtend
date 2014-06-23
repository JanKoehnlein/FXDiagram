package de.fxdiagram.lib.buttons

import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.extensions.InitializingListener
import javafx.animation.FadeTransition
import javafx.collections.ObservableList
import javafx.scene.Group
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import static javafx.collections.FXCollections.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

class RapidButtonBehavior<HOST extends XNode> extends AbstractHostBehavior<HOST> {

	@Property double border

	val pos2group = #{
		TOP -> new HBox,
		BOTTOM -> new HBox,
		LEFT -> new VBox, 
		RIGHT -> new VBox
	}
	
	ObservableList<RapidButton> buttonsProperty = observableArrayList
	
	Group allButtons = new Group
	
	val fadeTransition = new FadeTransition => [
		node = allButtons
		delay = 500.millis
		duration = 500.millis
		fromValue = 1
		toValue = 0
		onFinished = [
			allButtons.visible = false
		]
	]

	new(HOST host) {
		super(host)
		allButtons.children += pos2group.values
		allButtons.visible = false
	}

	def add(RapidButton button) {
		val group = pos2group.get(button.position)
		if(group == null)
			throw new IllegalArgumentException('Illegal XRapidButton position ' + button.position)
		buttonsProperty += button
	}

	def remove(RapidButton button) {
		val group = pos2group.get(button.position)
		if(group == null)
			throw new IllegalArgumentException('Illegal XRapidButton position ' + button.position)
		buttonsProperty -= button
	}

	override protected doActivate() {
		host.diagram.buttonLayer.children += allButtons
		buttonsProperty.addInitializingListener(new InitializingListListener<RapidButton>() => [
			add = [button | 
				button.activate
				val group = pos2group.get(button.position)
				button.enabledProperty.addInitializingListener(new InitializingListener => [
					set = [ 
						if(it) 
							group.children += button
						else 
							group.children -= button
						layout
					]
				])
			]
		])
		
		layout
		host.node.addEventHandler(MouseEvent.MOUSE_ENTERED, [ show ])
		host.node.addEventHandler(MouseEvent.MOUSE_EXITED, [ fade ])
		allButtons.onMousePressed = [
			allButtons.visible = false
		]
		pos2group.values.forEach[
			layoutBoundsProperty.addListener [
				p, o, n | 
				layout
			]
		]
		host.boundsInParentProperty.addListener [
			p, o, n | layout
		]
	}
	
	def show() {
		fadeTransition.stop
		allButtons => [
			visible = true
			opacity = 1.0
		]
	}
	
	def fade() {
		fadeTransition.play
	}
	
	override getBehaviorKey() {
		class
	}
	
	def layout() {
		val hostBounds = host.localToDiagram(host.layoutBounds)
		val hostCenter = hostBounds.center
		for(pos2group: pos2group.entrySet) {
			val pos = pos2group.key
			val group = pos2group.value
			group.layout
			val groupBounds = group.boundsInLocal
			val centered = hostCenter - groupBounds.center
			group => [
				layoutX = centered.x +
					switch(pos) {
						case LEFT:
							-0.5 * hostBounds.width - border - groupBounds.width
						case RIGHT:
							0.5 * hostBounds.width + border + groupBounds.width
						default:
						 	0
					}
				layoutY = centered.y +
					switch(pos) {
						case TOP:
							-0.5 * hostBounds.height - border - groupBounds.height
						case BOTTOM:
							0.5 * hostBounds.height + border +  groupBounds.height
						default:
						 	0
					}
			]
		}
	}
}
