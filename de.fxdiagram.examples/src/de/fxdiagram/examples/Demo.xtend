package de.fxdiagram.examples

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionKind
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.ecore.EClassNode
import de.fxdiagram.examples.java.JavaTypeNode
import de.fxdiagram.examples.lcars.LcarsDiagram
import de.fxdiagram.examples.login.LoginNode
import de.fxdiagram.examples.neonsign.NeonSignNode
import de.fxdiagram.examples.slides.eclipsecon.IntroductionSlideDeck
import de.fxdiagram.examples.slides.eclipsecon.SummarySlideDeck
import de.fxdiagram.lib.media.BrowserNode
import de.fxdiagram.lib.media.ImageNode
import de.fxdiagram.lib.media.MovieNode
import de.fxdiagram.lib.media.RecursiveImageNode
import de.fxdiagram.lib.simple.AddRapidButtonBehavior
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.lib.simple.SimpleNode
import java.net.URL
import javafx.application.Application
import javafx.concurrent.Task
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import org.eclipse.emf.ecore.EcorePackage

import static extension de.fxdiagram.core.extensions.UriExtensions.*
import static extension javafx.util.Duration.*
import javafx.application.Platform
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider
import de.fxdiagram.examples.java.JavaModelProvider

class Demo extends Application {

	XRoot root
	
	def static main(String... args) {
		launch(args)
	}

	override start(Stage it) {
		title = 'FX Diagram Demo'
		scene = createScene
//		fullScreen = true
		show
	}

	def createScene() {
		root = new XRoot
		val scene = new Scene(root, 1024, 768)
		scene.setCamera(new PerspectiveCamera)
		root.activate
		val diagram = new XDiagram
		root.diagram = diagram
		root.domainObjectProviders.add(new EcoreDomainObjectProvider)
		root.domainObjectProviders.add(new JavaModelProvider)

		diagram => [
//			nodes += new DemoCampIntroSlides
			nodes += new IntroductionSlideDeck
			nodes += new OpenableDiagramNode => [
				name = 'Basic'
				innerDiagram = createBasicDiagram('')
			]
			nodes += new OpenableDiagramNode => [
				name = 'JavaFX'
				innerDiagram = new XDiagram => [
					contentsInitializer = [
						nodes += newLoginNode
						nodes += newRecursiveImageNode
						nodes += newImageNode
						nodes += newMovieNode
						nodes += newBrowserNode
						nodes += newBrickBreakerNode
					]
				]
			]
			nodes += openableDiagram('Xtend', newNeonSignNode)
			nodes += openableDiagram('JavaFX Explorer', newJavaTypeNode)
			nodes += openableDiagram('Ecore Explorer', newEClassNode)
//			nodes += newLcarsDiagramNode
			nodes += new SimpleNode('Eclipse')
//			nodes += newGalleryDiagramNode()
//			nodes += new DemoCampSummarySlides
			nodes += new SummarySlideDeck
			val deltaX = scene.width / (nodes.size + 2)
			val deltaY = scene.height / (nodes.size + 2)
			nodes.forEach[
				node, i |
				node.layoutX = i * deltaX - node.layoutBounds.width / 2
				node.layoutY = i * deltaY - node.layoutBounds.height / 2
			]
			nodes.subList(0, nodes.size - 1).forEach [
				node, i |
				connections += new XConnection(node, nodes.get(i+1)) 
			]
		]
		warmUpLayouter
		Platform.runLater[|
			diagram.centerDiagram(true)
		]
		scene
	}
	
	def newGalleryDiagramNode() {
		new OpenableDiagramNode => [
			name = 'Gallery'
			innerDiagram = new XDiagram => [
				contentsInitializer = [
					nodes += newSimpleNode('')
					nodes += newOpenableBasicDiagramNode('')
					nodes += newEmbeddedBasicDiagram('')
					nodes += newNeonSignNode
					nodes += newJavaTypeNode
					nodes += newEClassNode
					nodes += newLoginNode
					nodes += newRecursiveImageNode
					nodes += newImageNode
					nodes += newMovieNode
					nodes += newBrowserNode
					nodes += newBrickBreakerNode
					nodes += newLcarsDiagramNode
				]
			]
		]
	}
	
