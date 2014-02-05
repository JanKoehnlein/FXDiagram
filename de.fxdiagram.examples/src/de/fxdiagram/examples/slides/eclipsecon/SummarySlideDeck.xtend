package de.fxdiagram.examples.slides.eclipsecon

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.slides.SlideDiagram
import de.fxdiagram.lib.simple.OpenableDiagramNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import static extension de.fxdiagram.examples.slides.Animations.*

import static de.fxdiagram.examples.slides.eclipsecon.EclipseConSlideFactory.*

class SummarySlideDeck extends OpenableDiagramNode {
	
	new() {
		this.name = 'Summary'
		innerDiagram = new SlideDiagram => [
			slides += createSlide('Summary', 144)
			slides += createSlide(
				'''
					The users must be our top priority.
					Not developers.
					Not frameworks.
				''', 48)
			slides += createSlide('JavaFX advantages') => [
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
			slides += createSlide(
				'''
					...and makes 
					developing graphical editors
					fun again.
				''', 48)
			slides += createSlide('Thanks') => [
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
				breathe(jungleDarkGreen, jungleDarkestGreen)
			]
			children += createText(normalText, 36)
		]
	}
}

