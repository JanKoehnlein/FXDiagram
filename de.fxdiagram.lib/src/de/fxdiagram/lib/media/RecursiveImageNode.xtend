package de.fxdiagram.lib.media

import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import de.fxdiagram.lib.simple.AddRapidButtonBehavior
import java.util.Deque
import java.util.LinkedList
import javafx.scene.SnapshotParameters
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.transform.Scale

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.geometry.TransformExtensions.*

class RecursiveImageNode extends XNode implements SvgExportable {

	// TODO convert to FxProperties
	Image image
	double x
	double y
	double scale

	Deque<Pane> panes = new LinkedList<Pane>

	new(Image image, double x, double y, double scale) {
		this.image = image
		this.x = x
		this.y = y
		this.scale = scale
		node = createPane()
	}

	override doActivate() {
		super.doActivate()
		updateChildPanes
		getRootDiagram.boundsInParentProperty.addListener [ prop, oldVal, newVal |
			updateChildPanes
		]
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate
	}

	override setHeight(double height) {
		super.setHeight(height)
		panes.forEach[(children.head as ImageView)?.setFitHeight(height)]
	}

	override setWidth(double width) {
		super.setWidth(width)
		panes.forEach[(children.head as ImageView)?.setFitWidth(width)]
	}

	protected def Pane createPane() {
		val pane = new Pane => [
			children += new ImageView => [
				it.image = image
				preserveRatio = true
				fitWidth = this.prefWidth(-1)
				fitHeight = this.prefHeight(-1)
				it.layoutXProperty.addListener [
					println("foo")
				]
				it.translateXProperty.addListener [
					println("bar")
				]
				it.xProperty.addListener [
					println("foobar")
				]
			]
		]
		panes.push(pane)
		pane
	}

	def void updateChildPanes() {
		while (panes.size > 0) {
			val child = panes.pop
			val parent = panes.peek
			val bounds = child.localToScene(child.boundsInLocal)
			val area = bounds.width * bounds.height
			if (area <= 10) {
				if (parent != null) {
					parent.children -= child
				} else {

					// never remove the first image
					panes.push(child)
					return
				}
			} else if (area > 500) {
				val grandChild = createPane() => [
					it.scaleX = scale
					it.scaleY = scale
					it.relocate(this.x, this.y)
					it.layoutXProperty.addListener [
						println("foo")
					]
					it.translateXProperty.addListener [
						println("bar")
					]
				]
				child.children += grandChild
				panes.push(child)
				panes.push(grandChild)
			} else
				return
		}
	}

	override toSvgElement(extension SvgExporter exporter) {
		val view = panes?.head?.children?.head as ImageView
		if (view != null) {
			val imageScale = min(view.fitWidth / image.width, view.fitHeight / image.height)
			val imageWidth = (image.width / imageScale) as int
			val imageHeight = (image.height / imageScale) as int
			val image = new WritableImage(imageWidth, imageHeight)
			val renderScale = imageWidth / layoutBounds.width
			val t = localToDiagramTransform * new Scale(renderScale, renderScale, renderScale)
			snapshot(
				new SnapshotParameters => [
					transform = t
				], image)
			return toSvgImage(this, image)
		}
		''
	}
}
