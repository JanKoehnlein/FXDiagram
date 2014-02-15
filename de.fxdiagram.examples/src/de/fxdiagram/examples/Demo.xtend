package de.fxdiagram.examples

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.services.ResourceHandle
import de.fxdiagram.core.services.ResourceProvider
import de.fxdiagram.examples.ecore.EClassNode
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider
import de.fxdiagram.examples.java.JavaModelProvider
import de.fxdiagram.examples.java.JavaTypeNode
import de.fxdiagram.examples.lcars.LcarsDiagram
import de.fxdiagram.examples.lcars.LcarsModelProvider
import de.fxdiagram.examples.login.LoginNode
import de.fxdiagram.examples.neonsign.NeonSignNode
import de.fxdiagram.examples.slides.eclipsecon.IntroductionSlideDeck
import de.fxdiagram.examples.slides.eclipsecon.SummarySlideDeck
import de.fxdiagram.lib.media.BrowserNode
import de.fxdiagram.lib.media.ImageNode
import de.fxdiagram.lib.media.MovieNode
import de.fxdiagram.lib.media.RecursiveImageNode
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.lib.simple.SimpleNode
import java.net.URL
import javafx.application.Application
import javafx.application.Platform
import javafx.concurrent.Task
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.stage.Stage
import org.eclipse.emf.ecore.EcorePackage

class Demo extends Application {

	XRoot root
	
	ResourceProvider resourceProvider
	
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
		root.classLoader = class.classLoader
		val scene = new Scene(root, 1024, 768)
		scene.setCamera(new PerspectiveCamera)
		root.activate
		val diagram = new XDiagram
		root.rootDiagram = diagram
		resourceProvider = new ResourceProvider(class.classLoader)
		root.domainObjectProviders += #[ 
			new EcoreDomainObjectProvider,
			new JavaModelProvider,
			new LcarsModelProvider,
			resourceProvider
		]
		diagram => [
//			nodes += new DemoCampIntroSlides
			nodes += new IntroductionSlideDeck
			nodes += new OpenableDiagramNode('Basic') => [
				innerDiagram = new LazyExampleDiagram('')
			]
			nodes += new OpenableDiagramNode('JavaFX') => [
				innerDiagram = new XDiagram => [
					nodes += newLoginNode
					nodes += newRecursiveImageNode
					nodes += newImageNode
					nodes += newMovieNode
					nodes += newBrowserNode
					nodes += newBrickBreakerNode
				]
			]
			nodes += openableDiagram('Xtend', newNeonSignNode)
			nodes += openableDiagram('JavaFX Explorer', newJavaTypeNode)
			nodes += openableDiagram('Ecore Explorer', newEClassNode)
			nodes += newLcarsDiagramNode
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
		new OpenableDiagramNode('Gallery') => [
			innerDiagram = new XDiagram => [
				contentsInitializer = [
					nodes += new SimpleNode('Simple')
					nodes += new OpenableDiagramNode('Openable') => [
						innerDiagram = new LazyExampleDiagram('(n)')
					]
					nodes += new LevelOfDetailDiagramNode('Embedded') => [
						innerDiagram = new LazyExampleDiagram('(e)')
					]
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
		new OpenableDiagramNode('LCARS') => [
			innerDiagram = new LcarsDiagram
		]
	}
	
	protected def openableDiagram(String name, XNode node) {
		new OpenableDiagramNode(name) => [
			innerDiagram = new XDiagram => [
				nodes += node
			]
		]
	}
	
	def newLoginNode() {
		new LoginNode('Login')
	}
	
	def newEClassNode() {
		val provider = root.getDomainObjectProvider(EcoreDomainObjectProvider)
		new EClassNode(provider.createEClassDescriptor(EcorePackage.Literals.ECLASS))
	}
	
	def newJavaTypeNode() {
		val provider = root.getDomainObjectProvider(JavaModelProvider)
		new JavaTypeNode(provider.createJavaTypeDescriptor(Button))
	}
	
	def newNeonSignNode() {
		new NeonSignNode('NeonSign')
	}

	def newMovieNode() {
		new MovieNode(newResource('Movie', 'media/Usability.mp4')) => [
			width = 640
			height = 360
		]
	}
	
	def newBrowserNode() {
		new BrowserNode('Browser') => [
			width = 120
			height = 160
			pageUrl = new URL('http://koehnlein.blogspot.de/')
		]
	}
	
	def newBrickBreakerNode() {
		new BrickBreakerNode('BrickBreaker') => [
			width = 640
			height = 480
		]
	}
	
	def newRecursiveImageNode() {
		new RecursiveImageNode(newResource('laptop', 'media/laptop.jpg')) => [
			x = 0
			y = -3
			scale = 0.6
			width = 80
			height = 60
		]
	}
	
	def newImageNode() {
		new ImageNode(newResource('seltsam', 'media/seltsam.jpg')) => [
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
	
	protected def newResource(String name, String relativePath) {
		resourceProvider.createResourceDescriptor(new ResourceHandle(name, this.class, relativePath))
	}
}
