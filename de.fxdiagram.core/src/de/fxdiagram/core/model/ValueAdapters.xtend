package de.fxdiagram.core.model

import de.fxdiagram.annotations.properties.FxProperty
import javafx.scene.paint.Color
import javafx.beans.property.Property

/**
 * Adapter interface for serializing instances of classes that don't implement
 * {@link XModelProvider}.  
 */
interface ValueAdapter<T> extends ModelElement {
	
	def T getValueObject()
	
}

class ColorAdapter implements ValueAdapter<Color> {
	
	@FxProperty double red
	@FxProperty double green 
	@FxProperty double blue
	@FxProperty double opacity
	
	Color color
	
	new() {
	}

	new(Color color) {
		this.red = color.red
		this.green = color.green
		this.blue = color.blue
		this.opacity = color.opacity
		this.color = color
	}
	
	override getValueObject() {
		new Color(red, green, blue, opacity)
	}
	
	override getProperties() {
		#[redProperty, greenProperty, blueProperty, opacityProperty]
	}
	
	override getListProperties() {
		emptyList
	}
	
	override getType(Property<?> property) {
		Double
	}
	
	override isPrimitive(Property<?> property) {
		true
	}
	
	override getNode() {
		if(color == null)
			color = new Color(red, green, blue, opacity)
		color
	}
	
}