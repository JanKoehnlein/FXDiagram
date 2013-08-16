package de.fxdiagram.lib.simple

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text

import static extension de.fxdiagram.core.Extensions.*
import eu.hansolo.enzo.radialmenu.Symbol
import eu.hansolo.enzo.radialmenu.SymbolCanvas
import javafx.scene.Node

class OpenableDiagramNode extends XNode {
	
	XDiagram nestedDiagram
	
	XDiagram parentDiagram 
	
	XRoot root
	
	new(String name, XDiagram nestedDiagram) {
		this.nestedDiagram = nestedDiagram
		node = new RectangleBorderPane => [
			children += new Text => [
				text = name
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
		]
		key = name
	}
	
	override createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}

	override doActivate() {
		super.doActivate()
		this.root = getRoot
		node.onMouseClicked = [
			if(clickCount == 2) {
				parentDiagram = root.diagram				
				root.diagram = nestedDiagram
				root.headsUpDisplay.add(
					SymbolCanvas.getSymbol(Symbol.Type.ZOOM_OUT, 32, Color.GRAY) => [
						onMouseClicked = [
							root.diagram = parentDiagram
							root.headsUpDisplay.children -= target as Node
						]
					], Pos.TOP_RIGHT)
			}
		]
	}
}