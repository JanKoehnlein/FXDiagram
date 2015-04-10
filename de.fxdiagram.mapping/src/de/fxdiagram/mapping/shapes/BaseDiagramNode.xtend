package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

import static de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*

/**
 * Base implementation for a {@link XNode} with a nested {@link XDiagram} that belongs to an
 * {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector. 
 */
@ModelNode
class BaseDiagramNode<T> extends OpenableDiagramNode {
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override getDomainObject() {
		super.getDomainObject() as IMappedElementDescriptor<T>
	}

	override initializeGraphics() {
		super.initializeGraphics()
		pane.backgroundPaint = new LinearGradient(
			0, 0, 1, 1, 
			true, CycleMethod.NO_CYCLE,
			#[
				new Stop(0, Color.rgb(242,236,181)), 
				new Stop(1, Color.rgb(255,248,202))
			]) 
	}

	override doActivate() {
		super.doActivate()
		val descriptor = domainObject
		if(descriptor instanceof IMappedElementDescriptor<?>) 
			addLazyBehavior(this, descriptor)
		innerDiagram.layoutOnActivate = LayoutType.DOT
	}
}