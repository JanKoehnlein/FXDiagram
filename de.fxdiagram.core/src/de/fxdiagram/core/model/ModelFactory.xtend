package de.fxdiagram.core.model

import de.fxdiagram.annotations.logging.Logging
import javafx.scene.text.Text

@Logging
class ModelFactory {
	
	protected def ModelElement createElement(Object node) {
		if(node == null)
			return null
		val element = new ModelElement(node)
		node.populate(element)
		element
	}
	
	protected def dispatch populate(XModelProvider node, ModelElement it) {
		node.populate(it)
	}

	protected def dispatch populate(Text text, ModelElement it) {
		addProperty(text.textProperty, String)
	}

	protected def dispatch populate(Object object, ModelElement it) {
		LOG.severe("No model population strategy for " + object.toString())
		null
	}
}