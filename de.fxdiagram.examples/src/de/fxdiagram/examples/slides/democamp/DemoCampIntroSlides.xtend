package de.fxdiagram.examples.slides.democamp

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.slides.SlideDiagram
import de.fxdiagram.lib.simple.OpenableDiagramNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox

import static de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory.*

import static extension de.fxdiagram.examples.slides.Animations.*
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class DemoCampIntroSlides extends OpenableDiagramNode {
	
	new() {
		super('Summary')
	}
	
	override doActivate() {
		innerDiagram = new SlideDiagram => [
			slides += createSlide('Title') => [ 
				stackPane => [
					children += new VBox => [
						alignment = Pos.CENTER
						StackPane.setMargin(it, new Insets(200, 0, 0, 0))
						children += createText('Eclipse Diagram Editors', 120) 
						children += createText('The Next Generation', 64) 
					]
				]
			]
			slides += createSlide('Jungle images') => [
				stackPane => [
					children += new Pane => [
						setPrefSize(1024, 768)
						children += createText('GEF', 64) => [
							rotate = 16
							layoutX = 180
							layoutY = 665
							flicker(textColor, darkTextColor)
						]
						children += createText('Draw2D', 64) => [
							rotate = 338
							layoutX = 480
							layoutY = 232
							crawl
						]
						children += createText('GMF RT', 64) => [
							rotate = 10
							layoutX = 700
							layoutY = 300
							crawl
						]
						children += createText('GMF Tooling', 64) => [
							rotate = 332
							layoutX = 740
							layoutY = 620
							breathe(textColor, darkTextColor)
						]
						children += createText('Graphiti', 64) => [
							layoutX = 211
							layoutY = 167
							dangle
						]
						children += createText('Sirius', 64) => [
							rotate = 5
							layoutX = 290
							layoutY = 480
							breathe(textColor, darkTextColor)
						]
					]
				]
			]
			slides += createSlide('Title') => [ 
				stackPane => [
					children += new VBox => [
						alignment = Pos.CENTER
						children += createText('Frustration', 100) 
					]
				]
			]
			slides += createSlide('JavaFX') => [ 
				stackPane => [
					children += new ImageView => [
						image = ImageCache.get.getImage(this, 'images/javafx.png')
						fitWidth = 587
						preserveRatio = true
					]
				]
			]
		]
		super.doActivate()
	}	
	
}