package de.fxdiagram.examples.slides

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.lib.simple.OpenableDiagramNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import static extension de.fxdiagram.examples.slides.Styles.*

class SummarySlideDeck extends OpenableDiagramNode {
	new() {
		super('Summary')
		innerDiagram = new SlideDiagram => [
			slides += new Slide('Summary', 144)
			slides += new Slide(
				'''
					The users must be our top priority.
					Not developers.
					Not frameworks.
				''', 48)
			slides += new Slide('JavaFX advantages') => [
				stackPane.children += new VBox => [
					alignment = Pos.CENTER
					children += new ImageView => [
						image = ImageCache.get.getImage(this, 'images/javafx.png')
						fitWidth = 300
						preserveRatio = true
						VBox.setMargin(it, new Insets(0,0,-30,0))
					]
					children += createText('''
						provides superior graphics,
						allows to focus on usability,
						and leverages the hardware...
					''', 48)
				]
			]
			slides += new Slide(
				'''
					...and makes 
					developing graphical editors
					fun again.
				''', 48)
			slides += new Slide('Thanks') => [
				stackPane.children += new VBox => [
					alignment = Pos.CENTER
					children += createText('Thanks to', 144) => [
						VBox.setMargin(it, new Insets(0,0,30,0))
					]
					children += createMixedText('Gerrit Grunwald', 'for JavaFX inspiration')
					children += createMixedText('Tom Schindl', 'for e(fx)clipse')
					children += createMixedText('The KIELER framework', 'for auto-layout')
					children += createMixedText('GaphViz', 'for the layout algorithm')
					children += createMixedText('Memory Alpha', 'for the data on StarTrek')
					children += createMixedText('GTJLCARS', 'for the StarTrek font')
				]
			]
		]
	}
	
	def protected createMixedText(String jungleText, String normalText) {
		new HBox => [
			alignment = Pos.CENTER
			spacing = 16
			children += createJungleText(jungleText, 36) => [
				breathe
			]
			children += createText(normalText, 36)
		]
	}
}

