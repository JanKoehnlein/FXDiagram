package de.fxdiagram.core.tools

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.tools.actions.CenterAction
import de.fxdiagram.core.tools.actions.DiagramAction
import de.fxdiagram.core.tools.actions.ExitAction
import de.fxdiagram.core.tools.actions.ExportSvgAction
import de.fxdiagram.core.tools.actions.LayoutAction
import de.fxdiagram.core.tools.actions.SelectAllAction
import de.fxdiagram.core.tools.actions.ZoomToFitAction
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

import static eu.hansolo.enzo.radialmenu.Symbol.Type.*

import static extension javafx.scene.layout.BorderPane.*

@Logging
class MenuTool implements XDiagramTool {

	XRoot root

	EventHandler<KeyEvent> keyHandler
	EventHandler<MouseEvent> mouseHandler

	Group menuGroup
	RadialMenu menu
	MenuItem selection

	new(XRoot root) {
		this.root = root
		keyHandler = [
			val DiagramAction action = switch code {
				case KeyCode.A:
					if (shortcutDown) {
						consume
						new SelectAllAction
					}
				case KeyCode.C:
					if (shortcutDown) {
						consume
						new CenterAction
					}
				case KeyCode.E:
					if (shortcutDown) {
						consume
						new ExportSvgAction
					}
				case KeyCode.F:
					if (shortcutDown) {
						consume
						new ZoomToFitAction
					}
				case KeyCode.L:
					if (shortcutDown) {
						consume
						new LayoutAction
					}
				case KeyCode.Q:
					if (shortcutDown) {
						new ExitAction
					}
				case KeyCode.ESCAPE: {
					consume
					closeMenu
					null
				}
				default: null
			}
			action?.perform(root)
		]
		menu = new RadialMenu(
			new Options => [
				degrees = 360
				offset = -90
				radius = 200
				buttonSize = 72
				buttonAlpha = 1.0
			],
			#[EJECT, GRAPH, CAMERA, SELECTION1, SELECTION2, ZOOM_IN
			  //, PHOTO, REFRESH, TAG, TAGS, TEXT, TOOL, SPEECH_BUBBLE, 
			  // TRASH, UNDO, ZOOM_IN, ZOOM_OUT, WEB, MONITOR, DELETE,
			].
				map [ s |
					new MenuItem => [
						symbol = s
						size = 64
					]
				])
		mouseHandler = [
			if(it.button == MouseButton.SECONDARY) {
				if (menu.state == State.OPENED) {
					closeMenu
					consume
				} else if (target == root.diagramCanvas && menu.state != State.OPENED) {
					openMenu
					consume
				}
			}
		]

	}

	protected def openMenu() {
		menuGroup = new Group
		root.headsUpDisplay.children += menuGroup => [
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
						val DiagramAction action = switch selection.symbol {
							case GRAPH:
								new LayoutAction
							case CAMERA:
								new ExportSvgAction
							case EJECT:
								new ExitAction
							case SELECTION1:
								new SelectAllAction
							case SELECTION2:
								new CenterAction
							case ZOOM_IN:
								new ZoomToFitAction
							default: {
								LOG.warning("Unhandled menu item " + selection)
								null								
							}
						}
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
