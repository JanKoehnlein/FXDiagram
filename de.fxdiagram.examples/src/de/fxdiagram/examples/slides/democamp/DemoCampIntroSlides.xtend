package de.fxdiagram.examples.slides.democamp

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.slides.SlideDiagram
import de.fxdiagram.lib.simple.OpenableDiagramNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.TextAlignment

import static de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory.*

import static extension de.fxdiagram.examples.slides.Animations.*
import javafx.scene.shape.Rectangle

@ModelNode
class DemoCampIntroSlides extends OpenableDiagramNode {
	
	new() {
		super('Introduction')
	}
	
	override doActivate() {
		innerDiagram = new SlideDiagram => [
			slides += createSlide('Title') => [ 
				stackPane => [
					children += new VBox => [
						alignment = Pos.CENTER
						StackPane.setMargin(it, new Insets(200, 0, 0, 0))
						children += createText('Eclipse Diagram Editors', 120) 
						children += createText('The FXed Generation', 64) 
					]
				]
			]
			slides += createSlide('Frameworks') => [ slide |
				slide.stackPane => [
					children += new Pane => [
//						setPrefSize(1024, 768)
						clip = new Rectangle(0, 0, 1024, 768)
						children += createText('Draw2D', 64) => [
							rotate = -16
							layoutX = 110
							layoutY = 145
							flicker(textColor, darkTextColor)
						]
						children += createText('Graphiti', 72) => [
							rotate = 5
							layoutX = 120
							layoutY = 652
							slide.onActivate = [| warp(1000) ]
						]
						children += createText('GMF', 72) => [
							rotate = 30
							layoutX = 840
							layoutY = 180
							breathe(textColor, darkTextColor)
						]
						children += new Group => [
							layoutX = 501
							layoutY = 367
							rotate = -15
							children += createText('Sirius', 72) => [
								orbit(370,150)
							]
						]
						children += createText('GEF', 92) => [
							rotate = 5
							layoutX = 490
							layoutY = 380
							spin
						]
					]
				]
			]
			slides += createClickThroughSlide('Title') => [ 
				it.pane => [
					addComparison('Framework', 'Product', 72, 100) 
					addComparison('Designed For Developers', 'Designed For Users', 48, 250)
					addComparison('High-level Abstractions', 'Customizability', 48, 325)
					addComparison('Pragmatic Behavior', 'Fun to Use', 48, 400)
					addComparison('Max Features w/ Min Effort', 'Embrace All Usecases', 48, 475)
					addComparison('Simplify Rendering', 'Appealing Visuals', 48, 550)
				]
			]
			slides += createSlideWithText('Problem', '''
					From a user's perspective
					you shouldn't use a framework...
				''', 72)
			slides += createSlideWithText('Problem', '''
					...but coding everything from scratch
					is not an option.
				''', 72)
			slides += createClickThroughSlide('JavaFX') => [ 
				pane => [
					children += new ImageView => [
						image = ImageCache.get.getImage(this, 'images/javafx.png')
						fitWidth = 587
						preserveRatio = true
						layoutX = 120
						layoutY = 140
					]
					children += new ImageView => [
						image = ImageCache.get.getImage(this, 'images/xtend.png')
						layoutX = 520
						layoutY = 440
					]
				]
			]
		]
		super.doActivate()
	}	
	
	protected def addComparison(Pane pane, String left, String right, int size, int y) {
		pane => [
			children += createText(left, size) => [
				layoutX = 100
				layoutY = y
				textAlignment = TextAlignment.LEFT
				textOrigin = VPos.TOP
				fill = darkTextColor
			]
			children += createText(right, size) => [
				layoutX = 612
				layoutY = y
				textAlignment = TextAlignment.LEFT
				textOrigin = VPos.TOP
			]
		] 
	}
}