	def newLcarsDiagramNode() {
		new OpenableDiagramNode => [
			name = 'LCARS'
			innerDiagram = new LcarsDiagram
		]
	}
	
	def XDiagram createBasicDiagram(String nameSuffix) {
		new XDiagram => [
			contentsInitializer = [
				val simple = newSimpleNode(nameSuffix)
				val openable = newOpenableBasicDiagramNode(nameSuffix)
				val levelOfDetail = newEmbeddedBasicDiagram(nameSuffix)
				nodes += simple => [
					layoutX = 75
					layoutY = 50
				]
				nodes += openable => [
					layoutX = 350
					layoutY = 150
				]
				nodes += levelOfDetail => [
					layoutX = 50
					layoutY = 300
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
	
	protected def openableDiagram(String name, XNode node) {
		new OpenableDiagramNode => [
			it.name = name
			innerDiagram = new XDiagram => [
				contentsInitializer = [
					nodes += node
				]
			]
		]
	}

	protected def void addRapidButtons(XNode node, String nameSuffix) {
		node.addBehavior(new AddRapidButtonBehavior(node) => [
			choiceInitializer = [
				for(i: 5..20) 
					addChoice(newSimpleNode(' ' + i))
				addChoice(newSimpleNode(nameSuffix))
				addChoice(newOpenableBasicDiagramNode(nameSuffix))
				addChoice(newEmbeddedBasicDiagram(nameSuffix))
				addChoice(newMovieNode)
				addChoice(newBrowserNode)
				addChoice(newBrickBreakerNode)
//				addChoice(newImageNode)
//				addChoice(newRecursiveImageNode)
				for(i: 1..4) 
					addChoice(newSimpleNode(' ' + i))
				addChoice(newSimpleNode(nameSuffix))
			]
		])
	}
	
	def newSimpleNode(String nameSuffix) {
		new SimpleNode('Node' + nameSuffix) => [
			if(!nameSuffix.empty)
				addRapidButtons(nameSuffix)
		]
	}
	
	def newOpenableBasicDiagramNode(String nameSuffix) {
		new OpenableDiagramNode => [
			name = 'Nested' + nameSuffix
			innerDiagram = createBasicDiagram(nameSuffix + " (nested)")
			addRapidButtons(nameSuffix)
		]
	}
	
	def newEmbeddedBasicDiagram(String nameSuffix) {
		new LevelOfDetailDiagramNode => [
			name = 'Embedded' + nameSuffix
			innerDiagram = createBasicDiagram(nameSuffix + " (embedded)")
			addRapidButtons(nameSuffix)
		]
	}
	
	def newLoginNode() {
		new LoginNode
	}
	
	def newEClassNode() {
		val provider = root.getDomainObjectProvider(EcoreDomainObjectProvider)
		new EClassNode => [
			domainObject = provider.createEClassHandle(EcorePackage.Literals.ECLASS)
		]
	}
	
	def newJavaTypeNode() {
		val provider = root.getDomainObjectProvider(JavaModelProvider)
		new JavaTypeNode => [
			domainObject = provider.createJavaTypeHandle(Button)
		]
	}
	
	def newNeonSignNode() {
		new NeonSignNode
	}

	def newMovieNode() {
		new MovieNode => [
			name = 'Movie'
			movieUrl = new URL(this.toURI('media/Usability.mp4'))
			width = 640
			height = 360
			player.seek(2.minutes)
		]
	}
	
	def newBrowserNode() {
		new BrowserNode => [
			name = 'Browser'
			width = 120
			height = 160
			pageUrl = new URL('http://koehnlein.blogspot.de/')
		]
	}
	
	def newBrickBreakerNode() {
		new BrickBreakerNode => [
			width = 640
			height = 480
		]
	}
	
	def newRecursiveImageNode() {
		new RecursiveImageNode => [
			image = ImageCache.get.getImage(this, 'media/laptop.jpg')
			x = 0
			y = -3
			scale = 0.6
			width = 80
			height = 60
		]
	}
	
	def newImageNode() {
		new ImageNode => [
			image = ImageCache.get.getImage(this, 'media/seltsam.jpg')
			width = 100
		]
	}

	protected def warmUpLayouter() {
		val Task<Void> task = [ |
			new Layouter
			null
		]
		task.run
	}
}
