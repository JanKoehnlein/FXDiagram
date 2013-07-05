package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XNode
import java.net.URL
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.scene.web.WebView

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
			]
			back = view = new WebView 
		]
	}

	def setPageUrl(URL pageUrl) {
		view.engine.load(pageUrl.toString)
	}

	
	def getView() {
		view
	}

}