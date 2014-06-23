package de.fxdiagram.examples.slides.democamp

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.examples.slides.SlideDiagram
import de.fxdiagram.lib.simple.OpenableDiagramNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import static de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory.*

import static extension de.fxdiagram.examples.slides.Animations.*

@ModelNode
class DemoCampSummarySlides extends OpenableDiagramNode {

	new() {
		super('Summary')
	}
	
	override doActivate() {
		innerDiagram = new SlideDiagram => [
			slides += createSlide('Conclusion') => [
				stackPane.children += createText('Conclusion', 100)
			]
			slides += createSlideWithText('Conclusion 2', '''
					Diagram frameworks
					don't help when you want to build
					user-centric editors.
				''', 72)
			slides += createSlideWithText('Conclusion 3', '''
					Use
					a state-of-the-art graphics engine
					combined with a 
					powerful programming language...
				''', 72)
			slides += createSlideWithText('Conclusion 4', '''
					...to take back the control
					over
					quality and usability...
				''', 72)
			slides += createSlideWithText('Conclusion 5', '''
					...and build diagram editors
					that really make 
					a dent in the universe.
				''', 72)
			slides += createSlide('Credits') => [
				stackPane.children += new VBox => [
					alignment = Pos.CENTER
					children += createText('Thanks to', 144) => [
						VBox.setMargin(it, new Insets(0, 0, 30, 0))
					]
					children += createMixedText('Gerrit Grunwald', 'for JavaFX inspiration')
					children += createMixedText('Tom Schindl', 'for e(fx)clipse')
					children += createMixedText('The KIELER framework', 'for auto-layout')
					children += createMixedText('GraphViz', 'for the layout algorithm')
					children += createMixedText('Memory Alpha', 'for the data on StarTrek')
					children += createMixedText('GTJLCARS', 'for the StarTrek font')
				]
			]
		]
		super.doActivate
	}

	def protected createMixedText(String jungleText, String normalText) {
		new HBox => [
			alignment = Pos.CENTER
			spacing = 24
			children += createText(jungleText, 40) => [
				flicker(textColor, darkTextColor)
			]
			children += createText(normalText, 36)
		]
	}
}
