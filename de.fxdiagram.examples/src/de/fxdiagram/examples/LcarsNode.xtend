package de.fxdiagram.examples

import de.fxdiagram.core.XNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.util.List
import java.util.Map
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class LcarsNode extends XNode {

	String name
	String imageUrl

	new(Map<String, Object> entry) {
		this.name = entry.get("name").toString
		this.imageUrl = (entry.get("imageUrl") as List<String>).last
		node = new RectangleBorderPane => [
			children += new VBox => [
				children += new ImageView => [
					image = new Image(imageUrl)
				]
				children += new Text => [
					text = name
					textOrigin = VPos.TOP
					VBox.setMargin(it, new Insets(5, 10, 5, 10))
				]
			]
		]
		key = name
	}

}
