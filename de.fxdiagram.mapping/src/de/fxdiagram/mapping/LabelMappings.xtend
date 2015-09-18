package de.fxdiagram.mapping

import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XLabel
import de.fxdiagram.mapping.execution.XDiagramConfig
import de.fxdiagram.mapping.shapes.BaseConnectionLabel
import de.fxdiagram.mapping.shapes.BaseNodeLabel
import javafx.geometry.Insets
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

/**
 * Base class for label mappings. 
 * 
 * As labels are integral parts of nodes and connections, the create method is always called 
 * from within a transaction and can therefore provide the domain object itself. 
 */
abstract class AbstractLabelMapping<T> extends AbstractMapping<T> {
	
	new(XDiagramConfig config, String id, String displayName) {
		super(config, id, displayName)
	}

	def XLabel createLabel(IMappedElementDescriptor<T> labelDescriptor, T labelElement)
}

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to a connection's {@link XConnectionLabel}.
 * 
 * @see AbstractMapping
 */
class ConnectionLabelMapping<T> extends AbstractLabelMapping<T> {
	
	new(XDiagramConfig config, String id) {
		super(config, id, id)
	}
	
	override XConnectionLabel createLabel(IMappedElementDescriptor<T> descriptor, T labelElement) {
		new BaseConnectionLabel(descriptor) => [
			text.text = labelElement.text
		]
	}
	
	def String getText(T labelElement) {
		null
	}
}

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to a node's {@link XLabel}.
 * 
 * @see AbstractMapping
 */
class NodeLabelMapping<T> extends AbstractLabelMapping<T> {
	
	new(XDiagramConfig config, String id) {
		super(config, id, id)
	}
	
	override BaseNodeLabel<T> createLabel(IMappedElementDescriptor<T> descriptor, T labelElement) {
		new BaseNodeLabel(descriptor) => [
			text.text = labelElement.text
		]
	}
	
	def String getText(T labelElement) {
		null
	}
}

/**
 * A fixed mapping from a domain object represented by a {@link IMappedElementDescriptor} 
 * to a node's {@link XLabel}.
 * 
 * As opposed to {@link NodeLabelMapping} this label will use a bigger, bold font. 
 * 
 * @see AbstractMapping
 */
class NodeHeadingMapping<T> extends NodeLabelMapping<T> {
	
	new(XDiagramConfig config, String id) {
		super(config, id)
	}
	
	override BaseNodeLabel<T> createLabel(IMappedElementDescriptor<T> descriptor, T labelElement) {
		super.createLabel(descriptor, labelElement) => [
			StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			text.font = Font.font(text.font.family, FontWeight.BOLD, text.font.size * 1.1)	
		]
	}
}