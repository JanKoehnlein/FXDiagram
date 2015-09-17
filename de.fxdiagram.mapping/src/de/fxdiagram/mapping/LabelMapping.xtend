package de.fxdiagram.mapping

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XLabel
import de.fxdiagram.mapping.shapes.BaseConnectionLabel
import de.fxdiagram.mapping.shapes.BaseNodeLabel
import javafx.geometry.Insets
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

abstract class AbstractLabelMapping<T> extends AbstractMapping<T> {
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}

	def XLabel createLabel(IMappedElementDescriptor<T> descriptor)
	
	def void styleText(Text text, T element) {
	}
	
	def String getText(T element) {
		''
	}
}

class ConnectionLabelMapping<T> extends AbstractLabelMapping<T> {
	
	new(XDiagramConfig config, String id) {
		super(config, id, id)
	}
	
	override XConnectionLabel createLabel(IMappedElementDescriptor<T> descriptor) {
		new BaseConnectionLabel(descriptor) 
	}
}

class NodeLabelMapping<T> extends AbstractLabelMapping<T> {
	
	new(XDiagramConfig config, String id) {
		super(config, id, id)
	}
	
	override BaseNodeLabel<T> createLabel(IMappedElementDescriptor<T> descriptor) {
		new BaseNodeLabel(descriptor)
	}
}

class NodeHeadingMapping<T> extends NodeLabelMapping<T> {
	
	new(XDiagramConfig config, String id) {
		super(config, id)
	}
	
	override BaseNodeLabel<T> createLabel(IMappedElementDescriptor<T> descriptor) {
		super.createLabel(descriptor) => [
			StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			text.font = Font.font(text.font.family, FontWeight.BOLD, text.font.size * 1.1)	
		]
	}
}