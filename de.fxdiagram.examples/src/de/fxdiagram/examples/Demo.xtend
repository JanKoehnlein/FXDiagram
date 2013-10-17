package de.fxdiagram.examples

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionKind
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.lib.simple.SimpleNode
import javafx.application.Application
import javafx.concurrent.Task
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.stage.Stage

class Main extends Application {

	def static main(String... args) {
		launch(args)
	}

	int node_nr = 0

	override start(Stage it) {
		title = 'FX Diagram Demo'
		scene = createScene
		show
	}

	def createScene() {
		val root = new XRoot
		val scene = new Scene(root, 1024, 768)
		scene.setCamera(new PerspectiveCamera)
		root.activate
		val diagram = new XDiagram
		root.diagram = diagram
		
		diagram => [
			nodes += new OpenableDiagramNode('Basic') => [
				innerDiagram = createBasicDiagram('')
			]
		]
//			val source = new LevelOfDetailDiagramNode('LOD diagram') => [
//				innerDiagram = createDummyDiagram
//				layoutX = 280
//				layoutY = 170
//				width = 80
//				height = 30
//			]
//			nodes += source
//
//			val target = new SimpleNode('Simple Node') => [
//				layoutX = 280
//				layoutY = 280
//				width = 80
//				height = 30
//			]
//			nodes += target
//
//			val target2 = new OpenableDiagramNode('Openable') => [
//				innerDiagram = createDummyDiagram
//				layoutX = 400
//				layoutY = 240
//				width = 80
//				height = 30
//			]
//			nodes += target2
//
//			connections += new XConnection(source, target) => [
//				kind = XConnectionKind.QUAD_CURVE
//				new XConnectionLabel(it) => [
//					text.text = 'quadratic'
//				]
//			]
//
//			connections += new XConnection(source, target2) => [
//				kind = XConnectionKind.CUBIC_CURVE
//				new XConnectionLabel(it) => [
//					text.text = 'cubic'
//				]
//			]
//
//			connections += new XConnection(target, target2) => [
//				new XConnectionLabel(it) => [
//					text.text = 'polyline'
//				]
//			]
//
//			nodes += new ImageNode => [
//				image = ImageCache.get.getImage(this, 'media/seltsam.jpg')
//				layoutX = 100
//				layoutY = 100
//				width = 100
//			]
//
//			nodes += new MovieNode('Movie') => [
//				movieUrl = new URL(this.toURI('media/ScreenFlow.mp4'))
//				width = 640
//				height = 360
//				view.viewport = new Rectangle2D(0, 60, 640, 360)
//				layoutX = 100
//				layoutY = 200
//			]
//
//			nodes += new RecursiveImageNode(ImageCache.get.getImage(this, 'media/laptop.jpg'), 0, -6, 0.6) => [
//				width = 120
//				height = 90
//			]
//		
//			nodes += new BrowserNode => [
//				width = 120
//				height = 160
//				layoutX = 100
//				layoutY = 500
//				pageUrl = new URL('http://koehnlein.blogspot.de/')
//			]
//		
//			nodes += new BrickBreakerNode => [
//				width = 640
//				height = 480
//				layoutX = 500
//				layoutY = 100
//			]
//
//			nodes += new JavaTypeNode(Button) => [
//				width = 160
//				height = 120
//				layoutX = 500
//				layoutY = 200
//			]
//
//			nodes += new EClassNode(EcorePackage.Literals.ECLASS) => [
//				width = 160
//				height = 120
//				layoutX = 100
//				layoutY = 500
//			]
//
//			nodes += new NeonSignNode => [
//				layoutX = 500
//				layoutY = 10
//				width = 80
//				height = 30
//			]
//
//			nodes += new OpenableDiagramNode('LCARS') => [
//				innerDiagram = new LcarsDiagram
//				layoutX = 300
//				layoutY = 300
//				width = 80
//				height = 30
//			]
//
//			nodes += new LoginNode => [
//				layoutX = 200
//				layoutY = 200
//				width = 80
//				height = 30
//			]
//
//		]
		warmUpLayouter
		root.centerDiagram
		scene
	}
	
	def XDiagram createBasicDiagram(String nameSuffix) {
		new XDiagram() => [
			contentsInitializer = [
				val openable = new OpenableDiagramNode('Linked' + nameSuffix)
				val levelOfDetail = new LevelOfDetailDiagramNode('Embedded' + nameSuffix)
				val simple = new SimpleNode('Simple' + nameSuffix)
				nodes += simple => [
					layoutX = 50
					layoutY = 50				
				]
				nodes += openable => [
					layoutX = 350
					layoutY = 150
					innerDiagram = createBasicDiagram(nameSuffix + " (linked)")
				]
				nodes += levelOfDetail => [
					layoutX = 50
					layoutY = 300
					innerDiagram = createBasicDiagram(nameSuffix + " (embedded)")
				]
				connections += new XConnection(simple, openable) => [
					new XConnectionLabel(it) => [
						text.text = 'polyline'
					]
				]
				connections += new XConnection(openable, levelOfDetail) => [
					kind = XConnectionKind.QUAD_CURVE
					new XConnectionLabel(it) => [
						text.text = 'quadratic'
					]
				]
				connections += new XConnection(simple, levelOfDetail) => [
					kind = XConnectionKind.CUBIC_CURVE
					new XConnectionLabel(it) => [
						text.text = 'cubic'
					]
				]
			]
		]
	}
	
	protected def warmUpLayouter() {
		val Task<Void> task = [|
			new Layouter
			null
		]
		task.run
	}
	
	protected def XDiagram createDummyDiagram() {
		new XDiagram => [
			contentsInitializer = [
				nodes += new SimpleNode('Inner ' + node_nr) => [
					relocate(0,0)
				]
				nodes += new SimpleNode('Inner ' + node_nr + 1) => [
					relocate(100,100)
				]
				nodes += new LevelOfDetailDiagramNode('Nested ' + node_nr + 2) => [
					innerDiagram = createDummyDiagram
					it.relocate(50, 50)
				]
			]
			node_nr = node_nr + 3
		]
	}
}
