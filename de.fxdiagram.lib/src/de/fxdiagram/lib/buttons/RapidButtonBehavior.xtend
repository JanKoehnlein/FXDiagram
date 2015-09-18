package de.fxdiagram.lib.buttons

import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.extensions.InitializingListListener
import javafx.animation.FadeTransition
import javafx.collections.ObservableList
import javafx.scene.Group
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import org.eclipse.xtend.lib.annotations.Accessors

import static javafx.collections.FXCollections.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import de.fxdiagram.core.extensions.InitializingListener

/**
 * Adds rapid buttons to a host {@link XNode}.
 * 
 * @see RapidButton
 */
class RapidButtonBehavior<HOST extends XNode> extends AbstractHostBehavior<HOST> {

	@Accessors double border

	ObservableList<RapidButton> buttonsProperty = observableArrayList
	
	val pos2group = #{
		TOP -> new HBox,
		BOTTOM -> new HBox,
		LEFT -> new VBox,
		RIGHT -> new VBox			
	} 
	
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
		allButtons.visible = false
		allButtons.children += pos2group.values
	}

	def add(RapidButton button) {
		buttonsProperty += button
	}

	def remove(RapidButton button) {
		buttonsProperty -= button
	}

	override protected doActivate() {
		buttonsProperty.addInitializingListener(new InitializingListListener<RapidButton>() => [
			add = [ button | 
				button.activate
				button.addEventHandler(MouseEvent.MOUSE_CLICKED, [ allButtons.visible = false ])
			]
		])
		updateButtons
		layout
		host.node.addEventHandler(MouseEvent.MOUSE_ENTERED, [ show ])
		host.node.addEventHandler(MouseEvent.MOUSE_EXITED, [ fade ])
		allButtons.onMouseEntered = [ show ]
		allButtons.onMouseExited = [ fade ]
		host.boundsInParentProperty.addListener [
			p, o, n |
			if(allButtons.visible) 
				layout
		]
		host.parentProperty.addInitializingListener(new InitializingListener => [
			set = [
				diagram.buttonLayer.children += allButtons
			]
			unset = [
				if(it != null)
					diagram.buttonLayer.children -= allButtons				
			]
		])
	}
	
	def show() {
		val blChildren = host.diagram.buttonLayer.children
		if(!blChildren.contains(allButtons)) 
			blChildren += allButtons
		fadeTransition.stop
		if(!allButtons.visible)  {
			updateButtons			
			layout
		}
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
	
	protected def updateButtons() {
		for(button: buttonsProperty) {
			val group = pos2group.get(button.position).children
			if(button.action.isEnabled(host)) {
				if(!group.contains(button)) 
					group += button
			} else {
				if(group.contains(button))
					group -= button
			}
		} 
	}
	
	protected def layout() {
		val hostBounds = host.localToDiagram(host.layoutBounds)
		val hostCenter = hostBounds?.center
		if(hostCenter != null) {
			for(entry: pos2group.entrySet) {
				val pos = entry.key
				val group = entry.value
				group.autosize
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
}
