package de.fxdiagram.examples.slides

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.lib.simple.OpenableDiagramNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.scene.shape.Polyline

import static de.fxdiagram.examples.slides.Styles.*

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.UriExtensions.*

class IntroductionSlideDeck extends OpenableDiagramNode {
	new() {
		super('Introduction')
		innerDiagram = new SlideDiagram => [
			slides += new Slide('Title') => [ 
				stackPane => [
					children += new Pane => [
						setPrefSize(1024, 768)
						children += createJungleText('GEF', 22) => [
							fill = jungleDarkGreen							
							rotate = 340
							layoutX = 110
							layoutY = 379
						]
						children += createJungleText('Draw2D', 22) => [
							fill = jungleDarkGreen							
							rotate = 339
							layoutX = 405
							layoutY = 147
						]
						children += createJungleText('GMF RT', 22) => [
							fill = jungleDarkGreen							
							rotate = 93
							layoutX = 702
							layoutY = 221
						]
						children += createJungleText('Graphiti', 22) => [
							fill = jungleDarkGreen							
							rotate = 325
							layoutX = 850
							layoutY = 319
						]
						children += createJungleText('Sirius', 22) => [
							fill = jungleDarkGreen							
							rotate = 67
							layoutX = 188
							layoutY = 229
						]
						children += createJungleText('GMF Tooling', 22) => [
							fill = jungleDarkGreen							
							rotate = 267
							layoutX = 525
							layoutY = 231
						]
					]
					children += new VBox => [
						alignment = Pos.CENTER
						StackPane.setMargin(it, new Insets(400, 0, 0, 0))
						children += createText('Eclipse Diagram Editors', 93) => [
							fill = Color.rgb(238, 191, 171)
						]
						children += createText('An Endangered Species', 48) => []
					]
				]
			]
			slides += new Slide('The Eclipse Jungle', 110)
			slides += new Slide('Jungle images') => [
				stackPane => [
					children += new Pane => [
						setPrefSize(1024, 768)
						children += createJungleText('GEF', 48) => [
							fill = jungleDarkGreen
							rotate = 26
							layoutX = 240
							layoutY = 637
						]
						children += createJungleText('Draw2D', 48) => [
							fill = jungleDarkGreen
							rotate = 338
							layoutX = 380
							layoutY = 132
						]
						children += createJungleText('GMF RT', 48) => [
							fill = jungleDarkGreen
							rotate = 10
							layoutX = 740
							layoutY = 337
						]
						children += createJungleText('Graphiti', 48) => [
							fill = jungleDarkGreen
							rotate = 332
							layoutX = 670
							layoutY = 580
						]
						children += createJungleText('Sirius', 48) => [
							fill = jungleDarkGreen
							rotate = 67
							layoutX = 111
							layoutY = 167
						]
						children += createJungleText('GMF Tooling', 48) => [
							fill = jungleDarkGreen
							rotate = 355
							layoutX = 40
							layoutY = 430
						]
					]
				]
			]
			slides += new Slide('Darkness', 144)
			slides += new ClickThroughSlide('Darkness images') => [
				pane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/darkness1.png')
					opacity = 0.8
					layoutX = 45
					layoutY = 45
				]
				pane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/darkness2.png')
					opacity = 0.8
					layoutX = 420
					layoutY = 374
				]
			]
			slides += new Slide('Behavior', 144)
			slides += new ClickThroughSlide('Behavior images') => [ 
				pane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/graphiti.png')
					opacity = 0.8
					layoutX = 50
					layoutY = 44
				]	
				pane.children += new MediaView => [
					opacity = 0.9
					layoutX = 295
					layoutY = 332
					mediaPlayer = new MediaPlayer(
							new Media(toURI('/de/fxdiagram/examples/media/Usability.mp4'))) => [
						seek(200.seconds)
//								play
					]
				]					
			]
			slides += new Slide('Recycling', 144)
			slides += new ClickThroughSlide('Recycling images') => [ 
				pane => [
					children += new ImageView => [
						image = ImageCache.get.getImage(this, 'images/onion.png')
						fitWidth = 570
						preserveRatio = true
						layoutX = 227
						layoutY = 110
					]
					children += new Group => [
						children += createJungleText('OS', 48) => [
							layoutX = 173
							layoutY = 216
						] 
						children += new Polyline => [
							points += #[ 244.0, 226.0, 537.0, 356.0 ]
							stroke = jungleGreen
							strokeWidth = 2
						]
					]
					children += new Group => [
						children += createJungleText('SWT', 48) => [
							layoutX = 62
							layoutY = 458
						]
						children += new Polyline => [
							points += #[ 176.0, 439.0, 501.0, 367.0]
							stroke = jungleGreen
							strokeWidth = 2
						]
					] 
					children += new Group => [
						children += createJungleText('Draw2D', 48) => [
							layoutX = 129
							layoutY = 666
						] 
						children += new Polyline => [
							points += #[ 307.0, 611.0, 489.0, 433.0 ]
							stroke = jungleGreen
							strokeWidth = 2
						]
					]
					children += new Group => [
						children += createJungleText('GEF MVC', 48) => [
							layoutX = 581
							layoutY = 712
						] 
						children += new Polyline => [
							points += #[ 714.0, 662.0, 588.0, 458.0 ]
							stroke = jungleGreen
							strokeWidth = 2
						]
					]
					children += new Group => [
						children += createJungleText('''
							GMF
							Runtime''', 48) => [
							layoutX = 770
							layoutY = 462
						]
						children += new Polyline => [
							points += #[ 803.0, 462.0, 658.0, 416.0 ]
							stroke = jungleGreen
							strokeWidth = 2
						]
					]
					children += new Group => [
						children += createJungleText('''
							GMF
							Tooling''', 48) => [
							layoutX = 770
							layoutY = 184
						] 
						children += new Polyline => [
							points += #[ 766.0, 206.0, 662.0, 281.0 ]
							stroke = jungleGreen
							strokeWidth = 2
						]
					]
					children += new Group => [
						children += createJungleText('Epsilon', 48) => [
							layoutX = 405
							layoutY = 88
						]
						children += new Polyline => [
							points += #[ 519.0, 101.0, 525.0, 188.0 ]
							stroke = jungleGreen
							strokeWidth = 2
						]
					] 
				]
			]
			slides += new Slide('Reproduction', 144)
			slides += new ClickThroughSlide('Reproduction images') => [ 
				pane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/graphiti_code.png')
					layoutX = 43
					layoutY = 41
					opacity = 0.8
				]
				pane.children += new VBox => [
					layoutX = 313
					layoutY = 81
					padding = new Insets(5, 5, 5, 5)
					alignment = Pos.CENTER
					style = '''
						-fx-border-color: black;
						-fx-border-width: 1;
						-fx-background-color: rgb(252,228,153);
					'''
					children += createText('34 Files', 24) => [
						fill = Color.BLACK
					]
					children += createText('2730 LOC', 24) => [
						fill = Color.BLACK
					]
				]
				pane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/gmf_dashboard.png')
					layoutX = 284
					layoutY = 406
					opacity = 0.8
				]
			]
			slides += new Slide('Endangerment', 144)
			slides += new ClickThroughSlide('Tablet') => [ 
				pane.children += new ImageView => [
					image = ImageCache.get.getImage(this, 'images/tablet.png')
					layoutX = 183
					layoutY = 210
					fitWidth = 587
					preserveRatio = true
				]
				pane.children += new ImageView => [ 
					image = ImageCache.get.getImage(this, 'images/hand.png')
					layoutX = 540
					layoutY = 244
				]
			]
			slides += new Slide('Help') => [ 
				stackPane => [
					children += new VBox => [
						alignment = Pos.CENTER
						spacing = 50
						children += createText('Help Us', 144) => []
						children += createText('save the', 72) => []
						children += createText('Eclipse Diagram Editors', 96) => [
							fill = Color.rgb(238, 191, 171)
						]
					]
				]
			]
			slides += new Slide(
				'''
					We have improve visual design,
					haptic behavior,
					usability,
					and customizability
					in order to save them from extinction.
				''', 48)
			slides += new Slide('JavaFX') => [ 
				stackPane => [
					children += new ImageView => [
						image = ImageCache.get.getImage(this, 'images/javafx.png')
						fitWidth = 587
						preserveRatio = true
					]
				]
			]
		]
	}
}
