package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import javafx.scene.Group
import javafx.scene.transform.Affine

import static de.fxdiagram.core.binding.NumberExpressionExtensions.*

@Logging
class XRootDiagram extends XAbstractDiagram {
	
	Group nodeLayer = new Group
	Group buttonLayer = new Group
	
	public static val MIN_SCALE = EPSILON
	
	@FxProperty double scale = 1.0
	
	XRoot root
	
	Affine canvasTransform
	
	new(XRoot root) {
		this.root = root
		children += nodeLayer
		children += buttonLayer
		canvasTransform = new Affine
		transforms.setAll(canvasTransform)
	}
	
	override doActivate() {
		super.doActivate
	}

	override getNodeLayer() {
		nodeLayer
	}
	
	override getConnectionLayer() {
		nodeLayer		
	}
	
	override getButtonLayer() {
		buttonLayer
	}
		
	def getCanvasTransform() {
		canvasTransform
	}
}