package de.fxdiagram.core.model

import de.fxdiagram.annotations.logging.Logging
import javafx.scene.paint.Color
import javafx.scene.text.Text

@Logging
class ModelFactory {
	
	val valueAdapters = #{
		Color.name -> ColorAdapter
	}
	
	protected def ModelElement createElement(String className) {
		val valueAdapter = valueAdapters.get(className)
		if(valueAdapter != null) {
			valueAdapter.newInstance()
		} else {
			val clazz = Class.forName(className)
			val node = clazz.newInstance()
			createElement(node)
		}
	}
	
	protected def ModelElement createElement(Object node) {
		if(node == null)
			return null
		node.create()
	}
	
	protected def dispatch create(XModelProvider node) {
		val element = new ModelElementImpl(node)
		node.populate(element)
		element
	}

	protected def dispatch create(Text text) {
		val element = new ModelElementImpl(text)
		element.addProperty(text.textProperty, String)
		element
	}

	protected def dispatch create(Color color) {
		val element = new ColorAdapter(color)
		element
	}

	protected def dispatch ModelElement create(Object object) {
		LOG.severe("No model population strategy for " + object.toString())
		null
	}
}