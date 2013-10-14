package de.fxdiagram.lib.media

import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.net.URL
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.scene.web.WebView

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

class BrowserNode extends XNode {
	
	WebView view

	new() {
		node = new FlipNode => [
			front = new RectangleBorderPane => [
				children += new Text => [
					text = "My Blog"
					textOrigin = VPos.TOP
					StackPane.setMargin(it, new Insets(10, 20, 10, 20))
				]
				tooltip = 'Double-click to browse'
			]
			back = view = new WebView => [
				tooltip = 'Double-click to close'
			]
			flipOnDoubleClick = true
		]
	}

	def setPageUrl(URL pageUrl) {
		view.engine.load(pageUrl.toString)
	}

	
	def getView() {
		view
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
}