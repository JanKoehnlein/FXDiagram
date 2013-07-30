package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import de.fxdiagram.lib.simple.AddRapidButtonBehavior
import java.util.Deque
import java.util.LinkedList
import javafx.scene.Group
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.shape.Rectangle

import static extension de.fxdiagram.core.Extensions.*

class RecursiveImageNode extends XNode implements SvgExportable {

	@FxProperty Image image
	@FxProperty double x
	@FxProperty double y
	@FxProperty double scale

	Deque<Group> panes = new LinkedList<Group>

	new(Image image, double x, double y, double scale) {
		this.image = image
		this.x = x
		this.y = y
		this.scale = scale
		val rootPane = createPane()
		node = rootPane
		panes.push(rootPane)
	}

	override doActivate() {
		super.doActivate()
		updateChildPanes
		getRootDiagram.scaleProperty.addListener [ prop, oldVal, newVal |
			updateChildPanes
		]
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate
	}

	protected def Group createPane() {
		val pane = new Group => [
			children += new ImageView => [
				it.imageProperty.bind(imageProperty)
				preserveRatio = true
				fitWidthProperty.bind(this.widthProperty)
				fitHeightProperty.bind(this.heightProperty)
			]
			clip = new Rectangle => [
				x = 0
				y = 0
				widthProperty.bind(this.widthProperty)
				heightProperty.bind(this.heightProperty)
			]
		]
		pane
	}

	def void updateChildPanes() {
		while (!panes.empty) {
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
					scaleXProperty.bind(this.scaleProperty)
					scaleYProperty.bind(this.scaleProperty)
					layoutXProperty.bind(this.xProperty)
					layoutYProperty.bind(this.yProperty)
				]
				child.children += grandChild
				panes.push(child)
				panes.push(grandChild)
				
			} else {
				panes.push(child)
				return
			}
		}
	}
	
	override toSvgElement(extension SvgExporter exporter) {
		exporter.snapshotToSvgElement(this.node)
	}

}
