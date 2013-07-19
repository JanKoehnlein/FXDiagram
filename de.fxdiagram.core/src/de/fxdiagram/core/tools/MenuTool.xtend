package de.fxdiagram.core.tools

import com.google.common.base.Charsets
import com.google.common.io.Files
import de.fxdiagram.core.XRootDiagram
import de.fxdiagram.core.export.SvgExporter
import de.fxdiagram.core.layout.Layouter
import eu.hansolo.enzo.radialmenu.MenuItem
import eu.hansolo.enzo.radialmenu.Options
import eu.hansolo.enzo.radialmenu.RadialMenu
import eu.hansolo.enzo.radialmenu.RadialMenu.State
import java.io.File
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent

import static eu.hansolo.enzo.radialmenu.Symbol.Type.*

import static extension javafx.util.Duration.*

class MenuTool implements XDiagramTool {

	XRootDiagram diagram

	EventHandler<KeyEvent> keyHandler
	EventHandler<MouseEvent> mouseHandler

	Group menuGroup
	RadialMenu menu
	MenuItem selection

	new(XRootDiagram diagram) {
		this.diagram = diagram
		keyHandler = [
			switch code {
				case KeyCode.E:
					if (shortcutDown) {
						doExportSvg
						consume
					}
				case KeyCode.L:
					if (shortcutDown) {
						doLayout
						consume
					}
				case KeyCode.Q:
					if (shortcutDown) {
						doExit
					}
				case KeyCode.ESCAPE: {
					closeMenu
					consume
				}
			}
		]
		menu = new RadialMenu(
			new Options => [
				degrees = 360
				offset = -90
				radius = 200
				buttonSize = 72
				buttonAlpha = 1.0
			],
			#[EJECT, GRAPH, CAMERA, PHOTO //, REFRESH, TAG, TAGS, TEXT, TOOL, SPEECH_BUBBLE, 
//				TRASH, UNDO, ZOOM_IN, ZOOM_OUT, WEB, MONITOR, SELECTION1, SELECTION2
			].
				map [ s |
					new MenuItem => [
						symbol = s
						tooltip = s.toString.toLowerCase.toFirstUpper
						size = 64
					]
				])
		mouseHandler = [
			if (menu.state == State.OPENED) {
				closeMenu
				consume
			} else if (target == diagram.scene && menu.state != State.OPENED) {
				openMenu
				consume
			}
		]

	}

	protected def openMenu() {
		menuGroup = new Group
		diagram.root.children += menuGroup => [
			translateX = 0.5 * diagram.scene.width
			translateY = 0.5 * diagram.scene.height
			children += menu => [
				show
				open
				onItemSelected = [
					selection = item
				]
				onMenuCloseFinished = [
					closeMenu
					if (selection != null) {
						switch selection.symbol {
							case GRAPH:
								doLayout
							case CAMERA:
								doExportSvg
							case EJECT:
								doExit
							default:
								println("Unhandled menu item " + selection)
						}
					}
					selection = null
				]
			]
		]
	}

	protected def closeMenu() {
		menu.hide
		if (menuGroup != null && menuGroup.parent != null) {
			diagram.root.children -= menuGroup
		}
	}

	override activate() {
		diagram.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		diagram.scene.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler)
		true
	}

	override deactivate() {
		diagram.scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseHandler)
		diagram.scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		true
	}

	def doLayout() {
		new Layouter().layout(diagram, 2.seconds)
	}

	def doExportSvg() {
		val svgCode = new SvgExporter().toSvg(diagram)
		Files.write(svgCode, new File("Diagram.svg"), Charsets.UTF_8)
	}

	def doExit() {
		System.exit(0)
	}
}
