package de.fxdiagram.core.tools

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.tools.actions.CloseAction
import eu.hansolo.enzo.radialmenu.MenuItem
import eu.hansolo.enzo.radialmenu.Options
import eu.hansolo.enzo.radialmenu.RadialMenu
import eu.hansolo.enzo.radialmenu.RadialMenu.State
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

import static extension javafx.scene.layout.BorderPane.*

@Logging
class DiagramActionTool implements XDiagramTool {

	XRoot root

	EventHandler<KeyEvent> keyHandler
	EventHandler<MouseEvent> mouseHandler

	Group menuGroup
	RadialMenu menu
	MenuItem selection
	
	new(XRoot root) {
		this.root = root
		keyHandler = [ event |
			root.diagramActionRegistry.actions
				.filter[matches(event)]
				.forEach[ 
					if (!event.consumed) 
						it.perform(root)
				]
			if(event.code == KeyCode.ESCAPE) {
				event.consume
				if(menu?.state == State.OPENED) {
					closeMenu
					return
				} else {
					new CloseAction
				}
			}
		]
		mouseHandler = [
			if(it.button == MouseButton.SECONDARY) {
				if (menu?.state == State.OPENED) {
					closeMenu
					consume
				} else if (target == root.diagramCanvas && menu?.state != State.OPENED) {
					openMenu
					consume
				}
			}
		]
	}

	protected def openMenu() {
		menu = new RadialMenu(
				new Options => [
					degrees = 360
					offset = -90
					radius = 200
					buttonSize = 72
					buttonAlpha = 1.0
				],
				root.diagramActionRegistry.actions
					.filter[symbol != null]
					.map[ action |
						new MenuItem => [
							symbol = action.symbol
							tooltip = action.tooltip
							size = 64
						]
				])
		root.headsUpDisplay.children += menuGroup = new Group => [
			alignment = Pos.CENTER
			translateX = 0.5 * root.scene.width
			translateY = 0.5 * root.scene.height
			children += menu => [
				show
				open
				onItemSelected = [
					selection = item
				]
				onMenuCloseFinished = [
					closeMenu
					if (selection != null) {
						val action = root.diagramActionRegistry.getBySymbol(selection.symbol)
						if(action == null) 
							LOG.warning("Unhandled menu item " + selection)
						action?.perform(root)
					}
					selection = null
				]
			]
		]
	}

	protected def closeMenu() {
		menu.hide
		if (menuGroup != null && menuGroup.parent != null) {
			root.headsUpDisplay.children -= menuGroup
		}
	}

	override activate() {
		root.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		root.diagramCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler)
		true
	}

	override deactivate() {
		root.diagramCanvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler)
		root.scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		true
	}
}
