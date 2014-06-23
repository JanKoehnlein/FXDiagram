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
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment

import static de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.examples.slides.Animations.*

@ModelNode
class DemoCampIntroSlides extends OpenableDiagramNode {
	
	new() {
		super('Introduction')
	}
	
	override doActivate() {
		innerDiagram = new SlideDiagram => [
			slides += createSlide('Title') => [
				val vbox = new VBox 
				stackPane => [
					children += vbox => [
						alignment = Pos.CENTER
						StackPane.setMargin(it, new Insets(350, 0, 0, 0))
						children += createText('Eclipse Diagram Editors', 120) 
						children += createText('The FXed Generation', 72)
						children += createText('Jan Koehnlein - itemis', 32) => [ 
							VBox.setMargin(it, new Insets(30,0,0,0))
						]
					]
				]
			]
			slides += createSlide('Frameworks') => [ slide |
				slide.stackPane => [
					children += new Pane => [
//						setPrefSize(1024, 768)
						clip = new Rectangle(0, 0, 1024, 768)
						children += new Group => [
							layoutX = 501
							layoutY = 367
							children += new Group => [
								rotate = 60
								children += createText('GMF', 50) => [
									orbit(110, 100, 10.seconds, 0)
								]
							]
						]
						children += new Group => [
							rotate = 30
							layoutX = 501
							layoutY = 367
							children += createText('Graphiti', 50) => [
								orbit(400, 250, 30.seconds, 0)
							]
						]
						children += new Group => [
							layoutX = 501
							layoutY = 367
							rotate = -15
							children += createText('Sirius', 50) => [
								orbit(300,200, 20.seconds, 0)
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
					addComparisonLeft('Framework', 72, 100) 
					addComparisonLeft('Designed For Developers', 48, 250)
					addComparisonLeft('High-level Abstractions', 48, 310)
					addComparisonLeft('Max Features w/ Min Effort', 48, 370)
					addComparisonLeft('Unified Usecase', 48, 430)
					addComparisonLeft('Standard Behavior', 48, 490)
					addComparisonLeft('Rendering Issues', 48, 550)
					addComparisonRight('Product', 72, 100) 
					addComparisonRight('Designed For Users', 48, 250)
					addComparisonRight('Custom Solution', 48, 310)
					addComparisonRight('Usability', 48, 370)
					addComparisonRight('Specific Usecase', 48, 430)
					addComparisonRight('Fun to Use', 48, 490)
					addComparisonRight('Stunning Visuals', 48, 550)
				]
			]
			slides += createSlideWithText('Problem', '''
					From a user's perspective
					you shouldn't use a framework...
				''', 72)
			slides += createSlideWithText('Problem', '''
					...but coding everything from scratch
					doesn't seem to be an option.
				''', 72)
			slides += createSlide('JavaFX') => [ 
				stackPane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/javafx.png')
					fitWidth = 587
					preserveRatio = true
				]
			]
			slides += createSlide('Xtend') => [ 
				stackPane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/xtend.png')
				]
			]
		]
		super.doActivate()
	}	
	
	protected def addComparisonLeft(Pane pane, String left, int size, int y) {
		pane => [
			children += createText(left, size) => [
				layoutX = 100
				layoutY = y
				textAlignment = TextAlignment.LEFT
				textOrigin = VPos.TOP
				fill = darkTextColor
			]
		]
	}

	protected def addComparisonRight(Pane pane, String right, int size, int y) {
		pane => [
			children += createText(right, size) => [
				layoutX = 612
				layoutY = y
				textAlignment = TextAlignment.LEFT
				textOrigin = VPos.TOP
			]
		] 
	}
}